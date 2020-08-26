package com.soonphe.portal.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: soonphe
 * @date: 2019-05-16 16:33
 * @description:  钱包更新VO
 */
@Getter
@Setter
public class WalletPrivateVo {

    private String privateKey;
    private String wordKey;
    private String newPrivateKey;
    private String newWordKey;

}
