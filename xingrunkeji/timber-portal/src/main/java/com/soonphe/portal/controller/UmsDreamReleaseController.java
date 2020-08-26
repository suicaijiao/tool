package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soonphe.portal.entity.UmsDreamRelease;
import com.soonphe.portal.entity.WmsWalletRelease;
import com.soonphe.portal.service.IUmsDreamReleaseService;
import com.soonphe.portal.service.IWmsWalletReleaseService;
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

import java.util.List;

/**
 * <p>
 * 用户钱包释放记录 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-08-01
 */
@RestController
@RequestMapping("/ums-dream-release")
@Api(tags = "【圆梦计划】释放记录")
public class UmsDreamReleaseController {

    private static final Logger logger = LoggerFactory.getLogger(UmsDreamReleaseController.class);

    @Autowired
    private IUmsDreamReleaseService service;

    /**
     * 查询list
     *
     * @return
     */
    @ApiOperation("查询list")
    @GetMapping("/getList")
    public ResponseResult<List<UmsDreamRelease>> getList(@ApiParam(required = true, value = "pageNum")
                                                           @RequestParam(value = "pageNum") int pageNum,
                                                           @ApiParam(required = true, value = "pageSize")
                                                           @RequestParam(value = "pageSize") int pageSize,
                                                           @ApiParam(required = true, value = "uid")
                                                           @RequestParam(value = "uid") int uid) {

        List<UmsDreamRelease> list = service.page(new Page<>(pageNum, pageSize),
                (new QueryWrapper<UmsDreamRelease>().lambda()
                        .eq(UmsDreamRelease::getUid, uid)
                        .orderByDesc(UmsDreamRelease::getId))).getRecords();
        return ResponseResult.success(list);
    }
}
