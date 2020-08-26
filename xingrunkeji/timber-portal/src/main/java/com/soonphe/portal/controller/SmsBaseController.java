package com.soonphe.portal.controller;


import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.SmsBase;
import com.soonphe.portal.service.ISmsBaseService;
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
 * 平台基础数据表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@RestController
@RequestMapping("/sms-base")
@Api(tags = "【系统】基础数据")
public class SmsBaseController {

    private static final Logger logger = LoggerFactory.getLogger(SmsBaseController.class);

    @Autowired
    private ISmsBaseService service;

    /**
     * 创建/更新基础数据
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("创建/更新消息")
    @PostMapping("/createObj")
    public ResponseResult createObj(@ApiParam(required = true, value = "实体")
                                                  @RequestBody SmsBase tyAdvert) {
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
    public ResponseResult getList() {

        List<SmsBase> list = service.list();
        return ResponseResult.success(list);
    }

}
