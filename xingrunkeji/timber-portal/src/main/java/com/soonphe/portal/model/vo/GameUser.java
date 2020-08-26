package com.soonphe.portal.model.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @description 缓存游戏匹配用户
 * @author: suicaijiao
 * @create: 2020-08-19 17:09
 **/
@Getter
@Setter
public class GameUser {

    /**
     * 用户id
     */
    private int userId;

    /**
     * TRX地址
     */
    private String trxAddress;

}
