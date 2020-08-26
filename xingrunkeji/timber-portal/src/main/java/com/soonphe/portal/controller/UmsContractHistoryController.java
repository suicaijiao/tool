package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.service.*;
import com.soonphe.portal.entity.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户合约关联表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@RestController
@RequestMapping("/ums-contract-history")
@Api(tags = "【合约记录】合约记录")
public class UmsContractHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(UmsContractController.class);

    @Autowired
    private IUmsContractHistoryService service;
    @Autowired
    private IUmsContractService contractService;
    @Autowired
    private IWmsWalletService walletService;
    @Autowired
    private ISmsStatsService iSmsStatsService;
    @Autowired
    private IOmsOrePoolService orePoolService;

    /**
     * 创建
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("创建")
    @PostMapping("/createObj")
    public ResponseResult<UmsContractHistory> createObj(@ApiParam(required = true, value = "实体")
                                                          @RequestBody UmsContractHistory tyAdvert) {

        //查询用户钱包余额
        WmsWallet wallet = walletService.getById(tyAdvert.getUid());
        //查询合约
        UmsContract contract = contractService.getById(tyAdvert.getCid());
        if (contract.getState() == 2) {
            return ResponseResult.failed("合约不可用");
        }
        //计算合约usdt换算DOT
        BigDecimal ftSpec = contract.getSpec().multiply(orePoolService.getFtTicket());

        if (wallet == null || wallet.getFtAmount().compareTo(ftSpec) < 0) {
            return ResponseResult.failed("钱包余额不足");
        }
        if (wallet.getContractLeve() != null && wallet.getContractLeve() > 0) {
            return ResponseResult.failed("合约不可重复购买");
        }

        tyAdvert.setCreateDate(new Date());
        tyAdvert.setSpec(ftSpec);
        service.save(tyAdvert);
        wallet.setFtAmount(wallet.getFtAmount().subtract(ftSpec));
        //初始化winCny
        wallet.setWinCny(new BigDecimal(0));
        wallet.setContractLeve(contract.getLevel());
        wallet.setContractGameNum(contract.getDailyGameNum());
        wallet.setGameNum(wallet.getGameNum() + contract.getDailyGameNum());
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, +contract.getExpireDate());     //利用Calendar 实现 Date日期+1天
        wallet.setContractExpireDate(c.getTime());
        walletService.updateById(wallet);

        //统计每日数据
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String now = simpleDateFormat.format(new Date());
        SmsStats smsStats = new SmsStats();

        List<SmsStats> smsStatsList = iSmsStatsService.list(new QueryWrapper<SmsStats>().lambda()
                .eq(SmsStats::getCreateDate, now));
        if (smsStatsList.size() > 0) {
            smsStats = smsStatsList.get(0);
            smsStats.setContractCount(smsStats.getContractCount().add(contract.getSpec()));
        } else {
            smsStats.setCreateDate(new Date());
            smsStats.setContractCount(ftSpec);
        }
        iSmsStatsService.saveOrUpdate(smsStats);
        return ResponseResult.success(tyAdvert);
    }


    /**
     * 创建New
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("创建New")
    @PostMapping("/createNewObj")
    public ResponseResult<UmsContractHistory> createNewObj(@ApiParam(required = true, value = "实体")
                                                             @RequestBody UmsContractHistory tyAdvert) {

        //查询用户钱包余额
        WmsWallet wallet = walletService.getById(tyAdvert.getUid());
        //查询合约
        UmsContract contract = contractService.getById(tyAdvert.getCid());
        if (contract.getState() == 2) {
            return ResponseResult.failed("合约不可用");
        }

        //特殊逻辑
        if (tyAdvert.getCid() != 8) {
            return ResponseResult.failed("此接口只能创建爆破合约");
        }

        if (wallet.getGameFtAmount().compareTo(new BigDecimal(1)) > 0) {
            return ResponseResult.failed("不能重复购买");
        }
        if (wallet == null || wallet.getTrxAmount().compareTo(contract.getSpec()) < 0) {
            return ResponseResult.failed("钱包余额不足");
        }
        wallet.setGameFtAmount(new BigDecimal(2));

        tyAdvert.setCreateDate(new Date());
        tyAdvert.setSpec(contract.getSpec());
        service.save(tyAdvert);
        wallet.setTrxAmount(wallet.getTrxAmount().subtract(contract.getSpec()));
        wallet.setContractNewLeve(contract.getLevel());
        wallet.setContractNewGameNum(contract.getDailyGameNum());
        wallet.setGameNum(wallet.getGameNum() + contract.getDailyGameNum());
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, +contract.getExpireDate());     //利用Calendar 实现 Date日期+1天
        wallet.setContractNewExpireDate(c.getTime());
        walletService.updateById(wallet);

        //统计每日数据
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String now = simpleDateFormat.format(new Date());
        SmsStats smsStats = new SmsStats();

        List<SmsStats> smsStatsList = iSmsStatsService.list(new QueryWrapper<SmsStats>().lambda()
                .eq(SmsStats::getCreateDate, now));
        if (smsStatsList.size() > 0) {
            smsStats = smsStatsList.get(0);
            smsStats.setContractCount(smsStats.getContractCount().add(contract.getSpec()));
        } else {
            smsStats.setCreateDate(new Date());
            smsStats.setContractCount(contract.getSpec());
        }
        iSmsStatsService.saveOrUpdate(smsStats);
        return ResponseResult.success(tyAdvert);
    }

    /**
     * 创建New后台
     *
     * @param uid
     * @return
     */
    @ApiOperation("创建New后台")
    @GetMapping("/createNewObjBack")
    public ResponseResult<UmsContractHistory> createNewObjBack(@ApiParam(required = true, value = "uid")
                                                                 @RequestParam(value = "uid") int uid) {

        //查询用户钱包余额
        WmsWallet wallet = walletService.getById(uid);

        if (wallet.getGameFtAmount().compareTo(new BigDecimal(1)) > 0) {
            return ResponseResult.failed("不能重复购买");
        }

        wallet.setGameFtAmount(new BigDecimal(2));
        UmsContractHistory tyAdvert = new UmsContractHistory();
        tyAdvert.setUid(uid);
        tyAdvert.setCid(8);
        tyAdvert.setCreateDate(new Date());
        service.save(tyAdvert);

        //查询合约
        UmsContract contract = contractService.getById(8);
//        wallet.setTrxAmount(wallet.getTrxAmount().subtract(contract.getSpec()));
        wallet.setContractNewLeve(contract.getLevel());
        wallet.setContractNewGameNum(contract.getDailyGameNum());
        wallet.setGameNum(wallet.getGameNum() + contract.getDailyGameNum());
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, +contract.getExpireDate());     //利用Calendar 实现 Date日期+1天
        wallet.setContractNewExpireDate(c.getTime());
        walletService.updateById(wallet);

        //统计每日数据
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String now = simpleDateFormat.format(new Date());
        SmsStats smsStats = new SmsStats();

        List<SmsStats> smsStatsList = iSmsStatsService.list(new QueryWrapper<SmsStats>().lambda()
                .eq(SmsStats::getCreateDate, now));
        if (smsStatsList.size() > 0) {
            smsStats = smsStatsList.get(0);
            smsStats.setContractCount(smsStats.getContractCount().add(contract.getSpec()));
        } else {
            smsStats.setCreateDate(new Date());
            smsStats.setContractCount(contract.getSpec());
        }
        iSmsStatsService.saveOrUpdate(smsStats);
        return ResponseResult.success(tyAdvert);
    }


    /**
     * 查询list
     *
     * @return
     */
    @ApiOperation("查询list")
    @GetMapping("/getList")
    public ResponseResult<List<UmsContractHistory>> getList(
            @ApiParam(required = true, value = "pageNum")
            @RequestParam(value = "pageNum") int pageNum,
            @ApiParam(required = true, value = "pageSize")
            @RequestParam(value = "pageSize") int pageSize,
            @ApiParam(required = true, value = "uid")
            @RequestParam(value = "uid") int uid) {

        List<UmsContractHistory> list = service.page(new Page<>(pageNum, pageSize),
                (new QueryWrapper<UmsContractHistory>().lambda()
                        .eq(UmsContractHistory::getUid, uid)
                        .orderByDesc(UmsContractHistory::getId))).getRecords();
        return ResponseResult.success(list);
    }

}
