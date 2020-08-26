
package com.soonphe.portal.commons.util;

import com.soonphe.portal.commons.golbal.Const;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class StringUtil {


    //region 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回

    /**
     * 将元数据前补零，补后的总长度为指定的长度，以字符串的形式返回
     *
     * @param sourceDate
     * @param formatLength
     * @return重组后的数据
     */
    public static String frontCompWithZore(int sourceDate, int formatLength) {
        // 0 指前面补充零  formatLength 字符总长度为 formatLength  d 代表为正数。
        String newString = String.format("%0" + formatLength + "d", sourceDate);
        return newString;
    }

    //endregion

    //region 根据字符串统计字符长度

    /**
     * 根据字符串统计字符长度
     *
     * @param value 字符串
     * @return int
     */
    public static int stringLength(String value) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }

    //endregion

    //region 字符串是否为空

    public static boolean isNullOrWhiteSpace(String str) {
        if (str == null)
            return true;
        if (str.trim().length() == 0)
            return true;
        return false;
    }

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }
    //endregion

    //region UUID

    /**
     * 生成32位的随机字符串
     *
     * @return 32位UUID
     */
    public static String UUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成36位的全小写的随机字符串
     *
     * @return 36位GUID
     */
    public static String GUID() {
        return UUID.randomUUID().toString().toLowerCase();
    }
    //endregion




    /**
     *
     * 根据传递的区间返回随机值
     *
     * @param max
     * @param min
     * @return
     */
    public static String getRandom(int max, int min) {
        String value = "";
        if (max > min) {
            Random random = new Random();
            int s = random.nextInt(max) % (max - min + 1) + min;
            value = String.valueOf(s);
        }
        return value;
    }

        /**
         * 把任何大于0的int转换成2的n次方之和，返回和的集合
         *
         * @param no
         * @return
         */
        public static List<Integer> getParsedSum(int no) {
            if (no <= 0) {
                return null;
            }
            String str = Integer.toBinaryString(no);
            List<Integer> list = new ArrayList<Integer>();
            for (int i = 0, len = str.length(); i < len; i++) {
                if (str.charAt(i) == '1') {
                    list.add(1 << (len - i - 1));
                }
            }
            return list;
        }

    /**
     * 把任何大于0的int转换成2的n次方之和，返回和的集合
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 截取字符串
     *
     * @param src
     * @param start_idx
     * @param end_idx
     *
     * @return
     */
    public static String bytesSubstring(String src, int start_idx, int end_idx) {
        byte[] b = src.getBytes();
        String tgt = "";
        // 循环截取从start_idx到end_idx之间的字符，转存在tgt中
        for (int i = start_idx; i <= end_idx; i++) {
            tgt += (char) b[i];
        }
        return tgt;
    }



    /**
     * 拼接连接字符串
     *
     * @param strUrl
     * @param mapParam
     * @return 完整带参数的请求地址
     */
    public static String makeFullUrl(String strUrl, Map<String,String> mapParam) {
        // 完整地址
        StringBuffer sbFullUrl = new StringBuffer();
        sbFullUrl.append(strUrl);

        List<String> keys = new ArrayList<>(mapParam.keySet());
        // 排序
        Collections.sort(keys);

        if(keys.size() > 0){
            // 拼接？
            sbFullUrl.append(Const.STRING_QUESTION);

            // 循环拼接
            for (int i = 0; i < keys.size(); i++) {
                // 取得key
                String key = keys.get(i);
                // 拼接key=value
                sbFullUrl.append(key).append(Const.STRING_EQUAL).append(mapParam.get(key));
                // 如果不是最后一项，则拼接&符
                if(i != keys.size()-1){
                    sbFullUrl.append(Const.STRING_AND);
                }
            }
        }
        return sbFullUrl.toString();
    }


    /**
     * 去除守尾任意字符串
     * @param strValues
     * @param trimValue
     * @return
     */
    public static String Rtrim(String strValues,char trimValue) {
        int len = strValues.length();
        int st = 0;
        char[] val = strValues.toCharArray();    /* avoid getfield opcode */

        while ((st < len) && (val[st] <= trimValue)) {
            st++;
        }
        while ((st < len) && (val[len - 1] <= trimValue)) {
            len--;
        }
        return ((st > 0) || (len < strValues.length())) ? strValues.substring(st, len) : strValues;
    }

    /**
     *  生成四位随机字符
     * @param length
     * @return
     */
    public static String getRandomUpperString(int length){
        String randomString = "";
        for (int i=0;i<length;i++){
            char c=(char)(int)(Math.random()*26+97);
            randomString+=c;
        }
        return randomString.toUpperCase();
    }

    /**
     *  字符转化为大写
     * @param param
     * @return
     */
    public static String toUpperCase(Object param){
        if(null!=param){
            return param.toString().toUpperCase();
        }else{
            return "";
        }
    }

    /**
     * 用于Redis缓存有效时间设置到第二天凌晨的秒数
     * @param currentDate
     * @return
     */
    public static Integer getRemainSecondsOneDay(Date currentDate) {
        //使用plusDays加传入的时间加1天，将时分秒设置成0
        LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
                .withSecond(0).withNano(0);
        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(),
                ZoneId.systemDefault());
        //使用ChronoUnit.SECONDS.between方法，传入两个LocalDateTime对象即可得到相差的秒数
        long seconds = ChronoUnit.SECONDS.between(currentDateTime, midnight);
        return (int) seconds;
    }

    /**
     * 用于Redis缓存有效时间设置到第二天凌晨的秒数
     * @return
     */
    public static Long getSecondsNextEarlyMorning() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        // 改成这样就好了
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }


    public static void main(String[] args) {
        System.out.println(getRemainSecondsOneDay(new Date()));
        System.out.println(getSecondsNextEarlyMorning());
    }

}