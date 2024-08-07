package com.touear.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @title: FileUtils
 * @description: 文件操作
 * @version: 1.0
 **/
@Slf4j
public class FileUtils {

    /**
     * 新建文件夹
     *
     * @param path 文件夹首层
     */
    public static void createDirectory(Path path) {
        if (Files.exists(path)) {
            log.warn("文件夹 " + path + " 已存在");
        } else {
            try {
                Files.createDirectories(path);
                log.info("创建文件夹 " + path + " 成功");
            } catch (IOException e) {
                throw new RuntimeException("创建文件夹 " + path + " 失败");
            }
        }
    }

    /**
     * 删除文件
     *
     * @param first 文件夹首层
     * @param parts 文件夹其余路径(没有为null)
     * @return 是否成功
     */
    public static boolean deleteDir(String first, List<String> parts) {
        //获取系统路径分隔符
        String sep = FileSystems.getDefault().getSeparator();
        if (parts == null) {
            parts = Collections.emptyList();
        }
        Path path = Paths.get(first, String.join(sep, parts));
        if (!path.normalize().startsWith(first)) {
            return false;
        }
        if (Files.exists(path)) {
            try {
                rmdir(path);
                System.out.println("删除成功");
            } catch (IOException e) {
                log.error(e.getMessage());
                return false;
            }
        } else {
            log.error("文件不存在！");
            return false;
        }
        return true;
    }

    /**
     * 递归删除文件夹及文件
     *
     * @param dir 文件路径
     * @throws IOException 异常
     */
    public static void rmdir(Path dir) throws IOException {
        Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    /**
     * zip文件压缩
     *
     * @param inputFile  待压缩文件夹/文件名
     * @param outputFile 压缩目标路径
     * @param name       压缩包名字
     */
    public static boolean zipCompress(Path inputFile, Path outputFile, String name) throws Exception {
        File input = inputFile.toFile();
        if (!Files.exists(inputFile)) {
            return false;
        }
        if (!Files.exists(outputFile)) {
            return false;
        }
        outputFile = outputFile.resolve(name);
        //创建zip输出流
        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(outputFile.toFile()));
        //创建缓冲输出流
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(zipOutputStream);

        compress(zipOutputStream, bufferedOutputStream, input, null);
        bufferedOutputStream.close();
        zipOutputStream.close();
        return true;
    }

    /**
     * @param name 压缩文件名，可以写为null保持默认 递归压缩
     */
    public static void compress(ZipOutputStream zipOutputStream, BufferedOutputStream bufferedOutputStream, File input, String name) throws IOException {
        if (name == null) {
            name = input.getName();
        }
        //如果路径为目录（文件夹）
        if (input.isDirectory()) {
            //取出文件夹中的文件（或子文件夹）
            File[] fileList = input.listFiles();
            if (fileList == null) {
                return;
            }
            //如果文件夹为空，则只需在目的地zip文件中写入一个目录进入
            if (fileList.length == 0) {
                zipOutputStream.putNextEntry(new ZipEntry(name + "/"));
            }
            //如果文件夹不为空，则递归调用compress，文件夹中的每一个文件（或文件夹）进行压缩
            else {
                for (File file : fileList) {
                    compress(zipOutputStream, bufferedOutputStream, file, name + "/" + file.getName());
                }
            }
        }
        //如果不是目录（文件夹），即为文件，则先写入目录进入点，之后将文件写入zip文件中
        else {
            zipOutputStream.putNextEntry(new ZipEntry(name));
            FileInputStream fileInputStream = new FileInputStream(input);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            int len;
            //将源文件写入到zip文件中
            byte[] buf = new byte[1024];
            while ((len = bufferedInputStream.read(buf)) != -1) {
                bufferedOutputStream.write(buf, 0, len);
            }
            bufferedInputStream.close();
            fileInputStream.close();
        }
    }

    /**
     * zip解压
     *
     * @param srcPath     源文件路径
     * @param destDirPath 解压路径
     */
    public static void zipUncompress(Path srcPath, Path destDirPath) {
        //获取当前压缩文件
        File srcFile = srcPath.toFile();

        // 判断源文件是否存在
        if (!srcFile.exists()) {
            log.info(srcFile.getPath() + "所指文件不存在");
            return;
        }
        //开始解压
        //构建解压输入流
        try (
                FileInputStream srcFileInputStream = new FileInputStream(srcFile);
                ZipInputStream zIn = new ZipInputStream(srcFileInputStream, Charset.forName("gbk"))
        ) {
            ZipEntry entry;
            File file;
            while ((entry = zIn.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    file = destDirPath.resolve(entry.getName()).toFile();
                    if (!file.exists()) {
                        //创建此文件的上级目录
                        boolean mk = new File(file.getParent()).mkdirs();
                    }
                    try (OutputStream outputStream = new FileOutputStream(file);
                         BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream)) {
                        int len;
                        byte[] buf = new byte[1024];
                        while ((len = zIn.read(buf)) != -1) {
                            bufferedOutputStream.write(buf, 0, len);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    /**
     * 获取图片
     *
     * @param first     最外层文件夹名称
     * @param parts     其余路径
     * @param imageName 图片名称
     * @return 图片
     */
    public static BufferedImage getImage(String first, List<String> parts, String imageName) {
        //获取系统路径分隔符
        String sep = FileSystems.getDefault().getSeparator();
        if (parts == null) {
            parts = Collections.emptyList();
        }
        Path path = Paths.get(first, String.join(sep, parts));
        path = path.resolve(imageName);
        File image = path.toFile();
        try (InputStream inputStream = new FileInputStream(image);
        ) {
            return ImageIO.read(inputStream);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 检查文件名称是否合法
     *
     * @param fileName 文件名称
     */
    public static void checkFileName(String fileName) {
        String regEx = "\\.{2}|\\*";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            log.error("文件名含非法字符！");
        }
    }

    /**
     * 修改文件名称
     *
     * @param path    目标文件路径
     * @param newName 新名称
     * @return 是否成功
     */
    public static boolean changeFileName(Path path, String newName) {
        if (!Files.exists(path)) {
            log.error("文件不存在");
            return false;
        }
        try {
            Files.move(path, path.resolveSibling(newName));
            return true;
        } catch (IOException e) {
            log.error("修改文件名称失败！");
            log.error(e.getMessage());
            return false;
        }

    }

    /**
     * 保存文件
     *
     * @param file 文件
     * @param path 保存路径
     * @return 文件路径
     */
    public static Path saveFile(MultipartFile file, Path path) {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            return null;
        }
        return saveFile(file, path, fileName);
    }

    /**
     * 保存文件
     *
     * @param file     文件
     * @param path     保存路径
     * @param fileName 新文件名
     * @return 路径
     */
    public static Path saveFile(MultipartFile file, Path path, String fileName) {
        if (file.isEmpty()) {
            log.error("文件为空！");
            return null;
        }

        if (!Files.exists(path)) {
            try {
                FileUtils.createDirectory(path);
            } catch (RuntimeException e) {
                log.error(e.getMessage());
                return null;
            }
        }

        path = path.resolve(fileName);
        try {
            file.transferTo(path);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
        return path;
    }

    /**
     * 将inputStream 转化为ByteArrayOutputStream
     * 该方法为多次利用inputSteam, 通过ByteArrayInputStream byteIn=new ByteArrayInputStream(out.toByteArray());
     * 将ByteArrayOutputStream转化为ByteArrayInputStream
     * 利用ByteArrayInputStream支持reset()和mark()方法以此实现流的多次使用
     *
     * @param in 输入流
     * @return 输出流
     * @throws IOException IO异常
     */
    public static ByteArrayOutputStream copyStream(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = in.read(buffer)) > -1) {
            out.write(buffer, 0, len);
        }
        out.flush();
        in.close();
        return out;
    }

}
