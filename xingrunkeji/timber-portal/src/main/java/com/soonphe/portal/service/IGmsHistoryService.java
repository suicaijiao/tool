package com.soonphe.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.soonphe.portal.entity.GmsHistory;
import com.soonphe.portal.entity.WmsWallet;
import com.soonphe.portal.model.GameMatchingUser;
import com.soonphe.portal.model.vo.GameEndResultVo;
import com.soonphe.portal.vo.DayUserGameStatisticsVo;
import com.soonphe.portal.vo.QueryUserGameStatisticsVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 游戏记录表 服务类
 * </p>
 *
 * @author soonphe
 * @since 2020-05-30
 */
public interface IGmsHistoryService extends IService<GmsHistory> {

    /**
     * 验证用户合约是否有效
     *
     * @param userId
     */
    GameMatchingUser checkUserGameValid(Integer userId);

    /**
     * 获取人数够10个开局
     *
     * @return
     */
    GameEndResultVo matchingGameUser(List<GameMatchingUser> matchingGameUserList);

    /**
     * 开始游戏
     *
     * @param gameUserList
     * @return
     */
    GameEndResultVo gameStart(List<GameMatchingUser> gameUserList, String gameRoomId);

    /**
     * 查询当前所有用户游戏记录统计
     *
     * @param query
     * @return
     */
    List<DayUserGameStatisticsVo> getPageUserGameStatistics(QueryUserGameStatisticsVo query);

    /**
     * 查询当前所有用户游戏记录统计数量
     *
     * @param query
     * @return
     */
    int countDayUserGameStatistics(QueryUserGameStatisticsVo query);


}
