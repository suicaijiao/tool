package com.soonphe.portal.util.rate;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soonphe.portal.controller.WmsWalletController;
import com.soonphe.portal.util.http.HttpUtil;
import com.soonphe.portal.util.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;

import static java.util.Calendar.HOUR_OF_DAY;

/**
 * @Author：soonphe
 * @Date：2017-09-22 11:38
 * @Comments：
 */
public class TronApiUtil {

    private static final Logger logger = LoggerFactory.getLogger(TronApiUtil.class);

    public static final String WALLET_CREATE_API =
            "http://65.49.199.222:8900/wallet/create";


    public static final String WALLET_TRANSFER_API =
            "http://65.49.199.222:8900/wallet/transfer/";


    public static final String WALLET_TRANSFER_FUNDCOLLECTION_API =
            "http://65.49.199.222:8900/wallet/fundCollection/";


    public static final String WALLET_RECHARGE_API =
            "http://65.49.199.222:8900/wallet/recharge/";

    public static String WALLET_CREATE() {
        String result = HttpUtil.post(WALLET_CREATE_API, null);
        String realResult = result.substring(4, result.length());
        System.out.println(realResult);
        JSONObject data = JSON.parseObject(realResult);
        String obj = data.getJSONObject("data").getString("address");
        return obj;
    }


    public static String WALLET_TRANSFER(String address, String symbol, double value) {
        String result = HttpUtil.post(WALLET_TRANSFER_API + symbol + "/" + address + "/" + value, null);
        if (result == null || "".equals(result)) {
            logger.error("WALLET_TRANSFER fail:" + address + "/" + symbol + "/" + value);
            return "-1";
        }
        String realResult = result.substring(4, result.length());
        System.out.println(realResult);
        JSONObject data = JSON.parseObject(realResult);
        String obj = data.getString("code");
        return obj;
    }

    public static boolean WALLET_TRANSFER_FUNDCOLLECTION(String symbol) {
        String result = HttpUtil.post(WALLET_TRANSFER_FUNDCOLLECTION_API + symbol , null);
        String realResult = result.substring(4, result.length());
        System.out.println(realResult);
        JSONObject data = JSON.parseObject(realResult);
        int obj = data.getInteger("code");
        if (obj==200){
            return true;
        }
        return true;
    }


    public static String WALLET_RECHARGE(String symbol) {
        String result = HttpUtil.get(WALLET_RECHARGE_API + symbol);
        if (result == null || "".equals(result)) {
            logger.error("WALLET_RECHARGE fail:" + symbol + "/" + symbol);
            return "-1";
        }
        String realResult = result.substring(4, result.length());
        System.out.println(realResult);
        JSONObject data = JSON.parseObject(realResult);
        String obj = data.getJSONObject("data").getString("address");

        return obj;
    }



    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        int curHour24 = calendar.get(HOUR_OF_DAY);
        System.out.println(curHour24);

    }

}
