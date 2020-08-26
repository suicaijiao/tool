package com.soonphe.portal.util.rate;

/**
 * @author: soonphe
 * @date: 2020-08-10
 * @description: 发行价更新
 */
public class RateUpdate {

    //{"symbol":"dotdosusdt","token":"9900455D4E545996CF443EFF355BFCFF2C7A4A83","price":1}
    private String symbol = "dotdosusdt";
    private String token = "9900455D4E545996CF443EFF355BFCFF2C7A4A83";
    private double price = 1;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
