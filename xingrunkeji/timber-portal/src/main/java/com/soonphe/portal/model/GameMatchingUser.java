package com.soonphe.portal.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @description 缓存游戏匹配用户
 * @author: suicaijiao
 * @create: 2020-08-19 17:09
 **/
@Getter
@Setter
public class GameMatchingUser {

    /**
     * 用户id
     */
    private int userId;

    /**
     * TRX地址
     */
    private String trxAddress;

    /**
     * 用户游戏状态 0.申请游戏 1.游戏中，2游戏结束
     */
    private Integer status;

    /**
     * 当日游戏次数
     */
    private Integer dayGameCount;

}
