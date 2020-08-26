package com.soonphe.portal.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description
 * @author: suicaijiao
 * @create: 2020-08-24 09:35
 **/
@Getter
@Setter
public class GameEndResultVo {

    /**
     * 用户id
     */
    private Integer winUserId;

    /**
     * 游戏状态 0未获奖 1已获奖
     */
    private Integer status;

    /**
     * 游戏奖金FDT
     */
    private BigDecimal gameBonusDOT;

    /**
     * 游戏奖金USDT
     */
    private BigDecimal gameBonusUSDT;

    private Integer userId;

}
