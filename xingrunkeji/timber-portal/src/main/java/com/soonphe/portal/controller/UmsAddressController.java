package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.UmsAddress;
import com.soonphe.portal.service.IUmsAddressService;
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
 * 地址簿表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-05-27
 */
@RestController
@RequestMapping("/mms-address")
@Api(tags = "【地址簿】地址簿")
public class UmsAddressController {

    private static final Logger logger = LoggerFactory.getLogger(UmsAddressController.class);

    @Autowired
    private IUmsAddressService service;

    /**
     * 创建/更新地址簿
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("创建/更新地址簿")
    @PostMapping("/createAddress")
    public ResponseResult<UmsAddress> createAddress(@ApiParam(required = true, value = "实体")
                                                      @RequestBody UmsAddress tyAdvert) {
        UmsAddress wallet = service.getOne(new QueryWrapper<UmsAddress>().lambda()
                .eq(UmsAddress::getId, tyAdvert.getId()));
        if (wallet != null) {
            tyAdvert.setId(wallet.getId());
            service.updateById(tyAdvert);
            return ResponseResult.success(tyAdvert);
        }
        service.save(tyAdvert);
        return ResponseResult.success(tyAdvert);
    }

    /**
     * 删除地址簿
     *
     * @param id
     * @return
     */
    @ApiOperation("删除地址簿")
    @GetMapping("/deleteAddress")
    public ResponseResult<Object> deleteAddress(@ApiParam(required = true, value = "id")
                                                  @RequestParam(value = "id") int id) {
        return ResponseResult.success(service.removeById(id));

    }

    /**
     * 查询list
     *
     * @param uid
     * @return
     */
    @ApiOperation("查询list")
    @GetMapping("/getList")
    public ResponseResult<List<UmsAddress>> getList(
            @ApiParam(required = true, value = "pageNum")
            @RequestParam(value = "pageNum") int pageNum,
            @ApiParam(required = true, value = "pageSize")
            @RequestParam(value = "pageSize") int pageSize,
            @ApiParam(required = true, value = "uid")
                                                      @RequestParam(value = "uid") int uid) {

        List<UmsAddress> list = service.page(new Page<>(pageNum, pageSize),
                (new QueryWrapper<UmsAddress>().lambda()
                .eq(UmsAddress::getUid, uid)
                        .orderByDesc(UmsAddress::getId)
        )).getRecords();
        return ResponseResult.success(list);
    }

}
