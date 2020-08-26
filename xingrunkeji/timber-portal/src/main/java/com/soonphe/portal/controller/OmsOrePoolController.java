package com.soonphe.portal.controller;


import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.OmsOrePool;
import com.soonphe.portal.entity.OmsOrePoolHistory;
import com.soonphe.portal.service.IOmsOrePoolHistoryService;
import com.soonphe.portal.service.IOmsOrePoolService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 矿池表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-05-27
 */
@RestController
@RequestMapping("/oms-ore-pool")
@Api(tags = "【矿池】矿池")
public class OmsOrePoolController {

    private static final Logger logger = LoggerFactory.getLogger(OmsOrePoolController.class);

    @Autowired
    private IOmsOrePoolService service;

    @Autowired
    private IOmsOrePoolHistoryService historyService;

    /**
     * 获取发行价
     *
     * @return
     */
    @ApiOperation("获取发行价")
    @GetMapping("/getFtRate")
    public ResponseResult getFtRate() {
        return ResponseResult.success(service.getFtRate());
    }

    /**
     * 获取发行价
     *
     * @return
     */
    @ApiOperation("获取昨日发行价")
    @GetMapping("/getYesterdayFtRate")
    public ResponseResult getYesterdayFtRate() {
        return ResponseResult.success(service.getYesterdayFtRate());
    }

    /**
     * 查询矿池信息
     *
     * @return
     */
    @ApiOperation("查询矿池信息")
    @GetMapping("/getOrePool")
    public ResponseResult getOrePool() {

        List<OmsOrePool> list = service.list();
        if (list.size() > 0) {
            return ResponseResult.success(list.get(0));
        }
        return ResponseResult.failed();
    }

    /**
     * 查询矿池释放记录
     *
     * @return
     */
    @ApiOperation("查询矿池释放记录")
    @GetMapping("/getOrePoolHistory")
    public ResponseResult getOrePoolHistory() {

        List<OmsOrePoolHistory> list = historyService.list();
        return ResponseResult.success(list);

    }


}
