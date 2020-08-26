package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.*;
import com.soonphe.portal.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 增益表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@RestController
@RequestMapping("/ums-gainpacket-history")
@Api(tags = "【增益包记录】增益包记录")
public class UmsGainpacketHistoryController {


    private static final Logger logger = LoggerFactory.getLogger(UmsGainpacketHistoryController.class);

    @Autowired
    private IUmsGainpacketHistoryService service;
    @Autowired
    private IUmsGainpacketService gainpacketService;
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
    public ResponseResult<UmsGainpacketHistory> createObj(@ApiParam(required = true, value = "实体")
                                                          @RequestBody UmsGainpacketHistory tyAdvert) {

        //查询用户钱包余额
        WmsWallet wallet = walletService.getById(tyAdvert.getUid());
        //查询增益包
        UmsGainpacket contract = gainpacketService.getById(tyAdvert.getGid());
        if (contract.getState()==2) {
            return ResponseResult.failed("增益包不可用");
        }
        if (wallet.getFtAmount().compareTo(contract.getSpec())<0){
            return ResponseResult.failed("钱包余额不足");
        }
        if (wallet.getGainpackLevel() != null && wallet.getGainpackLevel() > 0) {
            return ResponseResult.failed("增益包不可重复购买");
        }
        tyAdvert.setCreateDate(new Date());
        service.save(tyAdvert);
        wallet.setFtAmount(wallet.getFtAmount().subtract(contract.getSpec()));
        wallet.setGainpackLevel(contract.getLevel());
        wallet.setGainpackGameNum(contract.getDailyGameNum());
        wallet.setGameNum(wallet.getGameNum()+contract.getDailyGameNum());
        walletService.updateById(wallet);

        //统计每日数据
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String now = simpleDateFormat.format(new Date());
        SmsStats smsStats = new SmsStats();

        List<SmsStats> smsStatsList = iSmsStatsService.list(new QueryWrapper<SmsStats>().lambda()
                .eq(SmsStats::getCreateDate, now));
        if (smsStatsList.size() > 0) {
            smsStats = smsStatsList.get(0);
            smsStats.setGainpacketCount(smsStats.getGainpacketCount().add(contract.getSpec()));
        } else {
            smsStats.setCreateDate(new Date());
            smsStats.setGainpacketCount(contract.getSpec());
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
    public ResponseResult<List<UmsGainpacketHistory>> getList(@ApiParam(required = true, value = "uid")
                                                              @RequestParam(value = "uid") int uid) {

        List<UmsGainpacketHistory> list = service.list(new QueryWrapper<UmsGainpacketHistory>().lambda()
                .eq(UmsGainpacketHistory::getUid, uid));
        return ResponseResult.success(list);
    }
}
