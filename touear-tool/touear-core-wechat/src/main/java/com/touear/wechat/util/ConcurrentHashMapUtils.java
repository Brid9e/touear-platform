package com.touear.wechat.util;

import java.util.Map;
import java.util.function.Function;

/**
 * @Title: ConcurrentHashMapUtils
 * @Description: 统一工具类
 * @author wangqq
 * @date 2020-08-25 11:21:05
 * @version 1.0
 */
public class ConcurrentHashMapUtils {

    private static boolean IS_JAVA8;

    static {
        try {
            IS_JAVA8 = System.getProperty("java.version").startsWith("1.8.");
        } catch (Exception ignore) {
            IS_JAVA8 = true;
        }
    }

    /**
     * Java 8 ConcurrentHashMap#computeIfAbsent 存在性能问题的临时解决方案
     *
     * @see <a href="https://bugs.openjdk.java.net/browse/JDK-8161372">https://bugs.openjdk.java.net/browse/JDK-8161372</a>
     */
    public static <K, V> V computeIfAbsent(Map<K, V> map, K key, Function<? super K, ? extends V> mappingFunction) {
        if (IS_JAVA8) {
            V v = map.get(key);
            if (null == v) {
                v = map.computeIfAbsent(key, mappingFunction);
            }
            return v;
        } else {
            return map.computeIfAbsent(key, mappingFunction);
        }
    }
}
