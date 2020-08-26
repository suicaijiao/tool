package com.soonphe.portal.constant;

/**
 * 合约
 */
public enum ContractEnum {
    /**
     * 合约等级
     */
    CONTRACT_V1(50,1,10),
    CONTRACT_V2(500,7,20),
    CONTRACT_V3(1000,10,30),
    CONTRACT_V4(5000,35,50),
    CONTRACT_V5(10000,60,100),
    CONTRACT_V6(30000,90,300),
    CONTRACT_V7(50000,120,500);

    private int code;

    private int daysNum;

    private int dailyGameNum;


    private ContractEnum(int code, int daysNum,int dailyGameNum) {
        this.code = code;
        this.daysNum = daysNum;
        this.dailyGameNum = dailyGameNum;
    }


    public int getCode() {
        return code;
    }

    public int getDaysNum() {
        return daysNum;
    }

    public int getDailyGameNum() {
        return dailyGameNum;
    }
}
