package com.soonphe.portal.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author: soonphe
 * @date: 2019-05-16 16:33
 * @description:  增益包社群分红汇总
 */
@Getter
@Setter
public class GainpackRewarHistoryVo {

    private BigDecimal cnyReward;

    private BigDecimal ftReward;

}
