package com.touear.starter.cache.utils;

import java.util.concurrent.Callable;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.lang.Nullable;

import com.touear.core.tool.utils.Func;
import com.touear.core.tool.utils.SpringContextUtils;

/**
 * 缓存工具类
 *
 * @author Chill
 */
public class CacheUtil {

	private static CacheManager cacheManager = SpringContextUtils.getInstance().getBean(CacheManager.class);

	/**
	 * 获取缓存对象
	 *
	 * @param cacheName 缓存名
	 * @return Cache
	 */
	public static Cache getCache(String cacheName) {
		return cacheManager.getCache(cacheName);
	}

	/**
	 * 获取缓存
	 *
	 * @param cacheName 缓存名
	 * @param keyPrefix 缓存键前缀
	 * @param key       缓存键值
	 * @return
	 */
	@Nullable
	public static Object get(String cacheName, String keyPrefix, Object key) {
		if (Func.hasEmpty(cacheName, keyPrefix, key)) {
			return null;
		}
		return getCache(cacheName).get(keyPrefix.concat(String.valueOf(key))).get();
	}

	/**
	 * 获取缓存
	 *
	 * @param cacheName 缓存名
	 * @param keyPrefix 缓存键前缀
	 * @param key       缓存键值
	 * @param type
	 * @param <T>
	 * @return
	 */
	@Nullable
	public static <T> T get(String cacheName, String keyPrefix, Object key, @Nullable Class<T> type) {
		if (Func.hasEmpty(cacheName, keyPrefix, key)) {
			return null;
		}
		return getCache(cacheName).get(keyPrefix.concat(String.valueOf(key)), type);
	}

	/**
	 * 获取缓存
	 *
	 * @param cacheName   缓存名
	 * @param keyPrefix   缓存键前缀
	 * @param key         缓存键值
	 * @param valueLoader 重载对象
	 * @param <T>
	 * @return
	 */
	@Nullable
	public static <T> T get(String cacheName, String keyPrefix, Object key, Callable<T> valueLoader) {
		if (Func.hasEmpty(cacheName, keyPrefix, key)) {
			return null;
		}
		try {
			return getCache(cacheName).get(keyPrefix.concat(String.valueOf(key)), valueLoader);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * 设置缓存
	 *
	 * @param cacheName 缓存名
	 * @param keyPrefix 缓存键前缀
	 * @param key       缓存键值
	 * @param value     缓存值
	 */
	public static void put(String cacheName, String keyPrefix, Object key, @Nullable Object value) {
		getCache(cacheName).put(keyPrefix.concat(String.valueOf(key)), value);
	}

	/**
	 * 清除缓存
	 *
	 * @param cacheName 缓存名
	 * @param keyPrefix 缓存键前缀
	 * @param key       缓存键值
	 */
	public static void evict(String cacheName, String keyPrefix, Object key) {
		if (Func.hasEmpty(cacheName, keyPrefix, key)) {
			return;
		}
		getCache(cacheName).evict(keyPrefix.concat(String.valueOf(key)));
	}

	/**
	 * 清空缓存
	 *
	 * @param cacheName 缓存名
	 */
	public static void clear(String cacheName) {
		if (Func.isEmpty(cacheName)) {
			return;
		}
		getCache(cacheName).clear();
	}

}
