package com.soonphe.portal.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author: soonphe
 * @date: 2019-05-16 16:33
 * @description:  钱包分布统计VO
 */
@Getter
@Setter
public class StatsWalletDistributeVo {

    private BigDecimal destoryTotal;
    private int walletTotal;
    private int walletValid;

    private int walletZero;
    private int walletOne;
    private int walletTwo;
    private int walletThree;
    private int walletFour;

    private int levelOne;
    private int levelTwo;
    private int levelThree;
}
