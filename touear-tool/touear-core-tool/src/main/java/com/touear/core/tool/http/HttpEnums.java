package com.touear.core.tool.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Title: HttpEnums
 * @Description: TODO
 * @author wangqq
 * @date 2022-06-21 15:32:47
 * @version 1.0
 */
public class HttpEnums {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum ContentTypeEnum {

        TEXT_HTML("text/html", "HTML格式"),
        TEXT_PLAIN("text/plain", "纯文本格式"),
        TEXT_XML("text/xml", "XML格式"),
        IMAGE_GIF("image/gif", "gif图片格式"),
        IMAGE_JPEG("image/jpeg", "jpg图片格式"),
        IMAGE_PNG("image/png", "png图片格式"),
        VIDEO_MPEG("video/mpeg", "视频"),
        VIDEO_QUICKTIME("vedio/quicktime", "视频"),
        APPLICATION_XHTML_XML("application/xhtml+xml", "XHTML格式"),
        APPLICATION_XML("application/xml", "XML数据格式"),
        APPLICATION_ATOM_XML("application/atom+xml", "Atom XML聚合格式"),
        APPLICATION_JSON("application/json", "JSON数据格式"),
        APPLICATION_PDF("application/pdf", "pdf格式"),
        APPLICATION_MSWORD("application/msword", "Word文档格式"),
        APPLICATION_OCTET_STREAM("application/octet-stream", "二进制流数据（如常见的文件下载）"),
        APPLICATION_X_WWW_FORM_URLENCODED("application/x-www-form-urlencoded", "< form encType=””>中默认的encType，form表单数据被编码为key/value格式发送到服务器（表单默认的提交数据的格式）");

        private String code;

        private String describe;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum CharsetEnum {

        US_ASCII("US-ASCII"),
        ISO_8859_1("ISO-8859-1"),
        UTF_8("UTF-8"),
        UTF_16BE("UTF-16BE"),
        UTF_16LE("UTF-16LE"),
        UTF_16("UTF-16");

        private String charset;

    }

}
