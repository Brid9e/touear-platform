package com.touear.gateway.util;


import com.touear.constants.CommonConstants;
import com.touear.core.tool.utils.DateUtil;
import com.touear.core.tool.utils.Func;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.Assert;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

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

/*    public static void main(String[] args) throws Exception {
        String clientSecret = "0osTIhce7uPvDKHz6aa67bhCukaKoYl4";
        //参数签名算法测试例子
        HashMap<String, String> signMap = new HashMap<String, String>();
        signMap.put("APP_ID", "1552274783265");
        signMap.put("SIGN_TYPE", SignType.SHA256.name());
        signMap.put("TIMESTAMP", DateUtil.getCurrentTimestampStr());
        signMap.put("NONCE",RandomValueUtils.randomAlphanumeric(16));
        String sign = SignatureUtils.getSign(signMap, clientSecret);
        System.out.println("签名结果:" + sign);
        signMap.put("SIGN", sign);
        System.out.println("签名参数:" + JSONObject.toJSONString(signMap));
        System.out.println(SignatureUtils.validateSign(signMap, clientSecret));
    }*/

    /**
     * 验证参数
     *
     * @param paramsMap
     * @throws Exception
     */
    public static void validateParams(Map<String, String> paramsMap) throws Exception {
        Assert.hasText(paramsMap.get(CommonConstants.SIGN_NONCE_KEY), "签名验证失败:NONCE不能为空");
        Assert.hasText(paramsMap.get(CommonConstants.SIGN_TIMESTAMP_KEY), "签名验证失败:TIMESTAMP不能为空");
        Assert.hasText(paramsMap.get(CommonConstants.SIGN_SIGN_TYPE_KEY), "签名验证失败:SIGN_TYPE不能为空");
        Assert.hasText(paramsMap.get(CommonConstants.SIGN_SIGN_KEY), "签名验证失败:SIGN不能为空");
        if (!SignType.contains(paramsMap.get(CommonConstants.SIGN_SIGN_TYPE_KEY))) {
            throw new IllegalArgumentException(String.format("签名验证失败:SIGN_TYPE必须为:%s,%s", SignType.MD5, SignType.SHA256));
        }
        try {
            DateUtils.parseDate(paramsMap.get(CommonConstants.SIGN_TIMESTAMP_KEY), "yyyyMMddHHmmss");
        } catch (ParseException e) {
            throw new IllegalArgumentException("签名验证失败:TIMESTAMP格式必须为:yyyyMMddHHmmss");
        }
        String timestamp = paramsMap.get(CommonConstants.SIGN_TIMESTAMP_KEY);
        Long clientTimestamp = Long.parseLong(timestamp);
        //判断时间戳 timestamp=201808091113
        if ((DateUtil.getCurrentTimestamp() - clientTimestamp) > MAX_EXPIRE) {
            throw new IllegalArgumentException("签名验证失败:TIMESTAMP已过期");
        }
    }

    /**
     * @param paramsMap     必须包含
     * @param clientSecret
     * @return
     */
    public static boolean validateSign(Map<String, String> paramsMap, String clientSecret) {
        try {
            validateParams(paramsMap);
            String sign = paramsMap.get(CommonConstants.SIGN_SIGN_KEY);
            //重新生成签名
            String signNew = getSign(paramsMap, clientSecret);
            //判断当前签名是否正确
            if (signNew.equals(sign)) {
                return true;
            }
        } catch (Exception e) {
            log.error("validateSign error:{}", e.getMessage());
            return false;
        }
        return false;
    }


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
        String signType = paramMap.get(CommonConstants.SIGN_SIGN_TYPE_KEY);
        SignType type = null;
        if (StringUtils.isNotBlank(signType)) {
            type = SignType.valueOf(signType);
        }
        if (type == null) {
            type = SignType.MD5;
        }
        for (String k : keyArray) {
            if (k.equals(CommonConstants.SIGN_SIGN_KEY) || k.equals(CommonConstants.SIGN_SECRET_KEY)) {
                continue;
            }
            if (Func.toStr(paramMap.get(k)).trim().length() > 0 && !Func.equals(paramMap.get(k), "NaN")) {
                // 参数值为空，则不参与签名
                sb.append(k).append("=").append(Func.toStr(paramMap.get(k)).trim()).append("&");
            }
        }
        //暂时不需要个人认证
        sb.append(CommonConstants.SIGN_SECRET_KEY+"=").append(clientSecret);
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
