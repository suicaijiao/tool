package com.soonphe.portal.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description 用户游戏记录统计数据
 * @author: suicaijiao
 * @create: 2020-08-22 16:52
 **/
@Getter
@Setter
public class DayUserGameStatisticsVo {

    /**
     * 用户编号
     */
    private Integer userId;

    /**
     * 用户当日有序获取的USDT总数
     */
    private BigDecimal rewardUsdt;

    /**
     * 用户当日游戏获得的DOT总数
     */
    private BigDecimal rewardDot;

    /**
     * 用户当日游戏总数
     */
    private Integer gameCount;

    /**
     * 推荐id集合
     */
    private Integer recommendId;

}
