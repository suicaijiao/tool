package com.soonphe.portal.controller;


import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.MmsAboutus;
import com.soonphe.portal.service.IMmsAboutusService;
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
 * 关于我们表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@RestController
@RequestMapping("/mms-aboutus")
@Api(tags = "【消息】关于我们")
public class MmsAboutusController {


    private static final Logger logger = LoggerFactory.getLogger(MmsAboutusController.class);

    @Autowired
    private IMmsAboutusService service;

    /**
     * 创建/更新关于我们
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("创建/更新关于我们")
    @PostMapping("/createObj")
    public ResponseResult createObj(@ApiParam(required = true, value = "实体")
                                                @RequestBody MmsAboutus tyAdvert) {
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

        List<MmsAboutus> list = service.list();
        return ResponseResult.success(list);
    }
}
