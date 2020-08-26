package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 圆梦计划历史表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-08-01
 */
@RestController
@RequestMapping("/ums-dream-history")
@Api(tags = "【圆梦计划记录】圆梦计划记录")
public class UmsDreamHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(UmsContractController.class);

    @Autowired
    private IUmsDreamHistoryService service;
    @Autowired
    private IUmsDreamService dreamService;
    @Autowired
    private IWmsWalletService walletService;
    @Autowired
    private ISmsStatsService iSmsStatsService;

    /**
     * 创建
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("创建")
    @PostMapping("/createObj")
    public ResponseResult<UmsDreamHistory> createObj(@ApiParam(required = true, value = "实体")
                                                          @RequestBody UmsDreamHistory tyAdvert) {

        //查询用户钱包余额
        WmsWallet wallet = walletService.getById(tyAdvert.getUid());
        //查询合约
        UmsDream dream = dreamService.getById(tyAdvert.getDid());
        if (dream.getState() == 2) {
            return ResponseResult.failed("计划不可用");
        }
        if (wallet == null || wallet.getTrxAmount().compareTo(dream.getSpec()) < 0) {
            return ResponseResult.failed("钱包余额不足");
        }

        List<UmsDreamHistory> list = service.list(new QueryWrapper<UmsDreamHistory>().lambda()
                .eq(UmsDreamHistory::getUid, tyAdvert.getUid())
                .eq(UmsDreamHistory::getState, 1));
        if (list.size() > 0) {
            return ResponseResult.failed("计划不可重复购买");
        }

        tyAdvert.setCreateDate(new Date());
        tyAdvert.setName(dream.getName());
        tyAdvert.setSpec(dream.getSpec());
        tyAdvert.setSpecProfit(dream.getSpecProfit());
        tyAdvert.setPlanDate(dream.getPlanDate());
        tyAdvert.setName(dream.getName());
        tyAdvert.setName(dream.getName());

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, +dream.getPlanDate());     //利用Calendar 实现 Date日期+1天
        tyAdvert.setExpireDate(c.getTime());
        service.save(tyAdvert);


        wallet.setTrxAmount(wallet.getFtAmount().subtract(dream.getSpec()));
        walletService.updateById(wallet);

        //统计每日数据
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String now = simpleDateFormat.format(new Date());
//        SmsStats smsStats = new SmsStats();
//
//        List<SmsStats> smsStatsList = iSmsStatsService.list(new QueryWrapper<SmsStats>().lambda()
//                .eq(SmsStats::getCreateDate, now));
//        if (smsStatsList.size() > 0) {
//            smsStats = smsStatsList.get(0);
//            smsStats.setContractCount(smsStats.getGainpacketCount().add(contract.getSpec()));
//        } else {
//            smsStats.setCreateDate(new Date());
//            smsStats.setContractCount(contract.getSpec());
//        }
//        iSmsStatsService.saveOrUpdate(smsStats);
        return ResponseResult.success(tyAdvert);
    }

    /**
     * 查询list
     *
     * @return
     */
    @ApiOperation("查询list")
    @GetMapping("/getList")
    public ResponseResult<List<UmsDreamHistory>> getList(@ApiParam(required = true, value = "pageNum")
                                                           @RequestParam(value = "pageNum") int pageNum,
                                                           @ApiParam(required = true, value = "pageSize")
                                                           @RequestParam(value = "pageSize") int pageSize,
                                                           @ApiParam(required = true, value = "uid")
                                                           @RequestParam(value = "uid") int uid,
                                                           @ApiParam(required = true, value = "state")
                                                           @RequestParam(value = "state") int state) {

        List<UmsDreamHistory> list = service.page(new Page<>(pageNum, pageSize),
                (new QueryWrapper<UmsDreamHistory>().lambda()
                        .eq(uid>0,UmsDreamHistory::getUid, uid)
                        .eq(state > 0, UmsDreamHistory::getState, state)
                        .orderByDesc(UmsDreamHistory::getId))).getRecords();
        return ResponseResult.success(list);
    }

}
