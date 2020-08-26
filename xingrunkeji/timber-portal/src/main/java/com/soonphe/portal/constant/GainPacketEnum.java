package com.soonphe.portal.constant;

/**
 * 增益包
 */
public enum GainPacketEnum {

    /**
     * 增益包
     */
    GAIN_PACKET_JUNIOR(10000,10),
    GAIN_PACKET_MID(25000,25),
    GAIN_PACKET_HIGH(50000,50);

    private int code;

    private int dailyGameNum;


    private GainPacketEnum(int code, int dailyGameNum) {
        this.code = code;
        this.dailyGameNum = dailyGameNum;
    }


    public int getCode() {
        return code;
    }


    public int getDailyGameNum() {
        return dailyGameNum;
    }
}
