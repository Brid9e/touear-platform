package com.touear.core.oss.rule;


import com.touear.core.tool.utils.DateUtil;
import com.touear.core.tool.utils.FileUtil;
import com.touear.core.tool.utils.StringPool;
import com.touear.core.tool.utils.StringUtil;

import lombok.AllArgsConstructor;

/**
 * 默认存储桶生成规则
 *
 */
@AllArgsConstructor
public class TouearOssRule implements OssRule {


	@Override
	public String bucketName(String bucketName) {
		return bucketName;
	}

	@Override
	public String fileName(String originalFilename) {
		return "upload" + StringPool.SLASH + DateUtil.today() + StringPool.SLASH + StringUtil.randomUUID() + StringPool.DOT + FileUtil.getFileExtension(originalFilename);
	}

}
