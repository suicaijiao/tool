package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.MmsNews;
import com.soonphe.portal.service.IMmsNewsService;
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
 * 消息表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-05-27
 */
@RestController
@RequestMapping("/mms-news")
@Api(tags = "【消息】消息")
public class MmsNewsController {

    private static final Logger logger = LoggerFactory.getLogger(MmsNewsController.class);

    @Autowired
    private IMmsNewsService service;

    /**
     * 创建/更新消息
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("创建/更新消息")
    @PostMapping("/createNews")
    public ResponseResult createNews(@ApiParam(required = true, value = "实体")
                                                @RequestBody MmsNews tyAdvert) {
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
    public ResponseResult<List<MmsNews>> getList(@ApiParam(required = true, value = "pageNum")
                                                   @RequestParam(value = "pageNum") int pageNum,
                                                   @ApiParam(required = true, value = "pageSize")
                                                   @RequestParam(value = "pageSize") int pageSize) {

        List<MmsNews> list = service.page(new Page<>(pageNum, pageSize),
                (new QueryWrapper<MmsNews>().lambda()
                .orderByDesc(MmsNews::getId))).getRecords();
        return ResponseResult.success(list);
    }

    /**
     * 删除
     *
     * @return
     */
    @ApiOperation("删除")
    @GetMapping("/deleteObj")
    public ResponseResult<Boolean> deleteObj(@ApiParam(required = true, value = "id")
                                                   @RequestParam(value = "id") int id) {

        return ResponseResult.success(service.removeById(id));
    }
}
