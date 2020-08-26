package com.soonphe.portal.util.rate;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soonphe.portal.util.http.HttpUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author：soonphe
 * @Date：2017-09-22 11:38
 * @Comments：汇率接口工具类
 */
public class RateApiUtil {


    public static final String RATE_ALL_API =
            "https://fxhapi.feixiaohao.com/public/v1/ticker?convert=CNY&limit=10&start=2";


    public static final String RATE_UPDATE_API =
            "https://app-testnet.doschain.org/exchange/setlimitprice";

    /**
     * 获取所有
     *
     * @return
     */
    public static List<CurrencyRate> GET_ALL_RATE_API() {
        String result = HttpUtil.httpsGet(RATE_ALL_API);
        //获取不到结果返回默认7.06
        if (result==null||"".equals(result)){
            List<CurrencyRate> list = new ArrayList<>();
            CurrencyRate currencyRate = new CurrencyRate();
            currencyRate.setId("tether");
            currencyRate.setPrice_cny(7.06);
            list.add(currencyRate);
            return list;
        }
        String realResult = result.substring(4,result.length());
        System.out.println(result);
        List<CurrencyRate> trxToCNYRate = JSONObject.parseArray(realResult, CurrencyRate.class);
        return trxToCNYRate;
    }

    /**
     * 获取tron
     *
     * @return
     */
    public static CurrencyRate GET_TRON_RATE_API() {
        List<CurrencyRate> list = GET_ALL_RATE_API();
        for (CurrencyRate currencyRate:list){
//            if ("tron".equals(currencyRate.getId())){
            if ("tether".equals(currencyRate.getId())){
                return currencyRate;
            }
        }
        return null;
    }


    /**
     * 更新交易所价格
     *
     * @return
     */
    public static CurrencyRate RATE_UPDATE(double price) {
        RateUpdate rateUpdate=  new RateUpdate();
        rateUpdate.setPrice(price);
//        String result = HttpUtil.postJson(RATE_UPDATE_API, "{\"symbol\":\"dotdosusdt\",\"token\":\"9900455D4E545996CF443EFF355BFCFF2C7A4A83\",\"price\":1}");
        String param = JSONObject.toJSONString(rateUpdate);
        System.out.println(param);
        String result = HttpUtil.postJson(RATE_UPDATE_API, param);
        System.out.println(result+"");
//        String realResult = result.substring(4, result.length());
//        System.out.println(realResult);
//        JSONObject data = JSON.parseObject(realResult);
//        String obj = data.getJSONObject("data").getString("address");
        return null;
    }

    public static void main(String[] args) {
//        System.out.println("汇率list大小:"+GET_ALL_RATE_API().size());
//        System.out.println(new BigDecimal(0.975 * 15));
        RATE_UPDATE(1);
    }

}
