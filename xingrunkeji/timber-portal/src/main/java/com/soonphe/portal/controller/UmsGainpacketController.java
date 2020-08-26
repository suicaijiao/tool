package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.UmsGainpacket;
import com.soonphe.portal.service.IUmsGainpacketService;
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
 * 增益表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@RestController
@RequestMapping("/ums-gainpacket")
@Api(tags = "【增益包】增益包")
public class UmsGainpacketController {

    private static final Logger logger = LoggerFactory.getLogger(UmsGainpacketController.class);

    @Autowired
    private IUmsGainpacketService service;


    /**
     * 创建/更新
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("创建/更新")
    @PostMapping("/createObj")
    public ResponseResult<UmsGainpacket> createObj(@ApiParam(required = true, value = "实体")
                                                   @RequestBody UmsGainpacket tyAdvert) {
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
    public ResponseResult<List<UmsGainpacket>> getList(@ApiParam(required = true, value = "1可用 2不可用")
                                                             @RequestParam(value = "state") int state) {

        List<UmsGainpacket> list = service.list(new QueryWrapper<UmsGainpacket>().lambda()
                .eq(state > 0, UmsGainpacket::getState, state));
        return ResponseResult.success(list);
    }

}
