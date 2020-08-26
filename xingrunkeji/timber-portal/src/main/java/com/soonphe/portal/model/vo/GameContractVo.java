package com.soonphe.portal.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @description 查询用户合约
 * @author: suicaijiao
 * @create: 2020-08-24 13:02
 **/
@Getter
@Setter
public class GameContractVo {

    /**
     * 游戏获得奖励总数
     */
    private BigDecimal sumRewardUSDT;

    /**
     * 合约最大收益
     */
    private BigDecimal specMax;

    /**
     * 用户生效合约等级
     */
    private int level;

}
