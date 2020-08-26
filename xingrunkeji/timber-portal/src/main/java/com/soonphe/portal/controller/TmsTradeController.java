package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soonphe.portal.entity.*;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.service.*;
import com.soonphe.portal.vo.TradeHistoryPageVo;
import com.soonphe.portal.vo.TradeVo;
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
 * 交易表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-05-27
 */
@RestController
@RequestMapping("/tms-trade")
@Api(tags = "【交易】交易")
public class TmsTradeController {

    private static final Logger logger = LoggerFactory.getLogger(WmsWalletController.class);

    @Autowired
    private ITmsTradeService service;
    @Autowired
    private IWmsWalletService walletService;
    @Autowired
    private ITmsTradeHistoryService historyService;
    @Autowired
    private IOmsOrePoolService orePoolService;
    @Autowired
    private ISmsStatsService smsStatsService;
    @Autowired
    private ISmsBaseService smsBaseService;

    /**
     * 叠加获取list
     *
     * @param type
     * @param state
     * @return
     */
    @ApiOperation("叠加获取list")
    @GetMapping("getTradeList")
    public ResponseResult<List<TradeVo>> getTradeList(@ApiParam(required = true, value = "type")
                                                        @RequestParam(value = "type") int type,
                                                        @ApiParam(required = true, value = "state")
                                                        @RequestParam(value = "state") int state) {
        List<TradeVo> list = service.getTradeList(state, type);
        return ResponseResult.success(list);
    }

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @param type
     * @return
     */
    @ApiOperation("分页获取List")
    @GetMapping("getList")
    public ResponseResult<List<TmsTrade>> getList(@ApiParam(required = true, value = "pageNum")
                                                    @RequestParam(value = "pageNum") int pageNum,
                                                    @ApiParam(required = true, value = "pageSize")
                                                    @RequestParam(value = "pageSize") int pageSize,
                                                    @ApiParam(required = true, value = "uid")
                                                    @RequestParam(value = "uid") int uid,
                                                    @ApiParam(required = true, value = "type")
                                                    @RequestParam(value = "type") int type,
                                                    @ApiParam(required = true, value = "state")
                                                    @RequestParam(value = "state") int state) {
        IPage<TmsTrade> page1 = service.page(new Page<>(pageNum, pageSize),
                new QueryWrapper<TmsTrade>().lambda()
                        .eq(uid > 0 ? true : false, TmsTrade::getUid, uid)
                        .eq(type > 0 ? true : false, TmsTrade::getType, type)
                        .eq(state > 0 ? true : false, TmsTrade::getState, state)
                        .orderByDesc(TmsTrade::getId));
        return ResponseResult.success(page1.getRecords(), "", page1.getTotal() + "");
    }

    /**
     * 撤销订单
     *
     * @param tid
     * @return
     */
    @ApiOperation("撤销订单")
    @GetMapping("/cancleOrdel")
    public ResponseResult<TmsTrade> cancleOrdel(@ApiParam(required = true, value = "tid")
                                                  @RequestParam(value = "tid") int tid) {
        TmsTrade tmsTrade = service.getById(tid);
        if (tmsTrade.getState() == 2) {
            return ResponseResult.failed("撤销失败，订单已完成");
        }
        if (tmsTrade.getState() == 3) {
            return ResponseResult.failed("撤销失败，请不要重复撤销");
        }
        tmsTrade.setState(3);
        service.updateById(tmsTrade);
        if (tmsTrade.getAmount().compareTo(tmsTrade.getDealAmount()) > 0) {
            WmsWallet wallet = walletService.getById(tmsTrade.getUid());
            BigDecimal leave = tmsTrade.getAmount().subtract(tmsTrade.getDealAmount());
            if (tmsTrade.getType() == 1) {
                wallet.setCnyAmount(wallet.getCnyAmount().add(leave.multiply(tmsTrade.getPrice())));
            } else if (tmsTrade.getType() == 2) {
                wallet.setFtAmount(wallet.getFtAmount().add(leave));
            }
            walletService.updateById(wallet);
        }
        return ResponseResult.success(tmsTrade);
    }

    /**
     * 分页获取交易历史记录List
     *
     * @return
     */
    @ApiOperation("分页获取交易历史记录List")
    @PostMapping("getHistoryList")
    public ResponseResult<List<TmsTradeHistory>> getHistoryList(@ApiParam(required = true, value = "实体")
                                                                  @RequestBody TradeHistoryPageVo vo) {
        IPage<TmsTradeHistory> page1 = historyService.page(new Page<>(vo.getPageNum(), vo.getPageSize()),
                new QueryWrapper<TmsTradeHistory>().lambda()
                        .ge(vo.getStartDate() != null ? true : false, TmsTradeHistory::getCreateTime, vo.getStartDate())
                        .le(vo.getEndDate() != null ? true : false, TmsTradeHistory::getCreateTime, vo.getEndDate())
                        .orderByDesc(vo.getId() > 0, TmsTradeHistory::getId));
//                        .orderByDesc(vo.getPrice() > 0, TmsTradeHistory::getPrice));
        return ResponseResult.success(page1.getRecords());
    }

    /**
     * 买入/卖出
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("买入/卖出")
    @PostMapping("/purchaseAndSell")
    public ResponseResult<TmsTrade> purchaseAndSell(@ApiParam(required = true, value = "实体")
                                                      @RequestBody TmsTrade tyAdvert) {
        tyAdvert.setDealAmount(new BigDecimal(0));
        WmsWallet wallet = walletService.getById(tyAdvert.getUid());
        //判断价格不能低于发行价90%
//        BigDecimal rate = orePoolService.getFtRate();
        BigDecimal rate = new BigDecimal(1);
        List<SmsBase> bases= smsBaseService.list();
        if(bases.size()>0){
             rate = bases.get(0).getPricePublic();
        }

        if (tyAdvert.getPrice().compareTo(rate.multiply(new BigDecimal(0.5))) < 0) {
            return ResponseResult.failed("挂单价格不能低于发行价的50%");
        }
        if (tyAdvert.getType() == 1) {
            if (wallet.getCnyAmount().compareTo(tyAdvert.getAmount().multiply(tyAdvert.getPrice())) < 0) {
                return ResponseResult.failed("钱包CNY余额不足");
            }
            wallet.setCnyAmount(wallet.getCnyAmount().subtract(tyAdvert.getPrice().multiply(tyAdvert.getAmount())));
        } else if (tyAdvert.getType() == 2) {
            if (wallet.getFtAmount().compareTo(tyAdvert.getAmount()) < 0) {
                return ResponseResult.failed("钱包FT余额不足");
            }
            wallet.setFtAmount(wallet.getFtAmount().subtract(tyAdvert.getAmount()));
        }
        walletService.updateById(wallet);
        service.save(tyAdvert);

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
            smsStats.setTradeBuy(new BigDecimal("0"));
            smsStats.setTradeSell(new BigDecimal("0"));
        }
        if (tyAdvert.getType() == 1) {
            List<TmsTrade> list = service.list(new QueryWrapper<TmsTrade>().lambda()
                    .eq(TmsTrade::getState, 1)
                    .eq(TmsTrade::getType, 2)
                    .ne(TmsTrade::getUid, tyAdvert.getUid())
                    .orderByAsc(TmsTrade::getPrice));
            for (TmsTrade tmsTrade : list) {
                if (tmsTrade.getPrice().compareTo(tyAdvert.getPrice()) == 0) {
                    BigDecimal amount = tmsTrade.getAmount().subtract(tmsTrade.getDealAmount());
                    BigDecimal tyAmount = tyAdvert.getAmount().subtract(tyAdvert.getDealAmount());
                    if (amount.compareTo(tyAmount) < 0) {
                        smsStats.setTradeBuy(smsStats.getTradeBuy().add(amount));
                        smsStats.setTradeSell(smsStats.getTradeSell().add(amount));

                        historyService.save(new TmsTradeHistory(tyAdvert.getId(), tmsTrade.getId(), tyAdvert.getPrice(), amount));

                        tmsTrade.setState(2);
                        tmsTrade.setDealPrice(tyAdvert.getPrice());
                        tmsTrade.setDealAmount(tmsTrade.getAmount());
                        service.updateById(tmsTrade);
                        tyAdvert.setDealPrice(tyAdvert.getPrice());
                        tyAdvert.setDealAmount(tyAdvert.getDealAmount().add(amount));
                        service.updateById(tyAdvert);
                        updateWallet(tmsTrade.getUid(), tyAdvert.getUid(), amount, tyAdvert.getPrice());
                        continue;
                    } else if (amount.compareTo(tyAmount) == 0) {
                        smsStats.setTradeBuy(smsStats.getTradeBuy().add(amount));
                        smsStats.setTradeSell(smsStats.getTradeSell().add(amount));
                        historyService.save(new TmsTradeHistory(tyAdvert.getId(), tmsTrade.getId(), tyAdvert.getPrice(), amount));
                        tmsTrade.setState(2);
                        tmsTrade.setDealPrice(tmsTrade.getPrice());
                        tmsTrade.setDealAmount(tmsTrade.getAmount());
                        service.updateById(tmsTrade);
                        tyAdvert.setState(2);
                        tyAdvert.setDealPrice(tyAdvert.getPrice());
                        tyAdvert.setDealAmount(tyAdvert.getAmount());
                        service.updateById(tyAdvert);
                        updateWallet(tmsTrade.getUid(), tyAdvert.getUid(), amount, tyAdvert.getPrice());
                        break;
                    } else {
                        smsStats.setTradeBuy(smsStats.getTradeBuy().add(tyAmount));
                        smsStats.setTradeSell(smsStats.getTradeSell().add(tyAmount));
                        historyService.save(new TmsTradeHistory(tyAdvert.getId(), tmsTrade.getId(), tyAdvert.getPrice(), tyAmount));
                        tmsTrade.setDealPrice(tyAdvert.getPrice());
                        tmsTrade.setDealAmount(tmsTrade.getDealAmount().add(tyAmount));
                        service.updateById(tmsTrade);
                        tyAdvert.setState(2);
                        tyAdvert.setDealPrice(tyAdvert.getPrice());
                        tyAdvert.setDealAmount(tyAdvert.getAmount());
                        service.updateById(tyAdvert);
                        updateWallet(tmsTrade.getUid(), tyAdvert.getUid(), tyAmount, tyAdvert.getPrice());
                        break;
                    }
                }
            }

        } else {
            List<TmsTrade> list = service.list(new QueryWrapper<TmsTrade>().lambda()
                    .eq(TmsTrade::getState, 1)
                    .eq(TmsTrade::getType, 1)
                    .ne(TmsTrade::getUid, tyAdvert.getUid())
                    .orderByAsc(TmsTrade::getPrice));

            for (TmsTrade tmsTrade : list) {
                if (tmsTrade.getPrice().compareTo(tyAdvert.getPrice()) == 0) {
                    //判断目标钱包与当前钱包交易余量
                    BigDecimal amount = tmsTrade.getAmount().subtract(tmsTrade.getDealAmount());
                    BigDecimal tyAmount = tyAdvert.getAmount().subtract(tyAdvert.getDealAmount());
                    if (amount.compareTo(tyAmount) < 0) {
                        smsStats.setTradeBuy(smsStats.getTradeBuy().add(amount));
                        smsStats.setTradeSell(smsStats.getTradeSell().add(amount));
                        tmsTrade.setState(2);
                        tmsTrade.setDealPrice(tyAdvert.getPrice());
                        tmsTrade.setDealAmount(tmsTrade.getAmount());
                        service.updateById(tmsTrade);

                        tyAdvert.setDealPrice(tyAdvert.getPrice());
                        tyAdvert.setDealAmount(tyAdvert.getDealAmount().add(amount));
                        service.updateById(tyAdvert);
                        //更新sell和buy钱包
                        updateWallet(tyAdvert.getUid(), tmsTrade.getUid(), amount, tmsTrade.getPrice());
                        //交易记录
                        historyService.save(new TmsTradeHistory(tmsTrade.getId(), tyAdvert.getId(), tyAdvert.getPrice(), tmsTrade.getAmount()));
                        continue;
                    } else if (amount.compareTo(tyAmount) == 0) {
                        //更新每日交易统计
                        smsStats.setTradeBuy(smsStats.getTradeBuy().add(amount));
                        smsStats.setTradeSell(smsStats.getTradeSell().add(amount));
                        //数量等于要出售的数量
                        tmsTrade.setState(2);
                        tmsTrade.setDealPrice(tyAdvert.getPrice());
                        tmsTrade.setDealAmount(tmsTrade.getAmount());
                        service.updateById(tmsTrade);

                        tyAdvert.setState(2);
                        tyAdvert.setDealPrice(tyAdvert.getPrice());
                        tyAdvert.setDealAmount(tyAdvert.getAmount());
                        service.updateById(tyAdvert);
                        //更新sell和buy钱包
                        updateWallet(tyAdvert.getUid(), tmsTrade.getUid(), amount, tmsTrade.getPrice());
                        //交易记录
                        historyService.save(new TmsTradeHistory(tmsTrade.getId(), tyAdvert.getId(), tyAdvert.getPrice(), tmsTrade.getAmount()));
                        break;
                    } else {
                        //更新每日交易统计
                        smsStats.setTradeBuy(smsStats.getTradeBuy().add(tyAmount));
                        smsStats.setTradeSell(smsStats.getTradeSell().add(tyAmount));
                        //数量大于要出售的数量
                        tmsTrade.setDealPrice(tyAdvert.getPrice());
                        tmsTrade.setDealAmount(tmsTrade.getDealAmount().add(tyAmount));
                        service.updateById(tmsTrade);

                        tyAdvert.setState(2);
                        tyAdvert.setDealPrice(tyAdvert.getPrice());
                        tyAdvert.setDealAmount(tyAdvert.getAmount());
                        service.updateById(tyAdvert);
                        //更新sell和buy钱包
                        updateWallet(tyAdvert.getUid(), tmsTrade.getUid(), tyAmount, tmsTrade.getPrice());
                        //交易记录
                        historyService.save(new TmsTradeHistory(tmsTrade.getId(), tyAdvert.getId(), tyAdvert.getPrice(), tyAmount));
                        break;
                    }
                }
            }
        }
        smsStatsService.saveOrUpdate(smsStats);
        return ResponseResult.success(tyAdvert);
    }


    /**
     * 更新sell和buy钱包
     *
     * @param sellId
     * @param buyId
     * @param amount
     * @param price
     */
    public void updateWallet(int sellId, int buyId, BigDecimal amount, BigDecimal price) {


        WmsWallet sellWallet = walletService.getById(sellId);
        sellWallet.setCnyAmount(sellWallet.getCnyAmount().add(amount.multiply(price)));
        walletService.updateById(sellWallet);
        WmsWallet buyWallet = walletService.getById(buyId);
        buyWallet.setFtAmount(buyWallet.getFtAmount().add(amount));
        walletService.updateById(buyWallet);
    }

}
