package com.touear.core.oss.rule;




import com.touear.core.oss.utils.DateUtil;
import com.touear.core.oss.utils.FileUtil;
import com.touear.core.oss.utils.StringPool;
import lombok.AllArgsConstructor;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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
		return "upload" + StringPool.SLASH + DateUtil.today() + StringPool.SLASH + randomUUID() + StringPool.DOT + FileUtil.getFileExtension(originalFilename);
	}

	public static String randomUUID() {
		ThreadLocalRandom random = ThreadLocalRandom.current();
		return new UUID(random.nextLong(), random.nextLong()).toString().replace(StringPool.DASH, StringPool.EMPTY);
	}
}
