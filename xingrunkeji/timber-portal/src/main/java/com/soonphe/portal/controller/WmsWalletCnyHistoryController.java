package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.WmsWalletCnyHistory;
import com.soonphe.portal.service.IWmsWalletCnyHistoryService;
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

import java.util.List;

/**
 * <p>
 * 游戏充提记录 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-06-11
 */
@RestController
@RequestMapping("/wms-wallet-cny-history")
@Api(tags = "【cny充提记录】cny充提记录")
public class WmsWalletCnyHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(UmsGainpacketHistoryController.class);

    @Autowired
    private IWmsWalletCnyHistoryService service;

    /**
     * 查询list
     *
     * @return
     */
    @ApiOperation("查询list")
    @GetMapping("/getList")
    public ResponseResult<List<WmsWalletCnyHistory>> getList(
            @ApiParam(required = true, value = "pageNum")
            @RequestParam(value = "pageNum") int pageNum,
            @ApiParam(required = true, value = "pageSize")
            @RequestParam(value = "pageSize") int pageSize,
            @ApiParam(required = true, value = "uid")
            @RequestParam(value = "uid") int uid,
            @ApiParam(required = true, value = "type")
            @RequestParam(value = "type") int type) {

        List<WmsWalletCnyHistory> list = service.page(new Page<>(pageNum, pageSize),
                (new QueryWrapper<WmsWalletCnyHistory>().lambda()
                        .eq(WmsWalletCnyHistory::getUid, uid)
                        .eq(type > 0, WmsWalletCnyHistory::getType, type)
                        .orderByDesc(WmsWalletCnyHistory::getId))).getRecords();
        return ResponseResult.success(list);
    }

}
