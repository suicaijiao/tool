package com.soonphe.portal.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author: soonphe
 * @date: 2019-05-16 16:33
 * @description:  叠加交易价格列表VO
 */
@Getter
@Setter
public class TradeVo {

    private BigDecimal price;

    private BigDecimal amount;
    
}
