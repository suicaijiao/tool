package com.soonphe.portal.commons.constant;

import java.math.BigDecimal;

/**
 * @description
 * @author: suicaijiao
 * @create: 2020-08-20 15:04
 **/
public class SysConfInterface {

    //社群等级
    public static Integer communityLevelTy = -1;
    public static Integer communityLevelNone = 0;
    public static Integer communityLevelFirst = 1;
    public static Integer communityLevelSecond = 2;
    public static Integer communityLevelThird = 3;
    public static Integer communityLevelFourth = 4;


    //社群奖励，直推有效用户
    public static Integer communityEffectiveFirst = 10;
    public static Integer communityEffectiveSecond = 20;
    public static Integer communityEffectiveThird = 30;
    public static Integer communityEffectiveFourth = 50;

    //社群奖励， 收益比例
    public static BigDecimal communityRatioFirst = new BigDecimal("0.1");
    public static BigDecimal communityRatioSecond = new BigDecimal("0.2");
    public static BigDecimal communityRatioThird = new BigDecimal("0.3");
    public static BigDecimal communityRatioFourth = new BigDecimal("0.4");

    /**
     * 基础奖励USDT(未获奖用户获取)
     */

    public static BigDecimal BASE_USDT = new BigDecimal(1.2285).setScale(4, BigDecimal.ROUND_DOWN);
    // 游戏门票
    public static BigDecimal TICKET = new BigDecimal("0.15");
    // 获奖用户拿出15USDT换成等价DOT
    public static BigDecimal USER_USDT = new BigDecimal(15);
    // 1-2.5% ，平台扣除比例 DOT
    public static BigDecimal USER_GET_FT_RATIO = new BigDecimal(0.975);

    /**
     * 获奖用户获取的DOT
     */
    public static BigDecimal USER_GET_DOT = new BigDecimal(0.975).multiply(USER_USDT);

    /**
     * 矿工USDT费用数量费率，每个人收取（平台扣除）
     */
    public static BigDecimal GAME_USDT_COST = new BigDecimal(0.13);

    /**
     * 矿工DTO费用数量费率,扣除获奖用户拿出15U
     */
    public static BigDecimal GAME_DOT_COST = USER_USDT.multiply(new BigDecimal(0.025));


}
