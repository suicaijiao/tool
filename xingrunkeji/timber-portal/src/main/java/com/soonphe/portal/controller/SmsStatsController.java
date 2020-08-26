package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soonphe.portal.entity.SmsStats;
import com.soonphe.portal.entity.UmsCommunityProfit;
import com.soonphe.portal.entity.WmsWallet;
import com.soonphe.portal.entity.WmsWalletHistory;
import com.soonphe.portal.service.*;
import com.soonphe.portal.vo.StatsCurrencyVo;
import com.soonphe.portal.vo.StatsWalletDistributeVo;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 游戏数据统计模块 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-06-12
 */
@RestController
@RequestMapping("/sms-stats")
@Api(tags = "【统计】统计数据")
public class SmsStatsController {

    private static final Logger logger = LoggerFactory.getLogger(SmsBaseController.class);

    @Autowired
    private ISmsStatsService service;
    @Autowired
    private IWmsWalletService iWmsWalletService;
    @Autowired
    private IWmsWalletHistoryService iWmsWalletHistoryService;
    @Autowired
    private ISmsDestoryService destoryService;
    @Autowired
    private IUmsCommunityProfitService umsCommunityProfitService;

    /**
     * 分页获取每日统计记录
     *
     * @return
     */
    @ApiOperation("分页获取每日统计记录")
    @GetMapping("getListCount")
    public ResponseResult getListCount() {
        List<SmsStats> list = service.list();
        BigDecimal result = new BigDecimal(0);
        for (SmsStats smsStats:list){
            result = result.add(smsStats.getTicketCount());
        }
        return ResponseResult.success(result);
    }

    /**
     * 分页获取每日统计记录
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation("分页获取每日统计记录")
    @GetMapping("getPageList")
    public ResponseResult getPageList(@ApiParam(required = true, value = "pageNum")
                                                        @RequestParam(value = "pageNum") int pageNum,
                                                        @ApiParam(required = true, value = "pageSize")
                                                        @RequestParam(value = "pageSize") int pageSize) {
        IPage<SmsStats> page1 = service.page(new Page<>(pageNum, pageSize),
                new QueryWrapper<SmsStats>().lambda()
                        .orderByDesc(SmsStats::getId)
        );
        List<SmsStats> list = page1.getRecords();
        for (SmsStats smsStats:list){
            smsStats.setGameCny(BigDecimal.valueOf((smsStats.getUserGameCount() - smsStats.getUserWinCount()) * 9.75).setScale(4, BigDecimal.ROUND_DOWN));
            smsStats.setGameFt(smsStats.getMinerFtCount().multiply(new BigDecimal(39)).setScale(4, BigDecimal.ROUND_DOWN));
            smsStats.setDeposition(BigDecimal.valueOf(smsStats.getUserWinCount()*100).subtract(smsStats.getGameCny()).setScale(4, BigDecimal.ROUND_DOWN));
            smsStats.setDepositionValid(smsStats.getDeposition().subtract(smsStats.getRecommendCny()).subtract(smsStats.getGainpacketCny()).subtract(smsStats.getTeamCny()).setScale(4, BigDecimal.ROUND_DOWN));
            BigDecimal rate=new BigDecimal(0);
            if (smsStats.getUserGameCount()>0){
                rate = new BigDecimal(smsStats.getAndroidGameCount()).divide(new BigDecimal(smsStats.getUserGameCount()),4, BigDecimal.ROUND_DOWN);
            }
            smsStats.setAndroidCny(rate.multiply(smsStats.getMinerFtCount()).setScale(4, BigDecimal.ROUND_DOWN));
            smsStats.setAndroidFt(rate.multiply(smsStats.getMinerCnyCount()).setScale(4, BigDecimal.ROUND_DOWN));
            smsStats.setAndroidTicket(rate.multiply(smsStats.getTicketCount()).setScale(4, BigDecimal.ROUND_DOWN));
        }
        return ResponseResult.success(list, "", page1.getTotal() + "");
    }

    /**
     * 首页：统计用户社群分布，增益包等级分布
     *
     * @param sid
     * @return
     */
    @ApiOperation("统计用户分布")
    @GetMapping("/getUserDistributeStats")
    public ResponseResult getUserDistributeStats(@ApiParam(required = true, value = "sid")
                                                                            @RequestParam(value = "sid") int sid) {
        if (sid < 0) {
            return ResponseResult.failed("没有权限");
        }
        StatsWalletDistributeVo vo = new StatsWalletDistributeVo();
        vo.setDestoryTotal(destoryService.getTotal());

        vo.setWalletTotal(iWmsWalletService.count());
        vo.setWalletValid(iWmsWalletService.count(new QueryWrapper<WmsWallet>().lambda()
                .ge(WmsWallet::getGameTotal, 10)));

        vo.setWalletZero(iWmsWalletService.count(new QueryWrapper<WmsWallet>().lambda()
                .lt(WmsWallet::getRecommendCount, 10)
                .or(e -> e.lt(WmsWallet::getTeamNum, 100))));
        vo.setWalletFour(iWmsWalletService.count(new QueryWrapper<WmsWallet>().lambda()
                .ge(WmsWallet::getRecommendValidCount, 40)
                .ge(WmsWallet::getTeamValidNum, 3000)));
        vo.setWalletThree(iWmsWalletService.count(new QueryWrapper<WmsWallet>().lambda()
                .ge(WmsWallet::getRecommendValidCount, 30)
                .ge(WmsWallet::getTeamValidNum, 1000)) - vo.getWalletFour());
        vo.setWalletTwo(iWmsWalletService.count(new QueryWrapper<WmsWallet>().lambda()
                .ge(WmsWallet::getRecommendValidCount, 20)
                .ge(WmsWallet::getTeamValidNum, 500)) - vo.getWalletFour() - vo.getWalletThree());
        vo.setWalletOne(iWmsWalletService.count(new QueryWrapper<WmsWallet>().lambda()
                .ge(WmsWallet::getRecommendValidCount, 10)
                .ge(WmsWallet::getTeamValidNum, 100)) - vo.getWalletFour() - vo.getWalletThree() - vo.getWalletTwo());
        vo.setLevelOne(iWmsWalletService.count(new QueryWrapper<WmsWallet>().lambda()
                .eq(WmsWallet::getGainpackLevel, 1)));
        vo.setLevelTwo(iWmsWalletService.count(new QueryWrapper<WmsWallet>().lambda()
                .eq(WmsWallet::getGainpackLevel, 2)));
        vo.setLevelThree(iWmsWalletService.count(new QueryWrapper<WmsWallet>().lambda()
                .eq(WmsWallet::getGainpackLevel, 3)));

        return ResponseResult.success(vo);
    }

    /**
     * 会员管理
     *
     * @param pageNum
     * @param pageSize
     * @param sid
     * @return
     */
    @ApiOperation("会员管理List")
    @GetMapping("getWalletList")
    public ResponseResult getWalletList(@ApiParam(required = true, value = "pageNum")
                                                  @RequestParam(value = "pageNum") int pageNum,
                                                  @ApiParam(required = true, value = "pageSize")
                                                  @RequestParam(value = "pageSize") int pageSize,
                                                  @ApiParam(required = true, value = "sid")
                                                  @RequestParam(value = "sid") int sid,
                                                  @ApiParam(required = false, value = "uid")
                                                  @RequestParam(value = "uid", required = false) String uid,
                                                  @ApiParam(required = true, value = "teamNum")
                                                  @RequestParam(value = "teamNum", required = false) int teamNum,
                                                  @ApiParam(required = true, value = "level")
                                                  @RequestParam(value = "level", required = false) int level) {
        if (sid < 0) {
            return ResponseResult.failed("没有权限");
        }
        LambdaQueryWrapper<WmsWallet> wrapper = new QueryWrapper<WmsWallet>().lambda()
                .like(!"".equals(uid), WmsWallet::getId, uid);

        if (teamNum>0){
            UmsCommunityProfit profit = umsCommunityProfitService.getById(teamNum);
            UmsCommunityProfit profitMax = umsCommunityProfitService.getById(teamNum + 1);

            wrapper.ge(WmsWallet::getRecommendCount, profit.getDirectSpec())
                    .ge( WmsWallet::getTeamNum, profit.getTeamSpec())
                    .and(profitMax != null, e -> e.lt(profitMax != null, WmsWallet::getRecommendValidCount, profitMax.getDirectSpec())
                            .or(ee -> ee.lt(profitMax != null, WmsWallet::getTeamValidNum, profitMax.getTeamSpec())));
        }


        IPage<WmsWallet> page = iWmsWalletService.page(new Page<>(pageNum, pageSize),
                wrapper.eq(level > 0, WmsWallet::getGainpackLevel, level)
                        .orderByDesc(WmsWallet::getId)
        );


        List<WmsWallet> list = new ArrayList<>();
        if ( page.getRecords().size()>0){
             for (WmsWallet wallet:page.getRecords()){
                 wallet.setPrivateKey("******");
                 wallet.setWordKey("******");
                 list.add(wallet);
             }
        }
        return ResponseResult.success(list, "", String.valueOf(page.getTotal()));
    }

    /**
     * USDT CNY FT数量统计
     *
     * @param sid
     * @return
     */
    @ApiOperation("USDT CNY FT数量统计")
    @GetMapping("/currencyStats")
    public ResponseResult currencyStats(@ApiParam(required = true, value = "sid")
                                                           @RequestParam(value = "sid") int sid) {
        if (sid < 0) {
            return ResponseResult.failed("没有权限");
        }
        List<WmsWalletHistory> list = iWmsWalletHistoryService.list(new QueryWrapper<WmsWalletHistory>().lambda()
                .eq(WmsWalletHistory::getType, 1)
                .eq(WmsWalletHistory::getState, 1)
        );
        BigDecimal rechargeAmount = new BigDecimal("0");
        if (list.size() > 0) {
            for (WmsWalletHistory history : list) {
                rechargeAmount = rechargeAmount.add(history.getAmount());
            }
        }
        StatsCurrencyVo vo = iWmsWalletService.getTotalCurrencyAmount();
        vo.setTrxRechargeAmount(rechargeAmount);
        vo.setTrxWithdrawalAmount(rechargeAmount.subtract(vo.getTrxAmount()));
        return ResponseResult.success(vo);
    }


}
