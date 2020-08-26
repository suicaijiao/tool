package com.soonphe.portal.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author: soonphe
 * @date: 2019-05-16 16:33
 * @description: usdt充提统计VO
 */
@Getter
@Setter
public class StatsCurrencyVo {

    private BigDecimal trxAmount;
    private BigDecimal cnyAmount;
    private BigDecimal ftAmount;
    private BigDecimal winFt;
    private BigDecimal releaseTotal;

    //充值数量
    private BigDecimal trxRechargeAmount;
    //提现数量
    private BigDecimal trxWithdrawalAmount;
}
