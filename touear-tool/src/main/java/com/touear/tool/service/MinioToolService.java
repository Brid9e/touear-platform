package com.touear.tool.service;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class MinioToolService {
    @Autowired
    private MinioClient minioClient;

    private static final String BUCKET_NAME = "your-bucket-name";
//
//    // 创建桶
//    public void createBucket() {
//        try {
//            boolean isExist = minioClient.bucketExists(BUCKET_NAME);
//            if (!isExist) {
//                minioClient.makeBucket(BUCKET_NAME);
//            }
//        } catch (MinioException e) {
//            throw new RuntimeException("Error creating bucket", e);
//        }
//    }
//
//    // 上传文件
//    public void uploadFile(String objectName, InputStream file, String contentType) {
//        try {
//            minioClient.putObject(BUCKET_NAME, objectName, file, contentType);
//        } catch (MinioException e) {
//            throw new RuntimeException("Error uploading file", e);
//        }
//    }
//
//    // 下载文件
//    public InputStream downloadFile(String objectName) {
//        try {
//            return minioClient.getObject(BUCKET_NAME, objectName);
//        } catch (MinioException e) {
//            throw new RuntimeException("Error downloading file", e);
//        }
//    }
//
//    // 列出所有桶
//    public List<Bucket> listBuckets() {
//        try {
//            return minioClient.listBuckets();
//        } catch (MinioException e) {
//            throw new RuntimeException("Error listing buckets", e);
//        }
//    }
}
