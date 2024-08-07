package com.touear.core.tool.utils;

import com.google.common.base.Strings;


/**
 * 数据脱敏
 */
public class DataMaskingUtils{


    /**
     * 姓名脱敏 赵**
     * @param fullName 姓名
     */


    /**
     * 敏感信息脱敏(数字字母类，不支持中文)
     *
     * @param head         头显示位数
     * @param tail         尾显示位数
     * @param sensitiveStr 敏感信息字符串
     * @return 脱敏后的字符串
     */
    public static String formatToMask(int head, int tail, String sensitiveStr) {
        if (sensitiveStr == null || sensitiveStr.isEmpty()) {
            return "";
        }
        if (sensitiveStr.length() < head + tail) {
            return sensitiveStr;
        }
        StringBuffer maskStr = new StringBuffer("$1");
        for (int i = sensitiveStr.length() - head - tail; i > 0; i--) {
            maskStr.append("*");
        }
        maskStr.append("$3");
        String regex = "(\\w{" + head + "})(\\w+)(\\w{" + tail + "})";
        return sensitiveStr.replaceAll(regex, maskStr.toString());
    }

}