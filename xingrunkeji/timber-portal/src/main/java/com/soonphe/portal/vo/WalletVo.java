package com.soonphe.portal.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author: soonphe
 * @date: 2019-05-16 16:33
 * @description:  钱包更新VO
 */
@Getter
@Setter
public class WalletVo {

    private String privateKey;
    private String wordKey;
    private String name;
    private String password;
    private String newPassword;
}
