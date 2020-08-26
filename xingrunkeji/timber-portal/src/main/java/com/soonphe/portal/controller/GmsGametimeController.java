package com.soonphe.portal.controller;


import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.GmsGametime;
import com.soonphe.portal.service.IGmsGametimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 游戏时间限定表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-07-25
 */
@RestController
@RequestMapping("/gms-gametime")
@Api(tags = "【游戏】游戏")
public class GmsGametimeController {


    private static final Logger logger = LoggerFactory.getLogger(GmsGametimeController.class);

    @Autowired
    private IGmsGametimeService service;

    /**
     * 获取每日可玩时间段
     *
     * @return
     */
    @ApiOperation("获取每日可玩时间段")
    @GetMapping("/getList")
    public ResponseResult getList() {

        List<GmsGametime> list = service.list();
        if (list.size() > 0) {
            return ResponseResult.success(list);
        }
        return ResponseResult.success(new ArrayList<>());
    }

    /**
     * 创建/更新
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("创建/更新")
    @PostMapping("/createObj")
    public ResponseResult createObj(@ApiParam(required = true, value = "实体")
                                                @RequestBody GmsGametime tyAdvert) {
        service.saveOrUpdate(tyAdvert);
        return ResponseResult.success(tyAdvert);
    }
}
