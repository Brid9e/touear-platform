package com.touear.manage.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

/**
 * @Title: BaseFileService.java
 * @Description:  文件管理
 * @author Yang
 * @version 1.0
 */
public interface BaseFileService {

	/**
	 * 单个上传文件
	 *
	 * @param bucketName    桶名，用于区分上传文件类型
	 * @param multipartFile 文件
	 * @return 文件访问地址
	 * @author xuegang
	 * @date 2020-08-13 10:29:04
	 */
	String upload(String bucketName, MultipartFile multipartFile);

	/**
	 * 单个上传文件
	 *
	 * @param bucketName        桶名，用于区分上传文件类型
	 * @param multipartFiles 文件
	 * @return 文件访问地址
	 * @author xuegang
	 * @date 2020-08-13 10:40:22
	 */
	List<String> upload(String bucketName, MultipartFile[] multipartFiles);


	/**
	 * 流文件上传
	 * @param bucketName
	 * @param fileName
	 * @param stream
	 * @return 文件访问地址
	 * @author Administrator
	 * @date 2020-09-21 13:58:19
	 */
	String upload(String bucketName, String fileName, InputStream stream);


	/**
	 * 上传base64字符串
	 *
	 * @param bucketName 桶名，用于区分上传文件类型
	 * @param base64     base64字符串，数据示例：data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAACMCAIAAAAFl5vsAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA0ppVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNi1jMTM4IDc5LjE1OTgyNCwgMjAxNi8wOS8xNC0wMTowOTowMSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6RUVBREUwNzc1NzIyMTFFOEIyQUM4MTkwMzlEMkQ3MjUiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6RUVBREUwNzY1NzIyMTFFOEIyQUM4MTkwMzlEMkQ3MjUiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIDIwMTcgKFdpbmRvd3MpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDowNWEzZWM2Yy01NzIyLTExZTgtYTEzMy1lYThjZTQ4Zjk0NDAiIHN0UmVmOmRvY3VtZW50SUQ9ImFkb2JlOmRvY2lkOnBob3Rvc2hvcDowNWEzZWM2Yy01NzIyLTExZTgtYTEzMy1lYThjZTQ4Zjk0NDAiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+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
	 * @return
	 * @author xuegang
	 * @date 2020-08-13 10:40:22
	 */
	String uploadBase64(String bucketName, String base64);

	/**
	 * 移除文件
	 *
	 * @param bucketName 桶名，用于区分上传文件类型
	 * @param fileUrl    文件路径（只包含后半部分），数据示例：/{bucketName}/upload/20200416/d17e88eb6d4c1cce866cb14ae45de8eb.png
	 * @return
	 * @author xuegang
	 * @date 2020-08-13 11:18:03
	 */
	Boolean removeFile(String bucketName, String fileUrl);

	/**
	 * 批量删除文件夹下的文件
	 * @param bucketName 桶名
	 * @param prefix 文件夹名
	 * @return 删除标识
	 * @author gaoziming
	 */
	Boolean removeFiles(String bucketName, String prefix);

	/**
	 * 单个文件的拷贝
	 * @param bucketName 桶名称
	 * @param originName 源文件路径
	 * @param targetName 目标文件路径
	 * @author gaoZiMing
	 * @return 拷贝标识
	 */
	Boolean copyFile(String bucketName, String originName, String targetName);

	/**
	 * 功能描述：替换文件
	 * @Param:
	 * @param bucketName:
	 * @param fileName:
	 * @param oldName:
	 * @param stream:
	 * @Return:
	 * @Author: gaohaidong
	 * @Date: 2022/4/18 9:23
	 */
	String coverUpload(String bucketName, String fileName, String oldName, InputStream stream);




}
