package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.UmsContract;
import com.soonphe.portal.service.IUmsContractService;
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
 * 合约表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@RestController
@RequestMapping("/ums-contract")
@Api(tags = "【合约】合约")
public class UmsContractController {

    private static final Logger logger = LoggerFactory.getLogger(UmsContractController.class);

    @Autowired
    private IUmsContractService service;


    /**
     * 创建/更新
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("创建/更新")
    @PostMapping("/createObj")
    public ResponseResult<UmsContract> createObj(@ApiParam(required = true, value = "实体")
                                                   @RequestBody UmsContract tyAdvert) {
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
    public ResponseResult<List<UmsContract>> getList(@ApiParam(required = true, value = "1可用 2不可用")
                                                       @RequestParam(value = "state") int state) {

        List<UmsContract> list = service.list(new QueryWrapper<UmsContract>().lambda()
                .eq(state > 0, UmsContract::getState, state));
        return ResponseResult.success(list);
    }

}
