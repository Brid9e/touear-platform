package com.touear.core.tool.http;

import com.touear.core.tool.utils.Func;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

/**
 * @Title: HttpClientUtil.java
 * @Description: httpClient新建连接
 * @author touear
 * @date 2018年10月10日 下午4:15:22
 * @version 1.2
 */
@Slf4j
public class HttpClientUtil {

    /**
     * http协议中的post请求
     *
     * @param url      访问地址
     * @param paramMap 访问参数
     * @param charset  编码
     * @param header   header参数
     * @return {@link String}请求响应结果
     */
    public static String httpPost(String url, Map<String, Object> paramMap, HttpEnums.CharsetEnum charset, HttpHeader.HttpRequestHeader header) throws Exception {
        return commonPostClient(url, paramMap, charset, getDefaultHttpClient(), header);
    }

    /**
     * http协议中的post请求
     *
     * @param url      访问地址
     * @param paramMap 访问参数
     * @param charset  编码
     * @return {@link String}请求响应结果
     */
    public static String httpPost(String url, Map<String, Object> paramMap, HttpEnums.CharsetEnum charset) throws Exception {
        return commonPostClient(url, paramMap, charset, getDefaultHttpClient(), null);
    }


    /**
     * http协议中的get请求
     *
     * @param url      访问地址
     * @param paramMap 访问参数
     * @param charset  编码
     * @return {@link String}请求响应结果
     * @author touear
     * @date 2018年10月9日 下午4:27:49
     */
    public static String httpGet(String url, Map<String, String> paramMap, HttpEnums.CharsetEnum charset, HttpHeader.HttpRequestHeader header) throws Exception {
        return commonGetClient(url, paramMap, charset, getDefaultHttpClient(), header);
    }

    /**
     * http协议中的get请求
     *
     * @param url      访问地址
     * @param paramMap 访问参数
     * @param charset  编码
     * @return {@link String}请求响应结果
     * @author touear
     * @date 2018年10月9日 下午4:27:49
     */
    public static String httpGet(String url, Map<String, String> paramMap, HttpEnums.CharsetEnum charset) throws Exception {
        return commonGetClient(url, paramMap, charset, getDefaultHttpClient(), null);
    }

    /**
     * 获取默认的httpClient对象
     *
     * @return {@link CloseableHttpClient}
     * @author xuegang
     * @date 2021-07-14 16:09:15
     */
    private static CloseableHttpClient getDefaultHttpClient() {
        final PoolingHttpClientConnectionManager cManager = new PoolingHttpClientConnectionManager();
        cManager.setMaxTotal(50);
        cManager.setDefaultMaxPerRoute(50);
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(10000)
                .setConnectTimeout(10000)
                .setConnectionRequestTimeout(10000)
                .build();
        return HttpClients.custom()
                .setConnectionTimeToLive(10L, TimeUnit.SECONDS)
                .setDefaultRequestConfig(defaultRequestConfig)
                .setConnectionManager(cManager)
                .build();
    }

    /**
     * http与https通用的post请求
     *
     * @param url           访问地址
     * @param params        请求参数
     * @param charset       请求编码【参数和响应值】
     * @param httpclient    请求对象
     * @param header        请求header
     * @author touear
     * @date 2018年10月9日 下午4:42:59
     */
    private static String commonPostClient(String url, Map<String, Object> params, HttpEnums.CharsetEnum charset, CloseableHttpClient httpclient, HttpHeader.HttpRequestHeader header) throws Exception {
        HttpPost httpPost;
        CloseableHttpResponse response = null;
        String result = null;
        try {
            // 创建post方式请求对象
            httpPost = new HttpPost(url);
            // 封装参数
            if (params != null && params.size() != 0) {
                if (header != null && HttpEnums.ContentTypeEnum.APPLICATION_X_WWW_FORM_URLENCODED.equals(header.getContent_type())) {
                    //创建参数集合
                    List<BasicNameValuePair> list = new ArrayList<>();
                    //添加参数
                    for (Entry<String, Object> entry : params.entrySet()) {
                        list.add(new BasicNameValuePair(entry.getKey(), Func.toStr(entry.getValue())));
                    }
                    //把参数放入请求对象，，post发送的参数list，指定格式
                    httpPost.setEntity(new UrlEncodedFormEntity(list, charset.getCharset()));
                } else {
                    httpPost.setEntity(new StringEntity(Func.toJson(params), charset.getCharset()));
                }
            }

            // 设置请求头
            if (header != null) {
                Map<String, String> headers = HttpHeader.getAllHeader(header);
                if (Func.isNotEmpty(headers)) {
                    for (Entry<String, String> entry : headers.entrySet()) {
                        httpPost.addHeader(entry.getKey(), entry.getValue());
                    }
                }
            }

            // 执行请求操作，并获取请求结果
            response = httpclient.execute(httpPost);
            // 获取状态码进行验证
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, charset.getCharset());
            }
            if (HttpStatus.SC_OK == statusCode) {
                EntityUtils.consume(entity);
            } else {
                throw new Exception("post请求失败，错误码为：" + statusCode);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpclient != null) {
                httpclient.close();
            }
        }
        return result;
    }


    /**
     * http与https通用的get请求
     *
     * @param url           访问地址
     * @param paramMap      请求参数
     * @param charset       请求编码【参数和响应值】
     * @param httpclient    请求对象
     * @param header        请求header
     * @author touear
     * @date 2018年10月9日 下午4:42:59
     */
    private static String commonGetClient(String url, Map<String, String> paramMap, HttpEnums.CharsetEnum charset, CloseableHttpClient httpclient, HttpHeader.HttpRequestHeader header) throws Exception {
        HttpGet httpGet;
        CloseableHttpResponse response = null;
        String result = null;
        try {
            // 将路径与参数拼接
            URIBuilder uriBuilder = new URIBuilder(url);
            // 封装参数
            List<NameValuePair> nvps = new ArrayList<>();
            if (paramMap != null) {
                for (Entry<String, String> entry : paramMap.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                uriBuilder.addParameters(nvps);
            }

            // 创建get方式请求对象
            httpGet = new HttpGet(uriBuilder.build());
            // 设置请求头【有不同的需求的话，可改动】
            if (header != null) {
                Map<String, String> headers = HttpHeader.getAllHeader(header);
                if (Func.isNotEmpty(headers)) {
                    for (Entry<String, String> entry : headers.entrySet()) {
                        httpGet.addHeader(entry.getKey(), entry.getValue());
                    }
                }
            }

            // 执行请求操作，并获取请求结果
            response = httpclient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK == statusCode) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity, charset.getCharset());
                }
                EntityUtils.consume(entity);
            } else {
                throw new Exception("get请求失败，错误码为：" + statusCode);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpclient != null) {
                httpclient.close();
            }
        }
        return result;
    }

}
