package com.touear.common.utils;

import com.touear.common.enums.FileType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class FileUploadValidator {

    // 文件名格式要求（要求只允许：中文、字母、数字、空格、点、下划线、中划线）
    private static final String FILENAME_PATTERN = "[\\u4e00-\\u9fa5a-zA-Z0-9 ._-]+";
    // 默认文件大小最大限制1MB
    private static final long DEFAULT_MAX_FILE_SIZE = 1 * 1024 * 1024;

    /**
     * 文件校验
     *
     * @param file                  文件
     * @param allowedFileExtensions 允许文件的拓展类型（从FileType枚举类中获取，放入到集合中）如果为null，则根据FileType枚举类中是否包含文件拓展类型
     * @param maxFileSize           文件大小最大限制，如果为null，根据DEFAULT_MAX_FILE_SIZE默认文件大小最大限制进行判断
     * @return 如果文件有效返回true，否则返回false
     */
    public static boolean validFile(MultipartFile file, Set<String> allowedFileExtensions, Long maxFileSize) {
        if (file == null || file.isEmpty()) {
            log.warn("File is null or empty");
            return false;
        }

        // 检查allowedFileExtensions集合中的所有扩展名是否都在FileType枚举中
        if (allowedFileExtensions != null && !areValidExtensions(allowedFileExtensions)) {
            log.warn("Invalid allowed file extensions");
            return false;
        }

        // 检查文件名是否合法
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.matches(FILENAME_PATTERN)) {
            log.warn("Invalid file name: {}", originalFilename);
            return false;
        }

        // 检查文件大小
        long fileSize = file.getSize();
        long allowedMaxFileSize = (maxFileSize != null) ? maxFileSize : DEFAULT_MAX_FILE_SIZE;
        if (fileSize > allowedMaxFileSize) {
            log.warn("File size exceeds the maximum limit. Size: {} bytes, Max: {} bytes", fileSize, allowedMaxFileSize );
            return false;
        }

        // 检查文件拓展类型
        String fileExtension = getFileExtension(originalFilename);
        if (!((allowedFileExtensions != null && allowedFileExtensions.contains(fileExtension)) || FileType.isValidExtension(fileExtension))) {
            log.warn("Unsupported file extension: {}", fileExtension);
            return false;
        }

        // 如果是zip压缩类型，检查压缩包中的所有文件
        if (FileType.ZIP.getExtension().equals(fileExtension)) {
            // zip压缩文件中不允许包含压缩文件
            allowedFileExtensions.remove(FileType.ZIP.getExtension());
            return isValidZipFile(file, allowedFileExtensions);
        }

        return true;
    }

    /**
     * 校验ZIP文件的内容
     *
     * @param file 要校验的ZIP文件
     * @return 如果ZIP文件及其内容有效返回true，否则返回false
     */
    private static boolean isValidZipFile(MultipartFile file, Set<String> allowedFileExtensions) {
        try (ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream())) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }

                String entryName = getFileName(entry.getName());
                if (!entryName.matches(FILENAME_PATTERN)) {
                    log.warn("Invalid file name in ZIP: {}", entry.getName());
                    return false;
                }

                // 检查是否有嵌套的ZIP文件
                String entryExtension = getFileExtension(entryName);
                if (FileType.ZIP.getExtension().equalsIgnoreCase(entryExtension)) {
                    log.warn("Nested ZIP files are not allowed: {}", entry.getName());
                    return false;
                }

                // 校验ZIP文件中的每个文件类型
                if (!allowedFileExtensions.contains(entryExtension)) {
                    log.warn("Unsupported file type in ZIP: {}", entry.getName());
                    return false;
                }
            }
        } catch (IOException e) {
            log.error("Error reading ZIP file: {}\n{}", e.getMessage(), e);
            return false;
        }

        return true;
    }

    /**
     * 从文件名中提取文件扩展名
     *
     * @param filename 文件名，全名，示例：abc.json
     * @return 返回文件的拓展名，根据参数abc.json，返回示例：json
     */
    private static String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1).toLowerCase();
    }

    /**
     * 从路径中提取文件名
     *
     * @param entryName 路径名
     * @return 文件名
     */
    private static String getFileName(String entryName) {
        int slashIndex = entryName.lastIndexOf('/');
        return (slashIndex == -1) ? entryName : entryName.substring(slashIndex + 1);
    }

    /**
     * 检查allowedFileExtensions集合中的所有扩展名是否都在FileType枚举中
     *
     * @param allowedFileExtensions 文件扩展名集合
     * @return 如果所有扩展名都有效返回true，否则返回false
     */
    private static boolean areValidExtensions(Set<String> allowedFileExtensions) {
        for (String extension : allowedFileExtensions) {
            if (!FileType.isValidExtension(extension)) {
                return false;
            }
        }
        return true;
    }

}
