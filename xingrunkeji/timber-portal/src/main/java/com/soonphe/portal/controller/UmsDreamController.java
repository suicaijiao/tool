package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soonphe.portal.entity.UmsContract;
import com.soonphe.portal.entity.UmsDream;
import com.soonphe.portal.service.IUmsContractService;
import com.soonphe.portal.service.IUmsDreamService;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 圆梦计划表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-08-01
 */
@RestController
@RequestMapping("/ums-dream")
@Api(tags = "【圆梦计划】圆梦计划")
public class UmsDreamController {


    private static final Logger logger = LoggerFactory.getLogger(UmsDreamController.class);

    @Autowired
    private IUmsDreamService service;


    /**
     * 创建/更新
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("创建/更新")
    @PostMapping("/createObj")
    public ResponseResult<UmsDream> createObj(@ApiParam(required = true, value = "实体")
                                                   @RequestBody UmsDream tyAdvert) {
        service.saveOrUpdate(tyAdvert);
        return ResponseResult.success(tyAdvert);
    }

    /**
     * 查询list
     *
     * @return
     */
    @ApiOperation("查询list")
    @GetMapping("/getList")
    public ResponseResult<List<UmsDream>> getList(@ApiParam(required = true, value = "1可用 2不可用")
                                                       @RequestParam(value = "state") int state) {

        List<UmsDream> list = service.list(new QueryWrapper<UmsDream>().lambda()
                .eq(state > 0, UmsDream::getState, state));
        return ResponseResult.success(list);
    }
}
