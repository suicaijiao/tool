package com.soonphe.portal.commons.constant;

/**
 * @description
 * @author: suicaijiao
 * @create: 2020-08-21 14:11
 **/
public enum CommonsEnum {

    STATUS_NOT_END(0, "未结束"),
    STATUS_END(1, "已结束"),

    WIN_STATUS_FALSE(0, "未中奖"),
    WIN_STATUS_TRUE(1, "已中奖"),

    CONTRACT_STATUS_FALSE(0, "合约到期"),
    CONTRACT_STATUS_TRUE(1, "合约未到期"),
    /**
     * 1转入 2转出
     */
    WALLET_TRANSFER_OUT(1, "转入"),
    WALLET_TRANSFER_ENTER(2, "转出"),


    USER_GAME_FALSE(0, "游戏申请"),
    USER_GAME_TRUE(1, "游戏中"),
    USER_GAME_END(2, "游戏结束"),
    /**
     * -1体验  0有效  1s1  2s2  3s3  4s4
     */
    USER_LEVEL_TY(-1, "体验"),
    USER_LEVEL_YX(0, "有效"),
    USER_LEVEL_S1(1, "有效"),
    USER_LEVEL_S2(2, "有效"),
    USER_LEVEL_S3(3, "有效"),

    GAME_MATCHING(1,"匹配进入游戏"),
    GAME_STATE(2,"游戏状态"),
    GAME_LOTTERY(3,"游戏开奖结果"),

    ;
    private Integer code;
    private String message;

    CommonsEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
