package com.touear.manage.service.impl;

import com.touear.common.utils.BASE64DecodedMultipartFile;
import com.touear.core.oss.model.TouearFile;
import com.touear.core.oss.props.OssProperties;
import com.touear.core.tool.utils.Func;
import com.touear.manage.service.BaseFileService;
import com.touear.starter.minio.MinioTemplate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yang
 * @version 1.0
 * @Title: BaseFileServiceImpl.java
 * @Description:  文件管理
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class BaseFileServiceImpl implements BaseFileService {

    private final MinioTemplate minioTemplate;
    private final OssProperties ossProperties;

    @Override
    public String upload(String bucketName, MultipartFile multipartFile) {
        try {
            TouearFile bfile = minioTemplate.putFile(bucketName, multipartFile.getOriginalFilename(), multipartFile.getInputStream());
            String link = bfile.getLink();
            if (Func.isNotBlank(link)) {
                // 去掉ip和端口
                link = link.replace(ossProperties.getEndpoint(), "");
                return link;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("上传文件错误（upload）：{}", e);
            return null;
        }
    }

    @Override
    public List<String> upload(String bucketName, MultipartFile[] multipartFiles) {
        if (multipartFiles != null && multipartFiles.length > 0) {
            List<String> linkList = new ArrayList<String>();
            for (MultipartFile multipartFile : multipartFiles) {
                String link = upload(bucketName, multipartFile);
                if (Func.isNotBlank(link)) {
                    linkList.add(link);
                } else {
                    return linkList;
                }
            }
            if (linkList.size() > 0) {
                return linkList;
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public String uploadBase64(String bucketName, String base64) {
        if (Func.isNotBlank(base64)) {
            MultipartFile base64ToMultipart = BASE64DecodedMultipartFile.base64ToMultipart(base64);
            if (base64ToMultipart != null) {
                TouearFile bfile;
                try {
                    bfile = minioTemplate.putFile(bucketName, base64ToMultipart.getOriginalFilename(), base64ToMultipart.getInputStream());
                    String link = bfile.getLink();
                    if (Func.isNotBlank(link)) {
                        // 用于去掉ip端口
                        link = link.replace(ossProperties.getEndpoint(), "");
                        return link;
                    }
                } catch (IOException e) {
                    log.error("上传Base64文件错误（uploadBase64）：{}", e);
                }
            }
        }
        return null;
    }

    @Override
    public Boolean removeFile(String bucketName, String fileUrl) {
        try {
            minioTemplate.removeFile(bucketName, fileUrl);
            return true;
        } catch (Exception e) {
            log.error("移除文件错误（removeFile）：{}", e);
            return false;
        }
    }

    /**
     * 批量删除文件夹下的文件
     *
     * @param bucketName 桶名
     * @param prefix     文件夹名
     * @return 删除标识
     * @author gaoziming
     */
    @Override
    public Boolean removeFiles(String bucketName, String prefix) {
        try {
            return minioTemplate.removeFiles(bucketName, prefix);
        } catch (Exception e) {
            log.error("批量移除文件错误（removeFiles）：{}", e);
            return false;
        }
    }

    /**
     * 单个文件的拷贝
     *
     * @param bucketName 桶名称
     * @param originName 源文件路径
     * @param targetName 目标文件路径
     * @return 拷贝标识
     * @author gaoZiMing
     */
    @Override
    public Boolean copyFile(String bucketName, String originName, String targetName) {
        try (InputStream inputStream = minioTemplate.getMinIoObject(bucketName, originName)) {
             minioTemplate.putFileWithOriginName(bucketName, targetName, inputStream);
             return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public String coverUpload(String bucketName, String fileName, String oldName, InputStream stream) {
        String link = null;
        TouearFile bfile = new TouearFile();
        try {
            if(Func.isNotBlank(oldName)) {
                TouearFile putFileWithOriginName = minioTemplate.putFileWithOriginName(bucketName, oldName.replace("/" + bucketName + "/", ""), stream);
                link = oldName;
            }else {
                bfile = minioTemplate.putFile(bucketName, fileName, stream);
                link = bfile.getLink();
                if (Func.isNotBlank(link)) {
                    // 去掉ip和端口
                    link = link.replace(ossProperties.getEndpoint(), "");
                }
            }
        } catch (Exception e) {
            log.error("上传文件错误（upload2）：{}", e);
        }
        return link;
    }

    @Override
    public String upload(String bucketName, String fileName, InputStream stream) {
        try {
            TouearFile bfile = minioTemplate.putFile(bucketName, fileName, stream);
            String link = bfile.getLink();
            if (Func.isNotBlank(link)) {
                // 去掉ip和端口
                link = link.replace(ossProperties.getEndpoint(), "");
                return link;
            } else {
                return null;
            }
        } catch (Exception e) {
            log.error("上传文件错误（upload2）：{}", e);
            return null;
        }
    }



}
