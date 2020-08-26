//package com.soonphe.portal.config;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.soonphe.portal.entity.*;
//import com.soonphe.portal.service.*;
//import com.soonphe.portal.util.rate.RateApiUtil;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
///**
// * @author: soonphe
// * @date: 2020-06-23
// * @description: 钱包每日零点释放
// */
//@Component
//public class WalletReleaseTask {
//    private Logger LOGGER = LoggerFactory.getLogger(WalletReleaseTask.class);
//
//    @Autowired
//    private IWmsWalletService service;
//    @Autowired
//    private IWmsWalletReleaseService releaseService;
//    @Autowired
//    private ISmsBaseService smsBaseService;
//    @Autowired
//    private ISmsStatsService smsStatsService;
//    @Autowired
//    private IUmsDreamHistoryService umsDreamHistoryService;
//    @Autowired
//    private IUmsDreamReleaseService umsDreamReleaseService;
//    @Autowired
//    private IOmsOrePoolService omsOrePoolService;
//
//
//    @Scheduled(cron = "0 0 0 * * ?")
//    private void walletRelease() {
//
//        RateApiUtil.RATE_UPDATE(omsOrePoolService.getFtRate().doubleValue());
//
//        List<WmsWallet> list = service.list();
//        if (list.size() > 0) {
//            BigDecimal releaseTotal = new BigDecimal("0");
//            for (WmsWallet wallet : list) {
//                if (wallet.getWinFt().compareTo(new BigDecimal("0.00001")) < 0) {
//                    continue;
//                }
//                WmsWalletRelease walletRelease = new WmsWalletRelease();
//                BigDecimal releaseNum = new BigDecimal("0");
//                if (wallet.getWinFt().compareTo(new BigDecimal("20")) > 0) {
//                    SmsBase base = smsBaseService.list().get(0);
//                    releaseNum = wallet.getWinFt().multiply(wallet.getReleaseRatio().add(base.getWinSpeed()));
//                } else if (wallet.getWinFt().compareTo(new BigDecimal("0.00001")) > 0
//                        && wallet.getWinFt().compareTo(new BigDecimal("20")) <= 0) {
//                    releaseNum = wallet.getWinFt();
//                }
//                walletRelease.setUid(wallet.getId());
//                walletRelease.setReleaseNum(releaseNum);
//                walletRelease.setCreateDate(new Date());
//                releaseService.save(walletRelease);
//                releaseTotal = releaseTotal.add(releaseNum);
//                wallet.setFtAmount(wallet.getFtAmount().add(releaseNum));
//                wallet.setWinFt(wallet.getWinFt().subtract(releaseNum));
//                wallet.setReleaseTotal(wallet.getReleaseTotal().add(releaseNum));
//                service.updateById(wallet);
//            }
//
//
//            //统计矿池释放记录
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String now = simpleDateFormat.format(new Date());
//            SmsStats smsStats;
//            List<SmsStats> smsStatsList = smsStatsService.list(new QueryWrapper<SmsStats>().lambda()
//                    .eq(SmsStats::getCreateDate, now));
//            if (smsStatsList.size() > 0) {
//                smsStats = smsStatsList.get(0);
//                smsStats.setOreCount(smsStats.getOreCount().add(releaseTotal));
//            } else {
//                smsStats = new SmsStats();
//                smsStats.setCreateDate(new Date());
//                smsStats.setOreCount(releaseTotal);
//            }
//            smsStatsService.saveOrUpdate(smsStats);
//
//            List<UmsDreamHistory> listDream = umsDreamHistoryService.list(new QueryWrapper<UmsDreamHistory>().lambda()
//                    .eq(UmsDreamHistory::getState, 1));
//            if (listDream.size() > 0) {
//                for (UmsDreamHistory dreamHistory : listDream) {
//
//                    if (dreamHistory.getExpireDate().before(new Date())) {
//                        dreamHistory.setState(2);
//                        continue;
//                    }
//                    dreamHistory.setHaveDate(dreamHistory.getHaveDate() + 1);
//                    BigDecimal profit = dreamHistory.getSpecProfit().divide(new BigDecimal(dreamHistory.getPlanDate()), 2);
//                    dreamHistory.setTotalProfit(dreamHistory.getTotalProfit().add(profit));
//
//                    UmsDreamRelease walletRelease = new UmsDreamRelease();
//                    walletRelease.setUid(dreamHistory.getUid());
//                    walletRelease.setReleaseNum(profit);
//                    walletRelease.setCreateDate(new Date());
//                    umsDreamReleaseService.save(walletRelease);
//
//                    WmsWallet wallet = service.getById(dreamHistory.getUid());
//                    wallet.setTrxAmount(wallet.getTrxAmount().add(profit));
//                    service.updateById(wallet);
//                }
//            }
//        }
//
//    }
//}