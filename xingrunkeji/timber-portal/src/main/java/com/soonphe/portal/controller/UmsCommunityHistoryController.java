package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.UmsCommunityHistory;
import com.soonphe.portal.service.IUmsCommunityHistoryService;
import com.soonphe.portal.vo.CommunityProfitVo;
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

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 社群收益记录表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-06-05
 */
@RestController
@RequestMapping("/ums-community-history")
@Api(tags = "【社群收益】社群收益")
public class UmsCommunityHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(UmsCommunityHistoryController.class);

    @Autowired
    private IUmsCommunityHistoryService service;

    /**
     * 查询list
     *
     * @return
     */
    @ApiOperation("查询list")
    @GetMapping("/getList")
    public ResponseResult<List<UmsCommunityHistory>> getList(@ApiParam(required = true, value = "pageNum")
                                                               @RequestParam(value = "pageNum") int pageNum,
                                                               @ApiParam(required = true, value = "pageSize")
                                                               @RequestParam(value = "pageSize") int pageSize,
                                                               @ApiParam(required = true, value = "uid")
                                                               @RequestParam(value = "uid") int uid) {

        List<UmsCommunityHistory> list = service.page(new Page<>(pageNum, pageSize),
                (new QueryWrapper<UmsCommunityHistory>().lambda()
                        .eq(UmsCommunityHistory::getUid, uid)
                        .orderByDesc(UmsCommunityHistory::getId)
                )).getRecords();
        return ResponseResult.success(list);
    }


    /**
     * 邀请绑定收益汇总
     *
     * @return
     */
    @ApiOperation("邀请绑定收益汇总")
    @GetMapping("/getListCount")
    public ResponseResult<CommunityProfitVo> getListCount(@ApiParam(required = true, value = "uid")
                                                            @RequestParam(value = "uid") int uid) {

        CommunityProfitVo vo = new CommunityProfitVo();
        vo.setTicketCount(new BigDecimal("0"));
        vo.setMinerCnyCount(new BigDecimal("0"));
        vo.setMinerFtCount(new BigDecimal("0"));

        List<UmsCommunityHistory> list = service.list(new QueryWrapper<UmsCommunityHistory>().lambda()
                .eq(UmsCommunityHistory::getUid, uid));
        if (list.size()>0){
            for (UmsCommunityHistory history : list) {
                vo.setMinerCnyCount(vo.getMinerCnyCount().add(history.getMinerUsdt()));
                vo.setMinerFtCount(vo.getMinerFtCount().add(history.getMinerDot()));
            }
        }
        return ResponseResult.success(vo);
    }
}
