package com.touear.core.tool.utils;

import com.touear.core.tool.exception.ServiceException;
import com.touear.core.tool.jackson.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: UrlParamsUtil
 * @Description: url参数工具类
 * @author wangqq
 * @date 2023-08-01 14:20:29
 * @version 1.0
 */
@Slf4j
public class UrlParamsUtil {

    /**
     * 解析url自带的参数，并对参数进行url解码操作
     *
     * @param urlString     带参数的url地址
     * @return {@link Map < String, String>}     url自带的参数键值对，已进行url解码
     * @author wangqq
     * @date 2023-07-28 15:45:54
     */
    public static Map<String, String> parseUrlParams(String urlString) {
        try {
            Map<String, String> parameters = new HashMap<>();
            URL url = new URL(urlString);
            String query = url.getQuery();
            if (query != null) {
                String[] pairs = query.split(StringPool.AMPERSAND);
                for (String pair : pairs) {
                    int idx = pair.indexOf(StringPool.EQUALS);
                    String key = UrlUtil.decode(pair.substring(0, idx), StringPool.UTF_8);
                    String value = UrlUtil.decode(pair.substring(idx + 1), StringPool.UTF_8);
                    parameters.put(key, value);
                }
            }
            return parameters;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        throw new ServiceException("格式化地址失败，详细地址为：" + urlString);
    }

    /**
     * 根据url以及参数集合，生成一个自带参数的url地址，参数值会自动url编码
     *
     * @param urlString     原始的url地址
     * @param parameters    url地址后的参数
     * @return {@link String}   自带参数的url地址
     * @author wangqq
     * @date 2023-07-28 15:47:21
     */
    public static String createUrlByParams(String urlString, Map<String, String> parameters) {
        try {
            URL url = new URL(urlString);
            URIBuilder uriBuilder = new URIBuilder()
                    .setScheme(url.getProtocol())
                    .setHost(url.getHost())
                    .setPort(url.getPort())
                    .setPath(url.getPath());
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                uriBuilder.setParameter(entry.getKey(), entry.getValue());
            }
            URI build = uriBuilder.build();
            return build.toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        throw new ServiceException("创建地址失败，详细地址为：" + urlString + ", 参数为：" + JsonUtil.toJson(parameters));
    }

}
