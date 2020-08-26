package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.WmsWalletFtHistory;
import com.soonphe.portal.service.IWmsWalletFtHistoryService;
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
@RequestMapping("/wms-wallet-ft-history")
@Api(tags = "【ft充提记录】ft充提记录")
public class WmsWalletFtHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(UmsGainpacketHistoryController.class);

    @Autowired
    private IWmsWalletFtHistoryService service;

    /**
     * 查询list
     *
     * @return
     */
    @ApiOperation("查询list")
    @GetMapping("/getList")
    public ResponseResult<List<WmsWalletFtHistory>> getList(
            @ApiParam(required = true, value = "pageNum")
            @RequestParam(value = "pageNum") int pageNum,
            @ApiParam(required = true, value = "pageSize")
            @RequestParam(value = "pageSize") int pageSize,@ApiParam(required = true, value = "uid")
                                                              @RequestParam(value = "uid") int uid,
                                                              @ApiParam(required = true, value = "type")
                                                              @RequestParam(value = "type") int type) {

        List<WmsWalletFtHistory> list = service.page(new Page<>(pageNum, pageSize),
                (new QueryWrapper<WmsWalletFtHistory>().lambda()
                .eq(WmsWalletFtHistory::getUid, uid)
                .eq(type > 0, WmsWalletFtHistory::getType, type)
                        .orderByDesc(WmsWalletFtHistory::getId))).getRecords();
        return ResponseResult.success(list);
    }

}
