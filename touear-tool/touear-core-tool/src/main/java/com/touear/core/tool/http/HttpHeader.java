package com.touear.core.tool.http;

import com.touear.core.tool.utils.Func;
import com.touear.core.tool.utils.StringPool;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title: HttpRequestHeader
 * @Description: http请求中的header
 * @author wangqq
 * @date 2022-06-21 13:30:08
 * @version 1.0
 */
@Slf4j
public class HttpHeader implements Serializable {

    public static Map<String, String> getAllHeader(HttpRequestHeader requestHeader) {
        Map<String, String> definition = requestHeader.getDefinition();
        if (Func.isEmpty(definition)) {
            definition = new HashMap<>();
        }
        try {
            Map<String, Object> requestHeaderMap = Func.toMap(requestHeader);
            if (Func.isNotEmpty(requestHeader)) {
                for(Map.Entry<String, Object> entry : requestHeaderMap.entrySet()) {
                    String key = entry.getKey();
                    Object value = entry.getValue();
                    if (Func.isNotEmpty(value) && !"definition".equals(key)) {
                        key = getKey(entry.getKey());
                        if (value instanceof HttpEnums.ContentTypeEnum) {
                            HttpEnums.ContentTypeEnum obj = (HttpEnums.ContentTypeEnum) value;
                            value = obj.getCode();
                        } else if (value instanceof HttpEnums.CharsetEnum) {
                            HttpEnums.CharsetEnum obj = (HttpEnums.CharsetEnum) value;
                            value = obj.getCharset();
                        } else if (value instanceof Date) {
                            Date obj = (Date) value;
                            value = obj.getTime();
                        }
                        definition.put(key, Func.toStr(value));
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return definition;
    }

    @Data
    @Builder
    @Slf4j
    public static class HttpRequestHeader implements Serializable {

        /** 客户端希望接收的响应body 数据类型；示例： Accept: text/plain, text/html */
        private HttpEnums.ContentTypeEnum accept;

        /** 客户端希望接收的响应body 数据类型；示例： Accept-Charset: iso-8859-5 */
        private HttpEnums.CharsetEnum accept_charset;

        /** 客户端可以支持的返回内容压缩编码类型；示例： Accept-Encoding: compress, gzip */
        private String accept_encoding;

        /** 客户端可接受的语言；示例： Accept-Language: en,zh */
        private String accept_language;

        /** 可以请求网页实体的一个或者多个子范围字段；示例： Accept-Ranges: bytes */
        private String accept_ranges;

        /** HTTP授权的授权证书；示例： Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ== */
        private String authorization;

        /** 指定请求和响应遵循的缓存机制；示例： Cache-Control: no-cache */
        private String cache_control;

        /** 表示是否需要持久连接。（HTTP 1.1默认进行持久连接）；示例： Connection: close */
        private String connection;

        /** HTTP请求发送时，会把保存在该请求域名下的所有cookie值一起发送给web服务器；示例： Cookie: $Version=1; Skin=new; */
        private String cookie;

        /** 请求的内容长度；示例： Content-Length: 348 */
        private String content_length;

        /** 请求的与实体对应的MIME信息；示例： Content-Type: application/x-www-form-urlencoded */
        private HttpEnums.ContentTypeEnum content_type;

        /** 请求发送的日期和时间；示例：Date: Tue, 15 Nov 2010 08:12:31 GMT */
        private Date date;

        /** 请求的特定的服务器行为；示例：Expect: 100-continue */
        private String expect;

        /** 发出请求的用户的Email；示例：From: user@email.com */
        private String from;

        /** 指定请求的服务器的域名和端口号；示例：Host: www.baidu.com */
        private String host;

        /** 只有请求内容与实体相匹配才有效；示例：If-Match: “737060cd8c284d8af7ad3082f209582d” */
        private String if_match;

        /** 如果请求的部分在指定时间之后被修改则请求成功，未被修改则返回304代码；示例：If-Modified-Since: Sat, 29 Oct 2010 19:43:31 GMT */
        private String if_modified_since;

        /** 如果内容未改变返回304代码，参数为服务器先前发送的Etag，与服务器回应的Etag比较判断是否改变；示例：If-None-Match: “737060cd8c284d8af7ad3082f209582d” */
        private String if_none_match;

        /** 如果实体未改变，服务器发送客户端丢失的部分，否则发送整个实体，参数也为Etag；示例：If-Range: “737060cd8c284d8af7ad3082f209582d” */
        private String if_range;

        /** 只在实体在指定时间之后未被修改才请求成功；示例：If-Unmodified-Since: Sat, 29 Oct 2010 19:43:31 GMT */
        private String if_unmodified_since;

        /** 限制信息通过代理和网关传送的时间；示例：Max-Forwards: 10 */
        private String max_forwards;

        /** 用来包含实现特定的指令；示例：Pragma: no-cache */
        private String pragma;

        /** 连接到代理的授权证书；示例：Proxy-Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ== */
        private String proxy_authorization;

        /** 指请求实体的一部分，指定范围；示例：Range: bytes=500-999 */
        private String range;

        /** 先前网页的地址，当前请求网页紧随其后,即来路；示例：Referer: https://www.baidu.com/ */
        private String referer;

        /** 客户端愿意接受的传输编码，并通知服务器接受接受尾加头信息；示例：TE: trailers,deflate;q=0.5 */
        private String te;

        /** 向服务器指定某种传输协议以便服务器进行转换（如果支持）；示例：Upgrade: HTTP/2.0, SHTTP/1.3, IRC/6.9, RTA/x11 */
        private String upgrade;

        /** User-Agent的内容包含发出请求的用户信息；示例：User-Agent: Mozilla/5.0 (Linux; X11) */
        private String user_agent;

        /** 通知中间网关或代理服务器地址，通信协议；示例：Via: 1.0 fred, 1.1 nowhere.com (Apache/1.1) */
        private String via;

        /** 关于消息实体的警告信息；示例：Warn: 199 Miscellaneous warning */
        private String warning;

        private Map<String, String> definition;

        public HttpRequestHeader append(String key, String value) {
            if (Func.isNotBlank(key) && Func.isNotEmpty(value)) {
                if (Func.isEmpty(definition)) {
                    definition = new HashMap<>();
                }
                definition.put(key, value);
            }
            return this;
        }

        public Map<String, String> getDefinition() {
            return definition;
        }

    }

    @Data
    @Builder
    public static class HttpResponseHeader {

        /** 表明服务器是否支持指定范围请求及哪种类型的分段请求；示例： Accept-Ranges: bytes */
        private String accept_ranges;

        /** 从原始服务器到代理缓存形成的估算时间（以秒计，非负）；示例：Age: 12 */
        private String age;

        /** 对某网络资源的有效的请求行为，不允许则返回405；示例：Allow: GET, HEAD */
        private String allow;

        /** 告诉所有的缓存机制是否可以缓存及哪种类型；示例： Cache-Control: no-cache */
        private String cache_control;

        /** web服务器支持的返回内容压缩编码类型；示例： Content-Encoding: gzip */
        private String content_encoding;

        /** 响应体的语言；示例： Content-Language: en,zh */
        private String content_language;

        /** 响应内容长度；示例： Content-Length: 348 */
        private String content_length;

        /** 请求资源可替代的备用的另一地址；示例： Content-Location: /index.htm */
        private String content_location;

        /** 返回资源的MD5校验值；示例： Content-MD5: Q2hlY2sgSW50ZWdyaXR5IQ== */
        private String content_md5;

        /** 在整个返回体中本部分的字节位置；示例：Content-Range: bytes 21010-47021/47022 */
        private String content_range;

        /** 返回内容的MIME类型；示例：Content-Type: text/html; charset=utf-8 */
        private HttpEnums.ContentTypeEnum content_type;

        /** 请求发送的日期和时间；示例：Date: Tue, 15 Nov 2010 08:12:31 GMT */
        private Date date;

        /** 请求变量的实体标签的当前值；示例：ETag: “737060cd8c284d8af7ad3082f209582d” */
        private String etag;

        /** 响应过期的日期和时间；示例：Expires: Thu, 01 Dec 2010 16:00:00 GMT */
        private String expires;

        /** 请求资源的最后修改时间；示例：Last-Modified: Tue, 15 Nov 2010 12:45:26 GMT */
        private String last_modified;

        /** 用来重定向接收方到非请求URL的位置来完成请求或标识新的资源；示例：Location: https://www.baidu.com/ */
        private String location;

        /** 包括实现特定的指令，它可应用到响应链上的任何接收方；示例：Pragma: no-cache */
        private String pragma;

        /** 它指出认证方案和可应用到代理的该URL上的参数；示例：Proxy-Authorization: Basic */
        private String proxy_authorization;

        /** 应用于重定向或一个新的资源被创造，在5秒之后重定向（由网景提出，被大部分浏览器支持）；示例：Refresh: 5; url=https://www.baidu.com/ */
        private String refresh;

        /** 如果实体暂时不可取，通知客户端在指定时间之后再次尝试；示例：Retry-After: 120 */
        private String retry_after;

        /** web服务器软件名称；示例：Server: Apache/1.3.27 (Unix) (Red-Hat/Linux) */
        private String server;

        /** 设置Http Cookie；示例：Set-Cookie: UserID=JohnDoe; Max-Age=3600; Version=1 */
        private String set_cookie;

        /** 指出头域在分块传输编码的尾部存在；示例：Trailer: Max-Forwards */
        private String trailer;

        /** 文件传输编码；示例：Transfer-Encoding:chunked */
        private String transfer_encoding;

        /** 告诉下游代理是使用缓存响应还是从原始服务器请求；示例：Vary: */
        private String vary;

        /** 告知代理客户端响应是通过哪里发送的；示例：Via: 1.0 fred, 1.1 nowhere.com (Apache/1.1) */
        private String via;

        /** 警告实体可能存在的问题；示例：Warn: 199 Miscellaneous warning */
        private String warning;

        /** 表明客户端请求实体应该使用的授权方案；示例：WWW-Authenticate: Basic */
        private String www_authenticate;
    }

    private static String getKey(String key) {
        if (key.contains(StringPool.UNDERSCORE)) {
            String[] s = key.split(StringPool.UNDERSCORE);
            StringBuilder sb = new StringBuilder();
            for (String s1 : s) {
                if ("www".equals(s1)) {
                    sb.append(s1.toUpperCase());
                } else {
                    sb.append(Func.firstCharToUpper(s1));
                }
                sb.append(StringPool.DASH);
            }
            key = sb.substring(0, sb.length() - 1);
        }
        return key;
    }

}
