package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.GmsDailyNum;
import com.soonphe.portal.service.IGmsDailyNumService;
import com.soonphe.portal.service.IWmsWalletService;
import com.soonphe.portal.vo.GameDailyNumVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 游戏每日次数统计 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-06-21
 */
@RestController
@RequestMapping("/gms-daily-num")
@Api(tags = "【游戏】游戏")
public class GmsDailyNumController {


    private static final Logger logger = LoggerFactory.getLogger(GmsDailyNumController.class);

    @Autowired
    private IGmsDailyNumService service;
    @Autowired
    private IWmsWalletService iWmsWalletService;

    /**
     * 获取用户今日游戏次数
     *
     * @return
     */
    @ApiOperation("获取用户今日游戏次数")
    @PostMapping("/getNowNum")
    public ResponseResult getNowNum(@ApiParam(required = true, value = "实体")
                                                         @RequestBody GameDailyNumVo vo) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String now = simpleDateFormat.format(vo.getCreateDate());

        List<GmsDailyNum> list = service.list(new QueryWrapper<GmsDailyNum>().lambda()
                .eq(GmsDailyNum::getCreateDate, now)
                .eq(GmsDailyNum::getUid, vo.getUid()));
        if (list.size() > 0) {
            return ResponseResult.success(list);
        }
        return ResponseResult.success(new ArrayList<>());
    }

}
