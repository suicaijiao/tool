package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.UmsGainpacketRewardHistory;
import com.soonphe.portal.service.IUmsGainpacketRewardHistoryService;
import com.soonphe.portal.vo.GainpackRewarHistoryVo;
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
 * 增益包奖励历史记录 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-06-24
 */
@RestController
@RequestMapping("/ums-gainpacket-reward-history")
@Api(tags = "【增益包社群分红】增益包社群分红历史记录")
public class UmsGainpacketRewardHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(UmsGainpacketRewardHistoryController.class);

    @Autowired
    private IUmsGainpacketRewardHistoryService service;

    /**
     * 查询list
     *
     * @param pageNum
     * @param pageSize
     * @param uid
     * @return
     */
    @ApiOperation("查询list")
    @GetMapping("/getList")
    public ResponseResult<List<UmsGainpacketRewardHistory>> getList(
            @ApiParam(required = true, value = "pageNum")
            @RequestParam(value = "pageNum") int pageNum,
            @ApiParam(required = true, value = "pageSize")
            @RequestParam(value = "pageSize") int pageSize,
            @ApiParam(required = true, value = "uid")
            @RequestParam(value = "uid") int uid,
            @ApiParam(required = true, value = "shareType")
            @RequestParam(value = "shareType") int shareType) {

        List<UmsGainpacketRewardHistory> list = service.page(new Page<>(pageNum, pageSize),
                (new QueryWrapper<UmsGainpacketRewardHistory>().lambda()
                        .eq(uid > 0, UmsGainpacketRewardHistory::getUid, uid)
                        .eq(shareType > 0, UmsGainpacketRewardHistory::getShareType, shareType)
                        .orderByDesc(UmsGainpacketRewardHistory::getId))).getRecords();
        return ResponseResult.success(list);
    }

    /**
     * 分红收益汇总
     *
     * @param uid
     * @return
     */
    @ApiOperation("分红收益汇总")
    @GetMapping("/getListCount")
    public ResponseResult<GainpackRewarHistoryVo> getListCount(@ApiParam(required = true, value = "uid")
                                                     @RequestParam(value = "uid") int uid,
                                                     @ApiParam(required = true, value = "shareType")
                                                     @RequestParam(value = "shareType") int shareType) {
        List<UmsGainpacketRewardHistory> list = service.list(new QueryWrapper<UmsGainpacketRewardHistory>().lambda()
                .eq(uid > 0, UmsGainpacketRewardHistory::getUid, uid)
                .eq(shareType > 0, UmsGainpacketRewardHistory::getShareType, shareType)
                .orderByDesc(UmsGainpacketRewardHistory::getId));
        GainpackRewarHistoryVo result = new GainpackRewarHistoryVo();
        result.setCnyReward(new BigDecimal("0"));
        result.setFtReward(new BigDecimal("0"));
        if (list.size()>0){
            for (UmsGainpacketRewardHistory history : list) {
                if (history.getType()==1){
                    result.setCnyReward(result.getCnyReward().add(history.getAmount()));
                }else{
                    result.setFtReward(result.getFtReward().add(history.getAmount()));
                }
            }
        }
        return ResponseResult.success(result);
    }

}
