package com.soonphe.portal.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.soonphe.portal.commons.golbal.result.ResponseResult;
import com.soonphe.portal.commons.golbal.result.ResultCode;
import com.soonphe.portal.entity.GmsHistory;
import com.soonphe.portal.entity.WmsWallet;
import com.soonphe.portal.model.GameMatchingUser;
import com.soonphe.portal.model.vo.GameEndResultVo;
import com.soonphe.portal.service.*;
import com.soonphe.portal.util.redis.StringRedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.soonphe.portal.util.redis.RedisKeyConstant.*;

/**
 * <p>
 * 游戏记录表 前端控制器
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
@RestController
@RequestMapping("/gms-history")
@Api(tags = "【游戏】游戏")
public class GmsHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(GmsHistoryController.class);

    @Autowired
    private IGmsHistoryService service;
    @Autowired
    private IWmsWalletService walletService;


    /**
     * 游戏匹配
     *
     * @param uid
     * @return
     */
    @ApiOperation("游戏匹配")
    @GetMapping("/matching/{uid}")
    public ResponseResult matching(@ApiParam(required = true, value = "uid") @PathVariable("uid") Integer uid) {
        GameMatchingUser result = service.checkUserGameValid(uid);
        if (result != null) {
            return ResponseResult.success(true);
        }
        return ResponseResult.success(false);
    }

    //    /**
//     * 查询游戏当前匹配玩家名称
//     *
//     * @return
//     */
//    @ApiOperation("查询游戏当前匹配玩家名称")
//    @GetMapping("/getMatchingNumName")
//    public ResponseResult getMatchingNumName() {
//        Map<String, Object> gameUserMap = service.getMatchingUser();
//        return ResponseResult.success(gameUserMap);
//
//    }
//
//    @ApiOperation("开始游戏")
//    @GetMapping("/gamStart/{userId}")
//    public ResponseResult startGame(@ApiParam(required = true, value = "gameId")
//                                    @PathVariable("userId") Integer userId) {
//
//        service.matchingGameUser(userId);
//        return ResponseResult.success(true);
//    }


    /**
     * 游戏取消匹配
     *
     * @param uid
     * @return
     */
    @ApiOperation("游戏取消匹配")
    @GetMapping("/matchingCancel")
    public ResponseResult matchingCancel(@ApiParam(required = true, value = "uid")
                                         @RequestParam(value = "uid") int uid) {
        WmsWallet wmsWallet = walletService.getById(uid);
        List<String> list = StringRedisUtil.getList(GAME_ROOM);
        if (list.size() > 0) {
            if (list.contains(uid)) {

                list.remove(uid);
            }
            StringRedisUtil.removeListIndexValue(GAME_ROOM, 1, uid + "");
            StringRedisUtil.removeListIndexValue(GAME_ROOM_NAME, 1, wmsWallet.getTrxAddress());
        }

        if (StringRedisUtil.contain(GAME_ROOM_USER + uid)) {
            StringRedisUtil.del(GAME_ROOM_USER + uid);
        }
        return ResponseResult.success(true);
    }

    /**
     * 查询游戏当前匹配玩家
     *
     * @return
     */
    @ApiOperation("查询游戏当前匹配玩家")
    @GetMapping("/getMatchingNum")
    public ResponseResult getMatchingNum() {
        List<String> list = StringRedisUtil.getList(GAME_ROOM);
        if (list.size() < 1) {
            return ResponseResult.success(new ArrayList<>());
        }
//        StringRedisUtil.setList(GAME_ROOM, list);
        return ResponseResult.success(list);
    }


    /**
     * 查询游戏最后一轮获奖用户
     *
     * @return
     */
    @ApiOperation("查询游戏最后一轮获奖用户")
    @GetMapping("/getGameWin")
    public ResponseResult getGameWin() {
        String result = StringRedisUtil.get(GAME_WIN_ID) + "";
        return ResponseResult.success(result);
    }

    /**
     * 查询用户游戏记录list
     *
     * @param uid
     * @return
     */
    @ApiOperation("查询用户游戏记录list")
    @GetMapping("/getList")
    public ResponseResult getList(@ApiParam(required = true, value = "pageNum")
                                  @RequestParam(value = "pageNum") int pageNum,
                                  @ApiParam(required = true, value = "pageSize")
                                  @RequestParam(value = "pageSize") int pageSize,
                                  @ApiParam(required = true, value = "uid")
                                  @RequestParam(value = "uid") int uid,
                                  @ApiParam(required = false, value = "gid")
                                  @RequestParam(required = false, value = "gid") String gid) {

        IPage<GmsHistory> page1 = service.page(new Page<>(pageNum, pageSize),
                (new QueryWrapper<GmsHistory>().lambda()
                        .eq(GmsHistory::getUid, uid)
                        .eq(gid != null && gid.length() > 0, GmsHistory::getGid, gid)
                        .orderByDesc(GmsHistory::getId)
                ));
        if (page1 == null) {
            ResponseResult.failed(ResultCode.VALIDATE_FAILED.getCode(), "没有相关游戏记录");
        }
        return ResponseResult.success(page1.getRecords(), "", page1.getTotal() + "");
    }

}
