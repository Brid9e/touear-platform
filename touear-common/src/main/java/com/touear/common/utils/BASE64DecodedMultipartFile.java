package com.touear.common.utils;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.*;

/**
 * @Title: BASE64DecodedMultipartFile.java
 * @Description: base64转Multipart工具类
 * @author gaohaidong
 * @date 2020年4月16日 下午2:59:12
 * @version 1.0
 */
public class BASE64DecodedMultipartFile implements MultipartFile {
  
    private final byte[] imgContent;
    private final String header;
  
    public BASE64DecodedMultipartFile(byte[] imgContent, String header) {
        this.imgContent = imgContent;
        this.header = header.split(";")[0];
    }
  
    @Override
    public String getName() {
        return System.currentTimeMillis() + Math.random() + "." + header.split("/")[1];
    }
  
    @Override
    public String getOriginalFilename() {
        return System.currentTimeMillis() + (int) Math.random() * 10000 + "." + header.split("/")[1];
    }
  
    @Override
    public String getContentType() {
        return header.split(":")[1];
    }
  
    @Override
    public boolean isEmpty() {
        return imgContent == null || imgContent.length == 0;
    }
  
    @Override
    public long getSize() {
        return imgContent.length;
    }
  
    @Override
    public byte[] getBytes() throws IOException {
        return imgContent;
    }
  
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(imgContent);
    }
  
    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        new FileOutputStream(dest).write(imgContent);
    }
  
    /**
     * base64文件转Multipart
     * @param base64
     * @return
     * @author Administrator
     * @date 2020-04-16 14:58:39
     */
    public static MultipartFile base64ToMultipart(String base64) {
        try {
            String[] baseStrs = base64.split(",");
  
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = new byte[0];
            b = decoder.decodeBuffer(baseStrs[1]);
  
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            return new BASE64DecodedMultipartFile(b, baseStrs[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

  
}