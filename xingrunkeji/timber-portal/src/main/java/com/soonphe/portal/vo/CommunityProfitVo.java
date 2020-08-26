package com.soonphe.portal.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author: soonphe
 * @date: 2019-05-16 16:33
 * @description:  邀请绑定收益汇总VO
 */
@Getter
@Setter
public class CommunityProfitVo {

    private BigDecimal ticketCount;

    private BigDecimal minerFtCount;

    private BigDecimal minerCnyCount;
    
}
