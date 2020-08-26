package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.commons.golbal.result.ResultCode;
import com.soonphe.portal.entity.*;
import com.soonphe.portal.service.*;
import com.soonphe.portal.util.googleAuth.GoogleAuthenticator;
import com.soonphe.portal.util.rate.TronApiUtil;
import com.soonphe.portal.util.redis.StringRedisUtil;
import com.soonphe.portal.vo.WalletPrivateVo;
import com.soonphe.portal.vo.WalletTeamVo;
import com.soonphe.portal.vo.WalletVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.soonphe.portal.util.redis.RedisKeyConstant.WALLET_LIMIT;

/**
 * <p>
 * 钱包 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-05-27
 */
@RestController
@RequestMapping("/wms-wallet")
@Api(tags = "【钱包】钱包")
public class WmsWalletController {

    private static final Logger logger = LoggerFactory.getLogger(WmsWalletController.class);

    @Autowired
    private IWmsWalletService service;
    @Autowired
    private IWmsWalletHistoryService historyService;
    @Autowired
    private IOmsOrePoolService orePoolService;
    @Autowired
    private IUmsCommunityProfitService communityProfitService;
    @Autowired
    private IUmsContractService contractService;
    @Autowired
    private IWmsWalletCnyHistoryService wmsWalletCnyHistoryService;
    @Autowired
    private IWmsWalletFtHistoryService wmsWalletFtHistoryService;
    @Autowired
    private ISmsBaseService smsBaseService;


    /**
     * 查询钱包
     *
     * @param address
     * @return
     */
    @ApiOperation("查询钱包")
    @GetMapping("/getWallet")
    public ResponseResult<WmsWallet> getWallet(@ApiParam(required = true, value = "地址")
                                                 @RequestParam(value = "address") String address) {
        if(StringUtils.isBlank(address)){
            return ResponseResult.failed(ResultCode.VALIDATE_FAILED.getCode(),"钱包地址不允许为空");
        }
        WmsWallet wallet = service.getOne(new QueryWrapper<WmsWallet>().lambda()
                .eq(WmsWallet::getTrxAddress, address));
        if(wallet==null) {
            return ResponseResult.failed(ResultCode.VALIDATE_FAILED.getCode(),"当前用户钱包不存在");
        }
        SmsBase base = smsBaseService.list().get(0);
        wallet.setReleaseRatio(wallet.getReleaseRatio().add(base.getWinSpeed()));
        wallet.setPassword("");
        return ResponseResult.success(wallet);
    }


    /**
     * 查询/创建/导入钱包
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("查询/创建/导入钱包")
    @PostMapping("/createWallet")
    public ResponseResult<WmsWallet> createWallet(@ApiParam(required = true, value = "实体")
                                                    @RequestBody WmsWallet tyAdvert) {

        if (tyAdvert.getTrxAddress() != null || tyAdvert.getPrivateKey() != null || tyAdvert.getWordKey() != null) {
            if (syncAdress(tyAdvert.getTrxAddress())) {
                return ResponseResult.failed("访问超出限制次数");
            }
            WmsWallet wallet = service.getOne(new QueryWrapper<WmsWallet>().lambda()
                    .eq(WmsWallet::getTrxAddress, tyAdvert.getTrxAddress()));
            if (wallet != null) {
                if (!StringUtils.isEmpty(tyAdvert.getName())) {
                    wallet.setName(tyAdvert.getName());
                }
                if (!StringUtils.isEmpty(tyAdvert.getPassword())) {
                    wallet.setPassword(tyAdvert.getPassword());
                }
                if (wallet.getContractNewLeve() > 0 && wallet.getContractNewExpireDate().before(new Date())) {
                    wallet.setGameNum(wallet.getGameNum() - wallet.getContractNewGameNum());
                    wallet.setContractNewGameNum(0);
                    wallet.setContractNewLeve(0);
                    service.updateById(wallet);
                }
                if (wallet.getContractGameNum() > 0) {
                    UmsContract contract = contractService.getById(wallet.getContractLeve());
                    if (wallet.getWinCny().compareTo(contract.getSpecMax()) >= 0) {
                        wallet.setContractGameNum(0);
                        wallet.setContractLeve(0);
                        wallet.setGameNum(wallet.getGainpackGameNum() + wallet.getRecommendGameNum());
                        service.updateById(wallet);
                    }
                    if (wallet.getContractExpireDate().before(new Date())) {
                        wallet.setGameNum(wallet.getGameNum() - wallet.getContractGameNum());
                        wallet.setContractGameNum(0);
                        wallet.setContractLeve(0);
                        service.updateById(wallet);
                    }
                } else {
                    service.updateById(wallet);
                }
                SmsBase base = smsBaseService.list().get(0);
                wallet.setReleaseRatio(wallet.getReleaseRatio().add(base.getWinSpeed()));
                wallet.setPassword("");
                return ResponseResult.success(wallet);
            }
            tyAdvert.setPrivateKey(UUID.randomUUID().toString());
            tyAdvert.setTrxAddress(tyAdvert.getTrxAddress());
            String workKey = "";
            for (int i = 0; i < 12; i++) {
                if (i != 0) {
                    workKey += ",";
                }
                workKey += createWord(2, 6);
            }
            tyAdvert.setWordKey(workKey);
            service.save(tyAdvert);
            return ResponseResult.success(tyAdvert);
        }
        return ResponseResult.failed("接口参数不对");
    }

    public synchronized boolean syncAdress(String address) {

        if (StringRedisUtil.contain(WALLET_LIMIT + address)) {
            return true;
        }
        StringRedisUtil.set(WALLET_LIMIT + address, "1", 2, TimeUnit.SECONDS);
        return false;
    }

    public static String createWord(int min, int max) {
        int count = (int) (Math.random() * (max - min + 1)) + min;
        String str = "";
        for (int i = 0; i < count; i++) {
            str += (char) ((int) (Math.random() * 26) + 'a');
        }
        return str;
    }

    /**
     * 验证钱包密码
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("验证钱包密码")
    @PostMapping("/checkPassword")
    public ResponseResult<Boolean> checkPassword(@ApiParam(required = true, value = "实体")
                                                   @RequestBody WalletVo tyAdvert) {
        if (tyAdvert.getPrivateKey() != null || tyAdvert.getWordKey() != null) {
            WmsWallet wallet = service.getOne(new QueryWrapper<WmsWallet>().lambda()
                    .eq(WmsWallet::getPrivateKey, tyAdvert.getPrivateKey())
                    .or(e -> e.eq(WmsWallet::getWordKey, tyAdvert.getWordKey())));
            if (wallet != null) {
                if (!wallet.getPassword().equals(tyAdvert.getPassword())) {
                    return ResponseResult.failed("账户密码不匹配");
                }
                return ResponseResult.success(true);
            }
            return ResponseResult.failed("找不到对应的钱包");
        }
        return ResponseResult.failed("缺少私钥或助记词");
    }

    /**
     * 更新钱包——使用privateKey/wordKey
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("更新钱包")
    @PostMapping("/updateWallet")
    public ResponseResult<Boolean> updateWallet(@ApiParam(required = true, value = "实体")
                                                  @RequestBody WalletVo tyAdvert) {
        if (tyAdvert.getPrivateKey() != null || tyAdvert.getWordKey() != null) {
            WmsWallet wallet = service.getOne(new QueryWrapper<WmsWallet>().lambda()
                    .eq(WmsWallet::getPrivateKey, tyAdvert.getPrivateKey())
                    .or(e -> e.eq(WmsWallet::getWordKey, tyAdvert.getWordKey())));
            if (wallet != null) {
                if (!wallet.getPassword().equals(tyAdvert.getPassword())) {
                    return ResponseResult.failed("账户密码不匹配");
                }
                if (!"".equals(tyAdvert.getName())) {
                    wallet.setName(tyAdvert.getName());
                }
                if (!"".equals(tyAdvert.getNewPassword())) {
                    wallet.setPassword(tyAdvert.getNewPassword());
                }
                return ResponseResult.success(service.updateById(wallet));
            }
            return ResponseResult.failed("找不到对应的钱包");
        }
        return ResponseResult.failed("缺少私钥或助记词");
    }

    /**
     * 更新私钥或助记词
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("更新私钥或助记词")
    @PostMapping("/updateWalletPrivateKey")
    public ResponseResult<WalletPrivateVo> updateWalletPrivateKey(@ApiParam(required = true, value = "实体")
                                                                    @RequestBody WalletPrivateVo tyAdvert) {
        if (tyAdvert.getPrivateKey() != null || tyAdvert.getWordKey() != null) {
            WmsWallet wallet = service.getOne(new QueryWrapper<WmsWallet>().lambda()
                    .eq(WmsWallet::getPrivateKey, tyAdvert.getPrivateKey())
                    .or(e -> e.eq(WmsWallet::getWordKey, tyAdvert.getWordKey())));
            if (wallet != null) {
                if (wallet.getState() != 0) {
                    return ResponseResult.failed("钱包不能重复更新私钥或助记词");
                }
                wallet.setState(1);
                wallet.setPrivateKey(UUID.randomUUID().toString());
                String workKey = "";
                for (int i = 0; i < 12; i++) {
                    if (i != 0) {
                        workKey += ",";
                    }
                    workKey += createWord(2, 6);
                }
                wallet.setWordKey(workKey);
                service.updateById(wallet);
                tyAdvert.setNewPrivateKey(wallet.getPrivateKey());
                tyAdvert.setNewWordKey(wallet.getWordKey());
                return ResponseResult.success(tyAdvert);
            }
            return ResponseResult.failed("找不到对应的钱包");
        }
        return ResponseResult.failed("缺少私钥或助记词");
    }

    /**
     * Tron转入钱包专用回调
     *
     * @return
     */
    @ApiOperation("Tron转入钱包专用回调")
    @PostMapping("/walletRecharge")
    public ResponseResult<Boolean> walletRecharge(@ApiParam(required = true, value = "发送地址")
                                                    @RequestParam(value = "sender", required = false) String sender,
                                                    @ApiParam(required = true, value = "发送地址")
                                                    @RequestParam(value = "address") String address,
                                                    @ApiParam(required = true, value = "交易时间")
                                                    @RequestParam(value = "paytime") Long paytime,
                                                    @ApiParam(required = true, value = "数量")
                                                    @RequestParam(value = "paynumber") BigDecimal paynumber,
                                                    @ApiParam(required = true, value = "货币类型")
                                                    @RequestParam(value = "symbol") String symbol) {

        WmsWallet receiveWallet = service.getOne(new QueryWrapper<WmsWallet>().lambda()
                .eq(WmsWallet::getTrxAddress, address));
        if (receiveWallet == null) {
            receiveWallet = new WmsWallet();
            receiveWallet.setTrxAddress(address);
            receiveWallet.setPrivateKey(UUID.randomUUID().toString());
            String workKey = "";
            for (int i = 0; i < 12; i++) {
                if (i != 0) {
                    workKey += ",";
                }
                workKey += createWord(2, 6);
            }
            receiveWallet.setWordKey(workKey);
            receiveWallet.setTrxAmount(new BigDecimal(0));
            receiveWallet.setFtAmount(new BigDecimal(0));
            receiveWallet.setRecharge(new BigDecimal(0));
        }

        WmsWalletHistory history = new WmsWalletHistory();
        history.setState(1);
        history.setReceiveAddress(address);
        history.setCreateTime(new Date(paytime));
        history.setSendAddress(sender);
        history.setReceiveAddress(address);
        history.setAmount(paynumber);
        switch (symbol) {
            case "TRX":
                history.setType(1);
                receiveWallet.setTrxAmount(receiveWallet.getTrxAmount().add(paynumber));
                break;
            case "FT":
                history.setType(2);
                receiveWallet.setFtAmount(receiveWallet.getFtAmount().add(paynumber));
                break;
            case "USDT":
                history.setType(1);
                receiveWallet.setTrxAmount(receiveWallet.getTrxAmount().add(paynumber));
                receiveWallet.setRecharge(receiveWallet.getRecharge().add(paynumber));
                break;
        }
        service.saveOrUpdate(receiveWallet);

        boolean result = historyService.save(history);
        return ResponseResult.success(result);
    }

    /**
     * 钱包充值地址
     *
     * @return
     */
    @ApiOperation("钱包充值地址")
    @GetMapping("/getRechargeAddress")
    public ResponseResult<String> getRechargeAddress(@ApiParam(required = true, value = "货币类型")
                                                       @RequestParam(value = "symbol") String symbol) {
        String address = TronApiUtil.WALLET_RECHARGE(symbol);
        return ResponseResult.success(address);
    }

    /**
     * 生成Secret
     *
     * @param trxAddress
     * @return
     */
    @ApiOperation("生成Secret")
    @GetMapping("/googleSecretCreate")
    public ResponseResult<String> googleSecretCreate(@ApiParam(required = true, value = "trxAddress")
                                                       @RequestParam(value = "trxAddress") String trxAddress) {
        WmsWallet wallet = service.getOne(new QueryWrapper<WmsWallet>().lambda()
                .eq(WmsWallet::getTrxAddress, trxAddress));
        String secret = GoogleAuthenticator.generateSecretKey();
        wallet.setGSecret(secret);
        service.updateById(wallet);
        return ResponseResult.success(secret);
    }


    @ApiOperation("谷歌验证")
    @GetMapping("/googleSecretCheck")
    public ResponseResult<Boolean> googleSecretCheck(@ApiParam(required = true, value = "trxAddress")
                                                       @RequestParam(value = "trxAddress") String trxAddress,
                                                       @ApiParam(required = true, value = "codes")
                                                       @RequestParam(value = "codes") String codes,
                                                       @ApiParam(required = false, value = "secret")
                                                       @RequestParam(required = false, value = "secret") String secret) {
        WmsWallet wallet = service.getOne(new QueryWrapper<WmsWallet>().lambda()
                .eq(WmsWallet::getTrxAddress, trxAddress));
        if (secret == null || "".equals(secret)) {
            secret = wallet.getGSecret();
        }
        Boolean result = GoogleAuthenticator.authcode(codes, secret);
        if (result) {
            if (wallet.getState() != 2) {
                wallet.setState(2);
                service.updateById(wallet);
            }
        }
        return ResponseResult.success(result);
    }

    /**
     * 转出/转入钱包
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("转出/转入钱包")
    @PostMapping("/transferWallet")
    public ResponseResult<WmsWalletHistory> transferWallet(@ApiParam(required = true, value = "实体")
                                                             @RequestBody WmsWalletHistory tyAdvert) {
        if (tyAdvert.getSendId() == null || tyAdvert.getAmount() == null || tyAdvert.getType() == null) {
            ResponseResult.failed(ResultCode.AUDITFAIL.getCode(), "请求参数错误");
        }
        WmsWalletHistory result = service.transferWallet(tyAdvert);
        return ResponseResult.success(result);
    }

    /**
     * 钱包归集
     *
     * @param symbol
     * @return
     */
    @ApiOperation("钱包归集")
    @GetMapping("/fundCollection")
    public ResponseResult<Boolean> fundCollection(@ApiParam(required = true, value = "symbol")
                                                    @RequestParam(value = "symbol") String symbol) {
        TronApiUtil.WALLET_TRANSFER_FUNDCOLLECTION(symbol);
        return ResponseResult.success(true);
    }

    /**
     * 转出/转入交易记录
     *
     * @param uid
     * @return
     */
    @ApiOperation("转出/转入交易记录")
    @GetMapping("/getHistory")
    public ResponseResult<List<WmsWalletHistory>> getHistory(@ApiParam(required = true, value = "pageNum")
                                                               @RequestParam(value = "pageNum") int pageNum,
                                                               @ApiParam(required = true, value = "pageSize")
                                                               @RequestParam(value = "pageSize") int pageSize,
                                                               @ApiParam(required = true, value = "uid")
                                                               @RequestParam(value = "uid") int uid,
                                                               @ApiParam(required = true, value = "币种 1TRX 2FT")
                                                               @RequestParam(value = "type") int type,
                                                               @ApiParam(required = true, value = "类型 1转入 2转出")
                                                               @RequestParam(value = "state") int state) {
        WmsWallet wallet = service.getById(uid);
        if (wallet == null) {
            return ResponseResult.failed("钱包未找到");
        }
        LambdaQueryWrapper<WmsWalletHistory> wrapper = new QueryWrapper<WmsWalletHistory>().lambda()
                .eq(type > 0, WmsWalletHistory::getType, type)
                .eq(state > 0, WmsWalletHistory::getState, state);
        if (state == 0) {
            wrapper.eq(WmsWalletHistory::getReceiveAddress, wallet.getTrxAddress())
                    .eq(WmsWalletHistory::getState, 1)
                    .or(e -> e.eq(WmsWalletHistory::getSendAddress, wallet.getTrxAddress())
                            .eq(WmsWalletHistory::getState, 2));
        } else if (state == 1) {
            wrapper.eq(WmsWalletHistory::getReceiveAddress, wallet.getTrxAddress());
        } else if (state == 2) {
            wrapper.eq(WmsWalletHistory::getSendId, uid);
        }
        wrapper.orderByDesc(WmsWalletHistory::getId);
        List<WmsWalletHistory> list = historyService.page(new Page<>(pageNum, pageSize),
                wrapper).getRecords();
        return ResponseResult.success(list);
    }

    /**
     * 后台转出/转入交易记录
     *
     * @param uid
     * @return
     */
    @ApiOperation("后台获取/转入交易记录")
    @GetMapping("/getTransferHistory")
    public ResponseResult<List<WmsWalletHistory>> getTransferHistory(
            @ApiParam(required = true, value = "pageNum")
            @RequestParam(value = "pageNum") int pageNum,
            @ApiParam(required = true, value = "pageSize")
            @RequestParam(value = "pageSize") int pageSize,
            @ApiParam(required = true, value = "uid")
            @RequestParam(value = "uid") String uid,
            @ApiParam(required = true, value = "币种 1TRX 2FT")
            @RequestParam(value = "type") int type,
            @ApiParam(required = true, value = "类型 1转入 2转出")
            @RequestParam(value = "state") int state) {

        LambdaQueryWrapper<WmsWalletHistory> wrapper = new QueryWrapper<WmsWalletHistory>().lambda()
                .eq(type > 0, WmsWalletHistory::getType, type)
                .eq(state > 0, WmsWalletHistory::getState, state);
        if (uid != null && !"".equals(uid)) {
            WmsWallet wallet = service.getById(uid);
            if (wallet == null) {
                return ResponseResult.failed("钱包未找到");
            }
            if (state == 0) {
                wrapper.eq(WmsWalletHistory::getReceiveAddress, wallet.getTrxAddress())
                        .eq(WmsWalletHistory::getState, 1)
                        .or(e -> e.eq(WmsWalletHistory::getSendAddress, wallet.getTrxAddress())
                                .eq(WmsWalletHistory::getState, 2));
            } else if (state == 1) {
                wrapper.eq(WmsWalletHistory::getReceiveAddress, wallet.getTrxAddress());
            } else if (state == 2) {
                wrapper.eq(WmsWalletHistory::getSendId, uid);
            }
        }
        wrapper.orderByDesc(WmsWalletHistory::getId);
        IPage<WmsWalletHistory> page = historyService.page(new Page<>(pageNum, pageSize),
                wrapper);
        return ResponseResult.success(page.getRecords(), "", page.getTotal() + "");
    }

    /**
     * 团队人数统计
     *
     * @param uid
     * @return
     */
    @ApiOperation("团队人数统计")
    @GetMapping("/getTeamNum")
    public ResponseResult<Integer> getTeamNum(@ApiParam(required = true, value = "uid")
                                                @RequestParam(value = "uid") int uid) {

        List<WmsWallet> list = service.list(new QueryWrapper<WmsWallet>().lambda()
                .like(WmsWallet::getRecommendId, uid)
        );
        return ResponseResult.success(list.size());
    }

    /**
     * 添加推荐人
     *
     * @param uid
     * @param recommendCode
     * @return
     */
    @ApiOperation("添加推荐人")
    @GetMapping("/createRecommend")
    public ResponseResult<Boolean> createRecommend(@ApiParam(required = true, value = "uid")
                                                     @RequestParam(value = "uid") int uid,
                                                     @ApiParam(required = true, value = "recommendCode")
                                                     @RequestParam(value = "recommendCode") String recommendCode) {
        WmsWallet wallet = service.getById(uid);
        if (wallet.getRecommendId() != null && !wallet.getRecommendId().isEmpty()) {
            return ResponseResult.failed("当前用户的推荐人已存在，请不要重复添加");
        }
        if (wallet.getRecommendCode().equals(recommendCode)) {
            return ResponseResult.failed("推荐人不能为自己");
        }
        WmsWallet recommender = service.getOne(new QueryWrapper<WmsWallet>().lambda()
                .eq(WmsWallet::getRecommendCode, recommendCode));
        if (recommender == null) {
            return ResponseResult.failed("邀请码无效");
        }
        //将推荐码的所有父类和推荐码自己的ID组合
        if (recommender.getRecommendId() != null && !recommender.getRecommendId().isEmpty()) {
            wallet.setRecommendId(recommender.getRecommendId() + "," + recommender.getId());
        } else {
            wallet.setRecommendId(recommender.getId() + "");
        }
        //创建推荐码
        wallet.setRecommendCode(createWord(6, 8));
        //设置团队人数
        wallet.setTeamNum(0);
        //推荐人更新人数
        recommendNumAdd(wallet);
        return ResponseResult.success(service.updateById(wallet));
    }

    /**
     * 查询直推或团队人数
     *
     * @param uid
     * @param type
     * @return
     */
    @ApiOperation("查询直推或团队人数")
    @GetMapping("/getTeamList")
    public ResponseResult<List<WalletTeamVo>> getTeamList(@ApiParam(required = true, value = "pageNum")
                                                            @RequestParam(value = "pageNum") int pageNum,
                                                            @ApiParam(required = true, value = "pageSize")
                                                            @RequestParam(value = "pageSize") int pageSize,
                                                            @ApiParam(required = true, value = "uid")
                                                            @RequestParam(value = "uid") int uid,
                                                            @ApiParam(required = true, value = "type 1团队 2直推")
                                                            @RequestParam(value = "type") int type) {

        int count = 0;
        if (type == 1) {
            count = service.count(new QueryWrapper<WmsWallet>().lambda()
                    .like(WmsWallet::getRecommendId, uid));
        } else {
            count = service.count(new QueryWrapper<WmsWallet>().lambda()
                    .likeLeft(WmsWallet::getRecommendId, uid));
        }
        if (count > 0) {
            WmsWallet wmsWallet = service.getById(uid);
            if (type == 1) {
                wmsWallet.setTeamNum(count);
            } else {
                wmsWallet.setRecommendCount(count);
            }
            service.updateById(wmsWallet);
        }

        List<WmsWallet> list;
        if (type == 1) {
            list = service.page(new Page<>(pageNum, pageSize), new QueryWrapper<WmsWallet>().lambda()
                    .like(WmsWallet::getRecommendId, uid))
                    .getRecords();
        } else {
            list = service.page(new Page<>(pageNum, pageSize), new QueryWrapper<WmsWallet>().lambda()
                    .likeLeft(WmsWallet::getRecommendId, uid))
                    .getRecords();
        }
        if (list.size() > 0) {

            List<WalletTeamVo> teamVos = new ArrayList<>();
            for (WmsWallet wallet : list) {
                WalletTeamVo vo = new WalletTeamVo();
                vo.setId(wallet.getId());
                vo.setName(wallet.getName());
                if (wallet.getGameTotal() >= 10) {
                    vo.setType(1);
                } else {
                    vo.setType(2);
                }
                vo.setCreateTime(wallet.getCreateTime());
                teamVos.add(vo);
            }
            return ResponseResult.success(teamVos);
        }
        return ResponseResult.success(new ArrayList<>());
    }


    /**
     * 推荐人上层团队和推荐人直推
     *
     * @param wallet
     */
    public void recommendNumAdd(WmsWallet wallet) {
        String[] recommendCode = wallet.getRecommendId().split(",");
        WmsWallet recommender = service.getById(recommendCode[recommendCode.length - 1]);
        if (recommender.getRecommendId() != null && !recommender.getRecommendId().isEmpty()) {
            String[] strings = recommender.getRecommendId().split(",");
            for (String str : strings) {
                WmsWallet top = service.getById(str);
                top.setTeamNum(top.getTeamNum() + 1);
                service.updateById(top);
            }
        }
        recommender.setRecommendCount(recommender.getRecommendCount() + 1);
        recommender.setTeamNum(recommender.getTeamNum() + 1);
        service.updateById(recommender);
    }

    /**
     * 获取当前trx转CNY汇率
     *
     * @return
     */
    @ApiOperation("获取当前trx转CNY汇率")
    @GetMapping("/trxToCNYRate")
    public ResponseResult<BigDecimal> trxToCNYRate() {
        List<SmsBase> list = smsBaseService.list();
        if (list.size() > 0) {
            return ResponseResult.success(list.get(0).getPriceCny());
        }
        return ResponseResult.failed("获取汇率失败请稍后再试");
    }

    /**
     * 获取当前CNY转FT汇率
     *
     * @return
     */
    @ApiOperation("获取当前CNY转FT汇率")
    @GetMapping("/CNYToFTRate")
    public ResponseResult<BigDecimal> CNYToFTRate() {
        BigDecimal rate = new BigDecimal(1);
        List<SmsBase> bases = smsBaseService.list();
        if (bases.size() > 0) {
            rate = bases.get(0).getPricePublic();
        }
        return ResponseResult.success(
                rate
//                orePoolService.getFtRate()
        );
    }

    /**
     * 本账户trx转换CNY
     *
     * @param uid
     * @param trxNum
     * @return
     */
    @ApiOperation("本账户trx转换CNY")
    @GetMapping("/trxToCNY")
    public ResponseResult<WmsWallet> trxToCNY(@ApiParam(required = true, value = "uid")
                                                @RequestParam(value = "uid") int uid,
                                                @ApiParam(required = true, value = "trxNum")
                                                @RequestParam(value = "trxNum") BigDecimal trxNum) {
        List<SmsBase> list = smsBaseService.list();
        if (list.size() < 1) {
            return ResponseResult.failed("汇率获取失败");
        }
//        CurrencyRate currencyRate = RateApiUtil.GET_TRON_RATE_API();

        BigDecimal cnyNum = trxNum.multiply(list.get(0).getPriceCny()).setScale(4, BigDecimal.ROUND_DOWN);
        WmsWallet wallet = service.getById(uid);
        if (trxNum.compareTo(wallet.getTrxAmount()) > 0) {
            return ResponseResult.failed("钱包余额不足");
        }
        wallet.setTrxAmount(wallet.getTrxAmount().subtract(trxNum));
        wallet.setCnyAmount(wallet.getCnyAmount().add(cnyNum));
        service.updateById(wallet);
        WmsWalletCnyHistory wmsWalletCnyHistory = new WmsWalletCnyHistory();
        wmsWalletCnyHistory.setUid(uid);
        wmsWalletCnyHistory.setType(1);
        wmsWalletCnyHistory.setTrxAmount(trxNum);
        wmsWalletCnyHistory.setCnyAmount(cnyNum);
        wmsWalletCnyHistory.setCreateTime(new Date());
        wmsWalletCnyHistoryService.save(wmsWalletCnyHistory);

        return ResponseResult.success(wallet);
    }

    /**
     * 本账户CNY转换trx
     *
     * @param uid
     * @param cnyNum
     * @return
     */
    @ApiOperation("本账户cny转换trx")
    @GetMapping("/cnyToTrx")
    public ResponseResult<WmsWallet> cnyToTrx(@ApiParam(required = true, value = "uid")
                                                @RequestParam(value = "uid") int uid,
                                                @ApiParam(required = true, value = "trxNum")
                                                @RequestParam(value = "cnyNum") BigDecimal cnyNum) {
        List<SmsBase> list = smsBaseService.list();
        if (list.size() < 1) {
            return ResponseResult.failed("汇率获取失败");
        }
        BigDecimal trxNum = cnyNum.divide(list.get(0).getPriceCny(), 4, BigDecimal.ROUND_DOWN);
        WmsWallet wallet = service.getById(uid);
        if (cnyNum.compareTo(wallet.getCnyAmount()) > 0) {
            return ResponseResult.failed("钱包cny数量不足");
        }
        wallet.setCnyAmount(wallet.getCnyAmount().subtract(cnyNum));
        wallet.setTrxAmount(wallet.getTrxAmount().add(trxNum));
        service.updateById(wallet);

        WmsWalletCnyHistory wmsWalletCnyHistory = new WmsWalletCnyHistory();
        wmsWalletCnyHistory.setUid(uid);
        wmsWalletCnyHistory.setType(2);
        wmsWalletCnyHistory.setTrxAmount(trxNum);
        wmsWalletCnyHistory.setCnyAmount(cnyNum);
        wmsWalletCnyHistory.setCreateTime(new Date());
        wmsWalletCnyHistoryService.save(wmsWalletCnyHistory);
        return ResponseResult.success(wallet);
    }

    /**
     * 游戏账户FT充值
     *
     * @param uid
     * @param ftNum
     * @return
     */
    @ApiOperation("游戏账户FT充值")
    @GetMapping("/ftRecharge")
    public ResponseResult<WmsWallet> ftRecharge(@ApiParam(required = true, value = "uid")
                                                  @RequestParam(value = "uid") int uid,
                                                  @ApiParam(required = true, value = "ftNum")
                                                  @RequestParam(value = "ftNum") BigDecimal ftNum) {

        WmsWallet wallet = service.getById(uid);
        if (ftNum.compareTo(wallet.getFtAmount()) > 0) {
            return ResponseResult.failed("钱包ft数量不足");
        }
        wallet.setFtAmount(wallet.getFtAmount().subtract(ftNum));
        wallet.setGameFtAmount(wallet.getGameFtAmount().add(ftNum));
        service.updateById(wallet);
        WmsWalletFtHistory wmsWalletFtHistory = new WmsWalletFtHistory();
        wmsWalletFtHistory.setUid(uid);
        wmsWalletFtHistory.setType(1);
        wmsWalletFtHistory.setFtAmount(ftNum);
        wmsWalletFtHistory.setGameFtAmount(ftNum);
        wmsWalletFtHistory.setCreateTime(new Date());
        wmsWalletFtHistoryService.save(wmsWalletFtHistory);
        return ResponseResult.success(wallet);
    }

    /**
     * 游戏账户FT提现
     *
     * @param uid
     * @param ftNum
     * @return
     */
    @ApiOperation("游戏账户FT提现")
    @GetMapping("/ftWithdrawal")
    public ResponseResult<WmsWallet> ftWithdrawal(@ApiParam(required = true, value = "uid")
                                                    @RequestParam(value = "uid") int uid,
                                                    @ApiParam(required = true, value = "ftNum")
                                                    @RequestParam(value = "ftNum") BigDecimal ftNum) {

        WmsWallet wallet = service.getById(uid);
        if (ftNum.compareTo(wallet.getGameFtAmount()) > 0) {
            return ResponseResult.failed("钱包游戏ft数量不足");
        }
        wallet.setFtAmount(wallet.getFtAmount().add(ftNum));
        wallet.setGameFtAmount(wallet.getGameFtAmount().subtract(ftNum));
        service.updateById(wallet);
        WmsWalletFtHistory wmsWalletFtHistory = new WmsWalletFtHistory();
        wmsWalletFtHistory.setUid(uid);
        wmsWalletFtHistory.setType(2);
        wmsWalletFtHistory.setFtAmount(ftNum);
        wmsWalletFtHistory.setGameFtAmount(ftNum);
        wmsWalletFtHistory.setCreateTime(new Date());
        wmsWalletFtHistoryService.save(wmsWalletFtHistory);
        return ResponseResult.success(wallet);
    }


}
