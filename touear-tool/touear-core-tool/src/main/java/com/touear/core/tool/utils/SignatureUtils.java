package com.touear.core.tool.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;



import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @Title: SignatureUtils.java
 * @Description: 签名
 * @author chenl
 * @date 2020-07-01 10:59:07
 * @version 1.0
 */
@Slf4j
public class SignatureUtils {

    /**
     * 5分钟有效期
     */
    private final static long MAX_EXPIRE = 5 * 60;

    /**
     * 客户端ID KEY
     */
    public static final String SIGN_APP_ID_KEY = "APP_ID";

    /**
     * 客户端秘钥 KEY
     */
    public static final String SIGN_SECRET_KEY = "SECRET_KEY";

    /**
     * 随机字符串 KEY
     */
    public static final String SIGN_NONCE_KEY = "NONCE";
    /**
     * 时间戳 KEY yyyyMMddhhmmss
     */
    public static final String SIGN_TIMESTAMP_KEY = "TIMESTAMP";
    /**
     * 签名类型 KEY
     */
    public static final String SIGN_SIGN_TYPE_KEY = "SIGN_TYPE";
    /**
     * 签名结果 KEY
     */
    public static final String SIGN_SIGN_KEY = "SIGN";


    /**
     * 得到签名
     *
     * @param paramMap     参数集合不含appSecret
     *                     必须包含appId=客户端ID
     *                     signType = SHA256|MD5 签名方式
     *                     timestamp=时间戳
     *                     nonce=随机字符串
     * @param clientSecret 验证接口的clientSecret
     * @return
     */
    public static String getSign(Map<String, String> paramMap, String clientSecret) {
        if (paramMap == null) {
            return "";
        }
        //排序
        Set<String> keySet = paramMap.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        String signType = paramMap.get(SIGN_SIGN_TYPE_KEY);
        SignType type = null;
        if (StringUtil.isNotBlank(signType)) {
            type = SignType.valueOf(signType);
        }
        if (type == null) {
            type = SignType.MD5;
        }
        for (String k : keyArray) {
            if (k.equals(SIGN_SIGN_KEY) || k.equals(SIGN_SECRET_KEY)) {
                continue;
            }
            if (paramMap.get(k).trim().length() > 0 && !Func.equals(paramMap.get(k), "NaN")) {
                // 参数值为空，则不参与签名
                sb.append(k).append("=").append(paramMap.get(k).trim()).append("&");
            }
        }
        //暂时不需要个人认证
        sb.append(SIGN_SECRET_KEY+"=").append(clientSecret);
        String signStr = "";
        //加密
        switch (type) {
            case MD5:
                signStr = EncryptUtils.md5Hex(sb.toString()).toUpperCase();
                break;
            case SHA256:
                signStr = EncryptUtils.sha256Hex(sb.toString()).toUpperCase();
                break;
            default:
                break;
        }
        return signStr;
    }


    public enum SignType {
        MD5,
        SHA256;

        public static boolean contains(String type) {
            for (SignType typeEnum : SignType.values()) {
                if (typeEnum.name().equals(type)) {
                    return true;
                }
            }
            return false;
        }
    }

}
