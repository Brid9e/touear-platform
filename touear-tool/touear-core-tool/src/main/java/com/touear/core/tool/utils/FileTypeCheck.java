package com.touear.core.tool.utils;

import java.io.File;

import javax.activation.MimetypesFileTypeMap;

/**
 * @Title: FileTypeCheck.java
 * @Description: 判断文件类型
 * @author wangqq
 * @date 2019年3月6日 下午5:09:12
 * @version 1.0
 */
public class FileTypeCheck {

	private static MimetypesFileTypeMap imageMTFTM;
	private static MimetypesFileTypeMap videoMTFTM;
	private static MimetypesFileTypeMap fileMTFTM;

	static {
		imageMTFTM = new MimetypesFileTypeMap();
		imageMTFTM.addMimeTypes("image png gif jpg jpeg bmp");

		videoMTFTM = new MimetypesFileTypeMap();
		videoMTFTM.addMimeTypes("video flv swf mkv avi rm rmvb mpeg mpg ogg ogv mov wmv mp4 webm mp3 wav mid");

		fileMTFTM = new MimetypesFileTypeMap();
		fileMTFTM.addMimeTypes(
				"file png jpg jpeg gif bmp flv swf mkv avi rm rmvb mpeg mpg ogg ogv mov wmv mp4 webm mp3 wav mid rar zip tar gz 7z bz2 cab iso doc docx xls xlsx ppt pptx pdf txt md xml");

	}

	public static boolean isImage(File file) {
		String mimetype = imageMTFTM.getContentType(file);
		String type = mimetype.split("/")[0];
		return Func.equals("image", type);
	}

	public static boolean isVideo(File file) {
		String mimetype = videoMTFTM.getContentType(file);
		String type = mimetype.split("/")[0];
		return Func.equals("video", type);
	}

	public static boolean isFile(File file) {
		String mimetype = fileMTFTM.getContentType(file);
		String type = mimetype.split("/")[0];
		return Func.equals("file", type);
	}
	

}