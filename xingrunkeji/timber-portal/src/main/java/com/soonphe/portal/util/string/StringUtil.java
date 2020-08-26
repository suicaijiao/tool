package com.soonphe.portal.util.string;

import com.soonphe.portal.util.encrypt.EncryptUtil;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @Author：soonphe
 * @Date：2018-06-25 13:21
 * @Description：用户字符串操作
 */
public class StringUtil {

    /**
     * 数组翻转
     * @param Array
     * @return
     */
    public static List<String> reverseArray(String[] Array) {
        ArrayList<String> array_list = new ArrayList<String>();
        for (int i = 0; i < Array.length; i++) {
            array_list.add(Array[Array.length - i - 1]);
        }
        return array_list;
    }

    // 计算并获取md5值
    public static String getMD5(String requestBody) {
        return EncryptUtil.getMd5(requestBody);
    }

    public static String genSixteenUUId() {
        //最大支持1-9个集群机器部署
        int machineId = 1;
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        //有可能是负数
        if(hashCodeV < 0) {
            hashCodeV = - hashCodeV;
        }
        // 0 代表前面补充0
        // 4 代表长度为4
        // d 代表参数为正数型
        return machineId + String.format("%015d", hashCodeV);
    }

    public static void main(String[] args) {
        System.out.println(getMD5("123456"));
//        System.out.println(genSixteenUUId().toString());
//        System.out.println(buildRandom(6));
    }



}
