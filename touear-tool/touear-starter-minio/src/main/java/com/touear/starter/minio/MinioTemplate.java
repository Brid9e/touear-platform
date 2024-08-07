package com.touear.starter.minio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.touear.core.oss.model.TouearFile;
import io.minio.*;
import io.minio.messages.DeleteError;
import io.minio.messages.Item;
import org.springframework.web.multipart.MultipartFile;

import com.touear.core.oss.OssTemplate;
import com.touear.core.oss.model.OssFile;
import com.touear.core.oss.props.OssProperties;
import com.touear.core.oss.rule.OssRule;
import com.touear.core.tool.utils.Func;
import com.touear.core.tool.utils.StringPool;
import com.touear.starter.minio.enums.PolicyType;

import io.minio.messages.Bucket;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * MinIOTemplate
 *
 * @author Chen
 */
@Slf4j
@AllArgsConstructor
public class MinioTemplate implements OssTemplate {

	/**
	 * MinIO客户端
	 */
	private final MinioClient client;

	/**
	 * 存储桶命名规则
	 */
	private final OssRule ossRule;

	/**
	 * 配置类
	 */
	private final OssProperties ossProperties;


	@Override
	@SneakyThrows
	public void makeBucket(String bucketName) {
		if (!client.bucketExists(getBucketName(bucketName))) {
			client.makeBucket(getBucketName(bucketName));
			client.setBucketPolicy(getBucketName(bucketName), getPolicyType(getBucketName(bucketName), PolicyType.READ));
		}
	}

	@SneakyThrows
	public Bucket getBucket() {
		return getBucket(getBucketName());
	}

	@SneakyThrows
	public Bucket getBucket(String bucketName) {
		Optional<Bucket> bucketOptional = client.listBuckets().stream().filter(bucket -> bucket.name().equals(getBucketName(bucketName))).findFirst();
		return bucketOptional.orElse(null);
	}

	@SneakyThrows
	public List<Bucket> listBuckets() {
		return client.listBuckets();
	}

	@Override
	@SneakyThrows
	public void removeBucket(String bucketName) {
		client.removeBucket(getBucketName(bucketName));
	}

	@Override
	@SneakyThrows
	public boolean bucketExists(String bucketName) {
		return client.bucketExists(getBucketName(bucketName));
	}

	@Override
	@SneakyThrows
	public void copyFile(String bucketName, String fileName, String destBucketName) {
//		client.copyObject(getBucketName(bucketName), fileName, destBucketName);
	}

	@Override
	@SneakyThrows
	public void copyFile(String bucketName, String fileName, String destBucketName, String destFileName) {
//		client.copyObject(getBucketName(bucketName), fileName, getBucketName(destBucketName), destFileName);
	}

	@Override
	@SneakyThrows
	public OssFile statFile(String fileName) {
		return statFile(ossProperties.getBucketName(), fileName);
	}

	@Override
	@SneakyThrows
	public OssFile statFile(String bucketName, String fileName) {
		ObjectStat stat = client.statObject(getBucketName(bucketName), fileName);
		OssFile ossFile = new OssFile();
		ossFile.setName(Func.isEmpty(stat.name()) ? fileName : stat.name());
		ossFile.setLink(fileLink(ossFile.getName()));
		ossFile.setHash(String.valueOf(stat.hashCode()));
		ossFile.setLength(stat.length());
//		ossFile.setPutTime(stat.createdTime());
		ossFile.setContentType(stat.contentType());
		return ossFile;
	}

	@Override
	public String filePath(String fileName) {
		return getBucketName().concat(StringPool.SLASH).concat(fileName);
	}

	@Override
	public String filePath(String bucketName, String fileName) {
		return getBucketName(bucketName).concat(StringPool.SLASH).concat(fileName);
	}

	@Override
	@SneakyThrows
	public String fileLink(String fileName) {
		return ossProperties.getEndpoint().concat(StringPool.SLASH).concat(getBucketName()).concat(StringPool.SLASH).concat(fileName);
	}

	@Override
	@SneakyThrows
	public String fileLink(String bucketName, String fileName) {
		return ossProperties.getEndpoint().concat(StringPool.SLASH).concat(getBucketName(bucketName)).concat(StringPool.SLASH).concat(fileName);
	}

	@Override
	@SneakyThrows
	public TouearFile putFile(MultipartFile file) {
		return putFile(ossProperties.getBucketName(), file.getOriginalFilename(), file);
	}

	@Override
	@SneakyThrows
	public TouearFile putFile(String fileName, MultipartFile file) {
		return putFile(ossProperties.getBucketName(), fileName, file);
	}

	@Override
	@SneakyThrows
	public TouearFile putFile(String bucketName, String fileName, MultipartFile file) {
		return putFile(bucketName, file.getOriginalFilename(), file.getInputStream());
	}

	@Override
	@SneakyThrows
	public TouearFile putFile(String fileName, InputStream stream) {
		return putFile(ossProperties.getBucketName(), fileName, stream);
	}

	@Override
	@SneakyThrows
	public TouearFile putFile(String bucketName, String fileName, InputStream stream) {
		makeBucket(bucketName);
		String originalName = fileName;
		fileName = getFileName(fileName);

		return putBerserkerFile(bucketName, fileName, stream, originalName);
	}
	
	@Override
	@SneakyThrows
	public TouearFile putFileReFro(String bucketName, String fileName, InputStream stream) {
		makeBucket(bucketName);
		String originalName = fileName;
		fileName = getFileName(fileName);
		TouearFile putTouearFile = putBerserkerFile(bucketName, fileName, stream, originalName);
		if(putTouearFile != null) {
			String replace = putTouearFile.getLink().replace(ossProperties.getEndpoint(), ossProperties.getForeignAddress());
			putTouearFile.setLink(replace);
		}
		return putTouearFile;
	}

	private TouearFile putBerserkerFile(String bucketName, String fileName, InputStream stream, String originalName) throws Exception {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()){
        	byte[] buffer = new byte[1024];
        	int len;
            while ((len = stream.read(buffer)) > -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            byte[] fileBytes = outputStream.toByteArray();
            PutObjectOptions putObjectOptions = new PutObjectOptions(fileBytes.length, -1);
			String fileType = fileName.substring(fileName.lastIndexOf("."));
			//判断上传文件类型,以便访问
			switch (fileType) {
				case ".jpg":
				case ".jpeg":
					putObjectOptions.setContentType("image/jpeg");
					break;
				case ".svg":
					putObjectOptions.setContentType("image/svg+xml");
					break;
				case ".css":
					putObjectOptions.setContentType("text/css");
					break;
				case ".gif":
					putObjectOptions.setContentType("image/gif");
					break;
				case ".png":
					putObjectOptions.setContentType("image/png");
					break;
				case ".js":
					putObjectOptions.setContentType("application/x-javascript");
					break;
				default:
					putObjectOptions.setContentType("application/octet-stream");
			}
    		client.putObject(getBucketName(bucketName), fileName, new ByteArrayInputStream(fileBytes), putObjectOptions);
        } catch (IOException e) {
        	log.error(e.getMessage());
            throw new Exception("Illegal flow.");
		} finally {
			if (stream != null) {
				stream.close();
			}
		}
		TouearFile file = new TouearFile();
		file.setOriginalName(originalName);
		file.setName(fileName);
		file.setLink(fileLink(bucketName, fileName));
		return file;
	}

	/**
	 * 上传文件 (使用参数作为桶名)
	 *
	 * @param bucketName 存储桶名称
	 * @param fileName   文件名称
	 * @param stream     文件流
	 * @return TouearFile
	 */
	@Override
	@SneakyThrows
	public TouearFile putFileWithOriginName(String bucketName, String fileName, InputStream stream) {
		//文件夹不存在则创建新的
        boolean isExist = client.bucketExists(bucketName);
        if (!isExist) {
			client.makeBucket(bucketName);
			client.setBucketPolicy(bucketName, getPolicyType(bucketName, PolicyType.READ));
        }
		return putBerserkerFile(bucketName, fileName, stream, fileName);
	}

	@Override
	@SneakyThrows
	public void removeFile(String fileName) {
		removeFile(ossProperties.getBucketName(), fileName);
	}

	@Override
	@SneakyThrows
	public void removeFile(String bucketName, String fileName) {
		client.removeObject(getBucketName(bucketName), fileName);
	}

	@Override
	@SneakyThrows
	public void removeFiles(List<String> fileNames) {
		removeFiles(ossProperties.getBucketName(), fileNames);

	}

	@Override
	@SneakyThrows
	public void removeFiles(String bucketName, List<String> fileNames) {
		client.removeObjects(getBucketName(bucketName), fileNames);
	}

	/**
	 * 批量删除文件
	 *
	 * @param bucketName 存储桶名称
	 * @param prefix     文件夹名称
	 */
	@Override
	@SneakyThrows
	public boolean removeFiles(String bucketName, String prefix) {
			Iterable<Result<Item>> resultIterable = client.listObjects(bucketName, prefix, true);
			List<String> items = new ArrayList<>();
			for (Result<Item> itemResult : resultIterable) {
				String itemName = itemResult.get().objectName();
				items.add(itemName);
			}
			Iterable<Result<DeleteError>> results = client.removeObjects(bucketName, items);
			for (Result<DeleteError> result : results) {
				DeleteError error = result.get();
				log.error("MINIO删除文件失败： " + error.objectName() + "; " + error.message());
				return false;
			}
		return true;
	}

	/**
	 * 获取指定文件的文件流
	 * @param bucketName 存储桶名称
	 * @param fileNameWithPrefix 带路径文件名
	 * @return 文件流
	 */
	@SneakyThrows
	@Override
	public InputStream getMinIoObject(String bucketName, String fileNameWithPrefix) {
		try  {
			return client.getObject(bucketName, fileNameWithPrefix);
		}catch (Exception e) {
			log.error("Get inputStream from MinIo error, message: " + e.getMessage());
			return null;
		}
	}

	/**
	 *
	 * @param bucketName 存储桶名称
	 * @param prefix 带路径文件名
	 * @param recursive 是否递归查询
	 * @return 查询结果
	 */
	@SneakyThrows
	public Iterable<Result<Item>> listMinIoObjects(String bucketName, String prefix, boolean recursive){
		return client.listObjects(bucketName, prefix, recursive);
	}


	/**
	 * 根据规则生成存储桶名称规则
	 *
	 * @return String
	 */
	private String getBucketName() {
		return getBucketName(ossProperties.getBucketName());
	}

	/**
	 * 根据规则生成存储桶名称规则
	 *
	 * @param bucketName 存储桶名称
	 * @return String
	 */
	private String getBucketName(String bucketName) {
		return ossRule.bucketName(bucketName);
	}

	/**
	 * 根据规则生成文件名称规则
	 *
	 * @param originalFilename 原始文件名
	 * @return string
	 */
	private String getFileName(String originalFilename) {
		return ossRule.fileName(originalFilename);
	}

	/**
	 * 获取存储桶策略
	 *
	 * @param policyType 策略枚举
	 * @return String
	 */
	public String getPolicyType(PolicyType policyType) {
		return getPolicyType(getBucketName(), policyType);
	}

	/**
	 * 获取存储桶策略
	 *
	 * @param bucketName 存储桶名称
	 * @param policyType 策略枚举
	 * @return String
	 */
	public String getPolicyType(String bucketName, PolicyType policyType) {
		StringBuilder builder = new StringBuilder();
		builder.append("{\n");
		builder.append("    \"Statement\": [\n");
		builder.append("        {\n");
		builder.append("            \"Action\": [\n");

		switch (policyType) {
			case WRITE:
				builder.append("                \"s3:GetBucketLocation\",\n");
				builder.append("                \"s3:ListBucketMultipartUploads\"\n");
				break;
			case READ_WRITE:
				builder.append("                \"s3:GetBucketLocation\",\n");
				builder.append("                \"s3:ListBucket\",\n");
				builder.append("                \"s3:ListBucketMultipartUploads\"\n");
				break;
			default:
				builder.append("                \"s3:GetBucketLocation\"\n");
//				builder.append("                \"s3:ListBucket\"\n");
				break;
		}

		builder.append("            ],\n");
		builder.append("            \"Effect\": \"Allow\",\n");
		builder.append("            \"Principal\": \"*\",\n");
		builder.append("            \"Resource\": \"arn:aws:s3:::");
		builder.append(bucketName);
		builder.append("\"\n");
		builder.append("        },\n");
		builder.append("        {\n");
		builder.append("            \"Action\": ");

		switch (policyType) {
			case WRITE:
				builder.append("[\n");
				builder.append("                \"s3:AbortMultipartUpload\",\n");
				builder.append("                \"s3:DeleteObject\",\n");
				builder.append("                \"s3:ListMultipartUploadParts\",\n");
				builder.append("                \"s3:PutObject\"\n");
				builder.append("            ],\n");
				break;
			case READ_WRITE:
				builder.append("[\n");
				builder.append("                \"s3:AbortMultipartUpload\",\n");
				builder.append("                \"s3:DeleteObject\",\n");
				builder.append("                \"s3:GetObject\",\n");
				builder.append("                \"s3:ListMultipartUploadParts\",\n");
				builder.append("                \"s3:PutObject\"\n");
				builder.append("            ],\n");
				break;
			default:
				builder.append("\"s3:GetObject\",\n");
				break;
		}

		builder.append("            \"Effect\": \"Allow\",\n");
		builder.append("            \"Principal\": \"*\",\n");
		builder.append("            \"Resource\": \"arn:aws:s3:::");
		builder.append(bucketName);
		builder.append("/*\"\n");
		builder.append("        }\n");
		builder.append("    ],\n");
		builder.append("    \"Version\": \"2012-10-17\"\n");
		builder.append("}\n");
		return builder.toString();
	}

}
