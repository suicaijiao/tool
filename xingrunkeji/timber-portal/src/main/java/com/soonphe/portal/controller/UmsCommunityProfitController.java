package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.entity.UmsCommunityProfit;
import com.soonphe.portal.entity.WmsWallet;
import com.soonphe.portal.service.IUmsCommunityProfitService;
import com.soonphe.portal.service.IWmsWalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 社群收益表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@RestController
@RequestMapping("/ums-community-profit")
@Api(tags = "【社群收益】基础数据")
public class UmsCommunityProfitController {

    private static final Logger logger = LoggerFactory.getLogger(UmsCommunityProfitController.class);

    @Autowired
    private IUmsCommunityProfitService service;
    @Autowired
    private IWmsWalletService walletService;


    /**
     * 创建/更新
     *
     * @param tyAdvert
     * @return
     */
    @ApiOperation("创建/更新")
    @PostMapping("/createObj")
    public ResponseResult<UmsCommunityProfit> createObj(@ApiParam(required = true, value = "实体")
                                                          @RequestBody UmsCommunityProfit tyAdvert) {
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
    public ResponseResult<List<UmsCommunityProfit>> getList() {

        List<UmsCommunityProfit> list = service.list();
        return ResponseResult.success(list);
    }

    /**
     * 发放社群收益
     * @param level
     * @param giveCount
     * @return
     */
    @ApiOperation("发放社群收益")
    @GetMapping("/giveCommunityProfit")
    public ResponseResult<Boolean> giveCommunityProfit(@ApiParam(required = true, value = "level")
                                                                          @RequestParam(value = "level") int level,
                                                                          @ApiParam(required = true, value = "giveCount")
                                                                          @RequestParam(value = "giveCount") BigDecimal giveCount) {
        //查询同level下所有用户
        List<WmsWallet> list = walletService.list(new QueryWrapper<WmsWallet>().lambda()
                .eq(WmsWallet::getGainpackLevel, level));
        if (list.size()<1){
            return ResponseResult.failed("当前level下没有用户");
        }
        //计算每人数量
        BigDecimal everyoneNum = giveCount.divide(new BigDecimal(list.size() + ""),4);
        for (WmsWallet wallet : list) {
            wallet.setFtAmount(wallet.getFtAmount().add(everyoneNum));
        }
        return ResponseResult.success(walletService.updateBatchById(list));
    }


}
