package com.touear.manage.controller;

import com.google.protobuf.ServiceException;
import com.touear.common.enums.FileType;
import com.touear.common.enums.MinioBucketEnum;
import com.touear.common.enums.ServiceType;
import com.touear.common.utils.FileUploadValidator;
import com.touear.common.utils.R;
import com.touear.core.oss.props.OssProperties;
import com.touear.core.oss.utils.Func;
import com.touear.core.oss.utils.StringPool;
import com.touear.manage.entity.BaseSysconfig;
import com.touear.manage.service.BaseFileService;
import com.touear.manage.service.BaseSysconfigService;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * @Title: BaseFileController.java
 * @Description: 文件管理
 * @author Yang
 * @version 1.0
 */
@Api(tags = "文件管理")
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/file")
public class BaseFileController {


	private final BaseFileService fileService;
	private final OssProperties ossProperties;
	private final BaseSysconfigService sysconfigService;


	/**
	 * 以MultipartFile形式上传文件
	 * 
	 * @param bucketName 桶名，用于区分上传文件路径
	 * @param multipartFile
	 * @return
	 * @author Administrator
	 * @date 2020-08-06 17:19:10
	 */
	@PostMapping("/uploadFile")
	public R<String> uploadFile(@RequestParam String bucketName, @RequestParam("file") MultipartFile multipartFile) {
		List<String> list = getlist();
		if(!multipartFile.isEmpty()) {
		  String originalFilename = multipartFile.getOriginalFilename();
		  if(originalFilename.contains(".")) {
			  int length = originalFilename.split("\\.").length - 1;
			  if(!list.contains(originalFilename.split("\\.")[length])) {
				  return R.fail("文件 " + originalFilename + " 格式不正确");
			  }
		  }else {
			  return R.fail("文件 " + originalFilename + " 格式不正确");
		  }
		}else {
			return R.fail("文件 " + multipartFile.getOriginalFilename() + "为空");
		}
		if (MinioBucketEnum.contains(bucketName)) {
			String fileUrl = fileService.upload(bucketName, multipartFile);
			if (StringUtils.isNotBlank(fileUrl)) {
				return R.data(fileUrl);
			} else {
				return R.fail("上传失败！");
			}
		}
		return R.fail("配置错误，上传失败！");
	}

	/**
	 * 以MultipartFile形式上传文件
	 *
	 * @param path 桶名，用于区分上传文件路径
	 * @param multipartFile
	 * @return
	 * @author Administrator
	 * @date 2020-08-06 17:19:10
	 */
	@PostMapping("/uploadFileByPath")
	public R<String> uploadFileByPath(@RequestParam String bucketName,String path,
									  @RequestParam("file") MultipartFile multipartFile,
									  String serviceType) {
		String fileUrl = "";
		String url = "/" + bucketName + path;

		if(Func.isNotBlank(serviceType)){
			if(!ServiceType.isValidExtension(serviceType)){
				return R.fail("服务类型不存在！");
			}
			ServiceType serviceTypeEnum = ServiceType.getByServiceType(serviceType);
            if (serviceTypeEnum == null || Func.isBlank(serviceTypeEnum.getExtension())) {
				return R.fail("服务类型不存在！");
            }
            Set<String> allowedFileExtensions = new HashSet<>(Arrays.asList(serviceTypeEnum.getExtension().split(StringPool.COMMA)));
			long maxFileSize = serviceTypeEnum.getSize();
			if (!FileUploadValidator.validFile(multipartFile, allowedFileExtensions, maxFileSize)) {
				return R.fail("文件校验错误！");
			}
			if(Func.isBlank(path)){
				// 获取文件的原始名称
				String originalFilename = multipartFile.getOriginalFilename();

				// 使用 FilenameUtils 获取文件后缀
				String extension = FilenameUtils.getExtension(originalFilename);
				url = "/" + bucketName + "/" + serviceType + "/"+ Func.randomUuid() +StringPool.DOT + extension;
			}


		}else {
			// 文件校验
			Set<String> allowedFileExtensions = new HashSet<>();
			allowedFileExtensions.add(FileType.JPG.getExtension());
			allowedFileExtensions.add(FileType.JPEG.getExtension());
			allowedFileExtensions.add(FileType.PNG.getExtension());
			long maxFileSize = 1 * 1024 * 1024;
			if (!FileUploadValidator.validFile(multipartFile, allowedFileExtensions, maxFileSize)) {
				return R.fail("文件校验错误！");
			}
		}


		if (MinioBucketEnum.contains(bucketName)) {
			try {

				fileUrl = fileService.coverUpload(bucketName, url, url, multipartFile.getInputStream());
				if(Func.isBlank(fileUrl)){
					throw new ServiceException("上传失败！");
				}
				if(Func.isBlank(serviceType)){
					fileUrl = ossProperties.getAddress() + "/minio" + fileUrl;
				}

			} catch (Exception e){
				e.printStackTrace();
			}
			if (StringUtils.isNotBlank(fileUrl)) {
				return R.data(fileUrl);
			} else {
				return R.fail("上传失败！");
			}
		}
		return R.fail("配置错误，上传失败！");
	}

	/**
	 * 以MultipartFile形式上传文件
	 * 
	 * @param bucketName     桶名，用于区分上传文件类型
	 * @param multipartFiles 文件集合
	 * @return
	 * @author Administrator
	 * @date 2020-08-06 17:19:10
	 */
	@PostMapping("/uploadFiles")
	public R<List<String>> uploadFiles(@RequestParam String bucketName, @RequestParam("files") MultipartFile[] multipartFiles) {
		List<String> list = getlist();
		  for (MultipartFile multipartFile : multipartFiles) { 
			  String originalFilename = multipartFile.getOriginalFilename();
			  if(originalFilename.contains(".")) {
				  int length = originalFilename.split("\\.").length - 1;
				  if(!list.contains(originalFilename.split("\\.")[length])) {
					  return R.fail("文件 " + originalFilename + " 格式不正确");
				  }
			  }else {
				  return R.fail("文件 " + originalFilename + " 格式不正确");
			  }
		  }
		if (MinioBucketEnum.contains(bucketName)) {
			List<String> retList = fileService.upload(bucketName, multipartFiles);
			if (retList != null && retList.size() > 0) {
				return R.data(retList);
			} else {
				return R.fail("上传失败！");
			}
		}
		return R.fail("配置错误，上传失败！");
	}

	public List<String> getlist() {
		//
		BaseSysconfig selectSysconfig = sysconfigService.selectSysconfig("file_restrictive");
		List<String> list = new ArrayList<String>();
		boolean hasConfig = false;
		try {
			list = Func.readJson(selectSysconfig.getValue(), List.class);
			if(list != null && list.size() > 0) {
				hasConfig = true;
			}
		} catch (Exception e) {
		}
		if(!hasConfig) {
			list.add("jpg");
			list.add("png");
			list.add("rar");
			list.add("txt");
			list.add("zip");
			list.add("doc");
			list.add("ppt");
			list.add("xls");
			list.add("pdf");
			list.add("docx");
			list.add("xlsx");
			list.add("p8");
			list.add("p12");
		}
		return list;
	}
	
	/**
	 * 根据文件名删除文件
	 * 
	 * @param map
	 * 	bucketName 桶名，用于区分上传文件类型
	 *  fileUrl    文件路径（只包含后半部分），数据示例：/{bucketName}/upload/20200416/d17e88eb6d4c1cce866cb14ae45de8eb.png
	 * @return
	 * @author Administrator
	 * @date 2020-08-06 17:18:04
	 */
	@PostMapping("/remove")
	public R<Boolean> delete(@RequestBody @Validate(paramType = ParamTypeContant.MAP, key = {"bucketName", "fileUrl"}, message = "缺少参数") Map<String, Object> map) {
		try {
			String bucketName = map.get("bucketName").toString();
			String fileUrl = map.get("fileUrl").toString();
			if (MinioBucketEnum.contains(bucketName)) {
				fileUrl = fileUrl.replace("/" + bucketName, "");
				if (fileService.removeFile(bucketName, fileUrl)) {
					return R.data(true);
				}
				return R.fail("移除文件失败！");
			}
			return R.fail("配置错误！");
		} catch (Exception e) {
			e.printStackTrace();
			return R.fail(ResultCode.FAILURE);
		}
	}

	/**
	 * 以base64方式上传文件
	 * 
	 * @param bucketName
	 * @param base64
	 * @return
	 * @author Administrator
	 * @date 2020-08-06 17:17:49
	 */
	@PostMapping("/uploadBase64")
	public R<String> uploadBase64(@RequestParam String bucketName, @RequestBody String base64) {
		if (MinioBucketEnum.contains(bucketName)) {
			return R.data(fileService.uploadBase64(bucketName, base64));
		}
		return R.fail("配置错误，上传失败！");
	}


	/**
	* 功能描述：根据租户上传密钥
	* @Param:
	 * @param platType: 平台类型
	 * @param multipartFile: 文件
	* @Return:
	* @Author: gaohaidong
	* @Date: 2023/3/1 17:47
	*/
	@PostMapping("/uploadSecretKeyByTen")
	public R<String> uploadFileByTen(@RequestParam String platType,
									  @RequestParam("file") MultipartFile multipartFile) {
		return fileService.uploadFileByTen(platType, multipartFile);
	}

}
