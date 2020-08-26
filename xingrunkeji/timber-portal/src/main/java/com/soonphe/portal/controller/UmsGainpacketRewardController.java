package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soonphe.portal.entity.*;
import com.soonphe.portal.service.*;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 增益包奖励 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-06-07
 */
@RestController
@RequestMapping("/ums-gainpacket-reward")
@Api(tags = "【增益包社群分红】增益包社群分红")
public class UmsGainpacketRewardController {


    private static final Logger logger = LoggerFactory.getLogger(UmsGainpacketRewardController.class);

    @Autowired
    private IUmsGainpacketRewardService service;
    @Autowired
    private IWmsWalletService walletService;
    @Autowired
    private IUmsGainpacketRewardHistoryService gainpacketRewardHistoryService;
    @Autowired
    private IUmsCommunityProfitService umsCommunityProfitService;
    @Autowired
    private ISmsStatsService smsStatsService;

    /**
     * 发放增益包奖励
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("发放增益包奖励")
    @PostMapping("/giveReward")
    public ResponseResult<Boolean> giveReward(@ApiParam(required = true, value = "实体")
                                                @RequestBody UmsGainpacketReward tyAdvert) {
        tyAdvert.setCreateDate(new Date());
        boolean result = service.save(tyAdvert);
        List<WmsWallet> list;
        if (tyAdvert.getLevel() != null && tyAdvert.getLevel() > 0) {
            list = walletService.list(new QueryWrapper<WmsWallet>().lambda()
                    .eq(WmsWallet::getGainpackLevel, tyAdvert.getLevel()));
        } else {
            UmsCommunityProfit profit = umsCommunityProfitService.getById(tyAdvert.getTeamNum());
            UmsCommunityProfit profitMax = umsCommunityProfitService.getById(tyAdvert.getTeamNum() + 1);

            list = walletService.list(new QueryWrapper<WmsWallet>().lambda()
                    .ge(WmsWallet::getRecommendCount, profit.getDirectSpec())
                    .ge(WmsWallet::getTeamNum, profit.getTeamSpec())
                    .and(profitMax != null,
                            e -> e.lt(profitMax != null,WmsWallet::getRecommendCount, profitMax.getDirectSpec())
                                    .or(ee -> ee.lt(profitMax != null,WmsWallet::getTeamNum, profitMax.getTeamSpec())))

            );
        }
        if (list.size() > 0) {
            int gameTotal = 0;
            for (WmsWallet wallet : list) {
                gameTotal = gameTotal+  wallet.getGameTotal();
            }

            BigDecimal amount = tyAdvert.getProvideNum().divide(new BigDecimal(list.size()), 2, BigDecimal.ROUND_HALF_UP);


            for (WmsWallet wallet : list) {
                amount = tyAdvert.getProvideNum()
                        .divide(new BigDecimal(gameTotal),4,BigDecimal.ROUND_DOWN)
                        .multiply(new BigDecimal(wallet.getGameTotal()));
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String now = simpleDateFormat.format(new Date());
                SmsStats smsStats;
                List<SmsStats> smsStatsList = smsStatsService.list(new QueryWrapper<SmsStats>().lambda()
                        .eq(SmsStats::getCreateDate, now));
                if (smsStatsList.size() > 0) {
                    smsStats = smsStatsList.get(0);
                } else {
                    smsStats = new SmsStats();
                    smsStats.setCreateDate(new Date());
                    smsStats.setGainpacketCny(new BigDecimal("0"));
                    smsStats.setGainpacketFt(new BigDecimal("0"));
                    smsStats.setRecommendCny(new BigDecimal("0"));
                    smsStats.setRecommendFt(new BigDecimal("0"));
                    smsStats.setTeamCny(new BigDecimal("0"));
                    smsStats.setTeamFt(new BigDecimal("0"));
                }
                if (tyAdvert.getType() == 1) {
                    wallet.setCnyAmount(wallet.getCnyAmount().add(amount));
                    if (tyAdvert.getLevel() != null && tyAdvert.getLevel() > 0) {
                        smsStats.setGainpacketCny(smsStats.getGainpacketCny().add(amount));
                        wallet.setGainpacketCnyAmount(wallet.getGainpacketCnyAmount().add(amount));
                    } else {
                        smsStats.setTeamCny(smsStats.getTeamCny().add(amount));
                        wallet.setTeamCnyAmount(wallet.getTeamCnyAmount().add(amount));
                    }
                } else {
                    wallet.setFtAmount(wallet.getFtAmount().add(amount));
                    if (tyAdvert.getLevel() != null && tyAdvert.getLevel() > 0) {
                        smsStats.setGainpacketFt(smsStats.getGainpacketFt().add(amount));
                        wallet.setGainpacketFtAmount(wallet.getGainpacketFtAmount().add(amount));
                    } else {
                        smsStats.setTeamFt(smsStats.getTeamFt().add(amount));
                        wallet.setTeamFtAmount(wallet.getTeamFtAmount().add(amount));
                    }
                }
                smsStatsService.updateById(smsStats);
                UmsGainpacketRewardHistory history = new UmsGainpacketRewardHistory();
                if (tyAdvert.getLevel() != null && tyAdvert.getLevel() > 0) {
                    history.setShareType(1);
                } else {
                    history.setShareType(2);
                }
                history.setUid(wallet.getId());
                history.setAmount(amount);
                history.setCreateDate(new Date());
                history.setType(tyAdvert.getType());
                gainpacketRewardHistoryService.save(history);
            }
            walletService.updateBatchById(list);
        }
        return ResponseResult.success(result);
    }

    /**
     * 查询list
     *
     * @return
     */
    @ApiOperation("查询list")
    @GetMapping("/getList")
    public ResponseResult<List<UmsGainpacketReward>> getList(@ApiParam(required = true, value = "pageNum")
                                                               @RequestParam(value = "pageNum") int pageNum,
                                                               @ApiParam(required = true, value = "pageSize")
                                                               @RequestParam(value = "pageSize") int pageSize) {

        IPage<UmsGainpacketReward> page = service.page(new Page<>(pageNum, pageSize));
        return ResponseResult.success(page.getRecords(), "success", page.getTotal() + "");

    }
}
