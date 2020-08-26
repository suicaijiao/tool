package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.SmsDestory;
import com.soonphe.portal.service.ISmsDestoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 销毁表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-06-29
 */
@RestController
@RequestMapping("/sms-destory")
@Api(tags = "【销毁】销毁记录")
public class SmsDestoryController {

    private static final Logger logger = LoggerFactory.getLogger(SmsDestoryController.class);

    @Autowired
    private ISmsDestoryService service;

    /**
     * 创建/更新关于我们
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("创建/更新关于我们")
    @PostMapping("/createObj")
    public ResponseResult createObj(@ApiParam(required = true, value = "实体")
                                                  @RequestBody SmsDestory tyAdvert) {
        tyAdvert.setCreateTime(new Date());
        service.saveOrUpdate(tyAdvert);
        return ResponseResult.success(tyAdvert);
    }

    /**
     * 分页获取记录List
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation("分页获取记录List")
    @GetMapping("getPageList")
    public ResponseResult getPageList(@ApiParam(required = true, value = "pageNum")
                                                          @RequestParam(value = "pageNum") int pageNum,
                                                          @ApiParam(required = true, value = "pageSize")
                                                          @RequestParam(value = "pageSize") int pageSize) {
        IPage<SmsDestory> page1 = service.page(new Page<>(pageNum, pageSize),
                new QueryWrapper<SmsDestory>().lambda()
                        .orderByDesc(SmsDestory::getId)
        );
        return ResponseResult.success(page1.getRecords());
    }

    /**
     * 统计销毁总计
     *
     * @param sid
     * @return
     */
    @ApiOperation("统计销毁总计")
    @GetMapping("/getTotal")
    public ResponseResult getTotal(@ApiParam(required = true, value = "sid")
                                                 @RequestParam(value = "sid") int sid) {
        return ResponseResult.success(service.getTotal());
    }

}
