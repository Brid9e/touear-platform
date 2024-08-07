package com.touear.starter.redis.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @title: RedisOps
 * @description: 操作Redis的工具类
 * @version: 1.0
 **/
@Repository
@RequiredArgsConstructor
public class RedisRepo<T> {

	/** 注入 RedisTemplate */
    private final RedisTemplate<String, T> redis;

    /** 操作 Redis Hash */
    private HashOperations<String, String, T> hashOps;

    private ListOperations<String, T> listOps;

    private SetOperations<String, T> setOps;


    @PostConstruct
    public void init() {
        this.hashOps = redis.opsForHash();
        this.listOps = redis.opsForList();
        this.setOps = redis.opsForSet();
    }

    /**
     * 删除key
     *
     * @param key 键
     * @return 是否成功
     */
    public Boolean delete(String key) {
        return redis.delete(key);
    }

    /**
     * 批量删除key
     *
     * @param keys 键列表
     * @return 删除的数量
     */
    public Long deleteAll(Collection<String> keys) {
        return redis.delete(keys);
    }

    /**
     * 是否存在key
     *
     * @param key 键
     * @return bool
     */
    public Boolean hasKey(String key) {
        return redis.hasKey(key);
    }

    /**
     * 设置存活时间
     *
     * @param key     键
     * @param timeout 存活时间
     * @param unit    时间单位
     * @return bool
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redis.expire(key, timeout, unit);
    }

    /**
     * 设置过期时间点
     *
     * @param key  键
     * @param date 过期时间
     * @return bool
     */
    public Boolean expireAt(String key, Date date) {
        return redis.expireAt(key, date);
    }

    /**
     * 查询 key 的剩余的过期时间
     *
     * @param key  键
     * @param unit 时间单位
     * @return 剩余过期时间
     */
    public Long getExpire(String key, TimeUnit unit) {
        return redis.getExpire(key, unit);
    }

    /**
     * 移除 key 的过期时间
     *
     * @param key 键
     * @return bool
     */
    public Boolean persist(String key) {
        return redis.persist(key);
    }

    /**
     * 查找匹配的key(不能用于生产环境，会有性能问题)
     * 应该用scan方法代替
     *
     * @param pattern 匹配的模式
     * @return 符合条件的所有key
     */
    @Deprecated
    public Set<String> keys(String pattern) {
        return redis.keys(pattern);
    }

    /**
     * 分批查找匹配的key
     *
     * @param pattern 匹配的模式
     * @param count   每次查询的数量
     * @return 符合条件的所有key
     */
    public Set<String> scan(String pattern, long count) {
        Set<String> keySet = new HashSet<>();
        scanLambda(pattern, count, item -> {
            //符合条件的key
            String key = new String(item, StandardCharsets.UTF_8);
            keySet.add(key);
        });
        return keySet;
    }

    /**
     * Scan 方法
     *
     * @param pattern  匹配的模式
     * @param count    每次查询的数量
     * @param consumer 对找到的key的处理方式
     */
    private void scanLambda(String pattern, long count, Consumer<byte[]> consumer) {
        this.redis.execute((RedisConnection connection) -> {
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(count).match(pattern).build())) {
                cursor.forEachRemaining(consumer);
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 修改 key
     *
     * @param oldKey 旧键
     * @param newKey 新键
     */
    public void rename(String oldKey, String newKey) {
        redis.rename(oldKey, newKey);
    }

    /**
     * 当 newKey 不存在时，将 oldKey 改名为 newKey
     *
     * @param oldKey 旧键
     * @param newKey 新键
     * @return bool
     */
    public Boolean renameIfAbsent(String oldKey, String newKey) {
        return redis.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 设置指定 key 的值
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, T value) {
        redis.opsForValue().set(key, value);
    }

    /**
     * 只有在 key 不存在时设置 key 的值
     *
     * @param key   键
     * @param value 值
     * @return 之前已经存在返回false, 不存在返回true
     */
    public Boolean setIfAbsent(String key, T value) {
        return redis.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 设置 key-value，并单独设置过期时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 单独的存活时间
     * @param unit    时间单位
     */
    public void setEx(String key, T value, long timeout, TimeUnit unit) {
        redis.opsForValue().set(key, value, timeout, unit);
    }

    /**
     * 获取指定 key 的值
     *
     * @param key 键
     * @return 值
     */
    public T get(String key) {
        return redis.opsForValue().get(key);
    }

    /**
     * 批量获取
     *
     * @param keys 键列表
     * @return 值列表
     */
    public List<T> multiGet(Collection<String> keys) {
        return redis.opsForValue().multiGet(keys);
    }

    /**
     * 批量添加
     *
     * @param maps 键值对
     */
    public void multiSet(Map<String, T> maps) {
        redis.opsForValue().multiSet(maps);
    }

    /**
     * 同时设置一个或多个 key-value，当且仅当所有给定 key 都不存在时生效
     *
     * @param maps 键值对
     * @return 已经存在返回false, 不存在返回true
     */
    public Boolean multiSetIfAbsent(Map<String, T> maps) {
        return redis.opsForValue().multiSetIfAbsent(maps);
    }

    /**
     * 增加(Value必须是数字)
     *
     * @param key       键
     * @param increment 增加值
     * @return 新的值
     */
    public Long increase(String key, long increment) {
        return redis.opsForValue().increment(key, increment);
    }

    /**
     * 增加(Value必须是数字)
     *
     * @param key       键
     * @param increment 增加值
     * @return 新的值
     */
    public Double increase(String key, double increment) {
        return redis.opsForValue().increment(key, increment);
    }

    /* ------------------- Hash相关操作 ------------------- */

    /**
     * 获取存储在哈希表中指定键的值
     *
     * @param key     键
     * @param hashKey 哈希表的键
     * @return 值
     */
    public T hGet(String key, String hashKey) {
        return hashOps.get(key, hashKey);
    }

    /**
     * 获取所有给定字段的值
     *
     * @param key 键
     * @return 哈希表
     */
    public Map<String, T> hGetAll(String key) {
        return hashOps.entries(key);
    }

    /**
     * 获取所有给定键的值
     *
     * @param key      键
     * @param hashKeys 哈希表的键列表
     * @return 哈希表的值列表
     */
    public List<T> hMultiGet(String key, Collection<String> hashKeys) {
        return hashOps.multiGet(key, hashKeys);
    }

    /**
     * 设置hash表中的值
     *
     * @param key     键
     * @param hashKey 哈希表的键
     * @param value   哈希表的值
     */
    public void hPut(String key, String hashKey, T value) {
        hashOps.put(key, hashKey, value);
    }

    /**
     * 批量设置hash表中的值
     *
     * @param key  键
     * @param maps 哈希表的键值对
     */
    public void hPutAll(String key, Map<String, T> maps) {
        hashOps.putAll(key, maps);
    }

    /**
     * 仅当hashKey不存在时才设置
     *
     * @param key     键
     * @param hashKey 哈希表的键
     * @param value   值
     * @return bool
     */
    public Boolean hPutIfAbsent(String key, String hashKey, T value) {
        return hashOps.putIfAbsent(key, hashKey, value);
    }

    /**
     * 删除哈希表字段
     *
     * @param key     键
     * @param hashKey 哈希表的键
     * @return 删除的数量
     */
    public Long hDel(String key, String hashKey) {
        return hashOps.delete(key, hashKey);
    }

    /**
     * 删除哈希表字段
     *
     * @param key      键
     * @param hashKeys 哈希表的键列表
     * @return 删除的数量
     */
    public Long hDelAll(String key, Collection<String> hashKeys) {
        return hashOps.delete(key, hashKeys.toArray());
    }

    /**
     * 查看哈希表中，指定的 Key 是否存在
     *
     * @param key     键
     * @param hashKey 哈希表的键
     * @return 是否存在
     */
    public boolean hExists(String key, String hashKey) {
        return hashOps.hasKey(key, hashKey);
    }

    /**
     * 为哈希表中指定key加上增量
     *
     * @param key       键
     * @param hashKey   哈希表的键
     * @param increment 增加值
     * @return 新的值
     */
    public Long hIncrease(String key, String hashKey, long increment) {
        return hashOps.increment(key, hashKey, increment);
    }

    /**
     * 为哈希表中指定key加上增量
     *
     * @param key       键
     * @param hashKey   哈希表的键
     * @param increment 增加值
     * @return 新的值
     */
    public Double hIncrease(String key, String hashKey, double increment) {
        return hashOps.increment(key, hashKey, increment);
    }

    /**
     * 获取哈希表中所有的键
     *
     * @param key 键
     * @return 所有键的Set
     */
    public Set<String> hKeys(String key) {
        return hashOps.keys(key);
    }

    /**
     * 获取哈希表中键值对的数量
     *
     * @param key 键
     * @return 键值对的数量
     */
    public Long hSize(String key) {
        return hashOps.size(key);
    }

    /**
     * 获取哈希表中所有值
     *
     * @param key 键
     * @return 所有值的List
     */
    public List<T> hValues(String key) {
        return hashOps.values(key);
    }

    /* ------------------- List相关操作 ------------------- */

    /**
     * 通过索引获取列表中的元素
     *
     * @param key   键
     * @param index 索引
     * @return list中的值
     */
    public T lGet(String key, long index) {
        return listOps.index(key, index);
    }

    /**
     * 获取列表指定范围内的元素
     *
     * @param key   键
     * @param start 开始位置, 0是开始位置
     * @param end   结束位置, -1返回所有
     * @return 范围内的元素
     */
    public List<T> lGetRange(String key, long start, long end) {
        return listOps.range(key, start, end);
    }

    /**
     * 在 list 前面加入新的元素
     *
     * @param key   键
     * @param value 新的元素
     * @return 当前列表长度
     */
    public Long lLeftPush(String key, T value) {
        return listOps.leftPush(key, value);
    }

    /**
     * 在指定元素的前面添加新的元素
     * 如果pivot存在, 在pivot前面添加
     *
     * @param key   键
     * @param pivot 指定元素
     * @param value 新的元素
     * @return 当前列表长度
     */
    public Long lLeftPush(String key, T pivot, T value) {
        return listOps.leftPush(key, pivot, value);
    }

    /**
     * 在 list 前面加入新的元素
     *
     * @param key    键
     * @param values 新的元素
     * @return 当前列表长度
     */
    public Long lLeftPushAll(String key, Collection<T> values) {
        return listOps.leftPushAll(key, values);
    }

    /**
     * 当list存在的时候才加入
     *
     * @param key   键
     * @param value 新的元素
     * @return 当前列表长度
     */
    public Long lLeftPushIfPresent(String key, T value) {
        return listOps.leftPushIfPresent(key, value);
    }

    /**
     * 在 list 后面加入新的元素
     *
     * @param key   键
     * @param value 新的元素
     * @return 当前列表长度
     */
    public Long lRightPush(String key, T value) {
        return listOps.rightPush(key, value);
    }

    /**
     * 在指定元素的后面添加新的元素
     * 如果pivot存在, 在pivot后面添加
     *
     * @param key   键
     * @param pivot 指定元素
     * @param value 新的元素
     * @return 当前列表长度
     */
    public Long lRightPush(String key, T pivot, T value) {
        return listOps.rightPush(key, pivot, value);
    }

    /**
     * 在 list 后面加入新的元素
     *
     * @param key    键
     * @param values 新的元素
     * @return 当前列表长度
     */
    public Long lRightPushAll(String key, Collection<T> values) {
        return listOps.rightPushAll(key, values);
    }

    /**
     * 当list存在的时候才加入
     *
     * @param key   键
     * @param value 新的元素
     * @return 当前列表长度
     */
    public Long lRightPushIfPresent(String key, T value) {
        return listOps.rightPushIfPresent(key, value);
    }

    /**
     * 通过索引设置列表元素的值(必须是已存在的索引)
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     */
    public void lSet(String key, long index, T value) {
        listOps.set(key, index, value);
    }

    /**
     * 删除并获取列表的第一个元素
     *
     * @param key 键
     * @return 删除的元素
     */
    public T lLeftPop(String key) {
        return listOps.leftPop(key);
    }

    /**
     * 删除并获取列表最后一个元素
     *
     * @param key 键
     * @return 删除的元素
     */
    public T lRightPop(String key) {
        return listOps.rightPop(key);
    }

    /**
     * 移除列表的最后一个元素，并将该元素添加到另一个列表并返回
     *
     * @param sourceKey      源列表的Key
     * @param destinationKey 目标列表的Key
     * @return 被转移的元素
     */
    public T lRightPopAndLeftPush(String sourceKey, String destinationKey) {
        return listOps.rightPopAndLeftPush(sourceKey, destinationKey);
    }

    /**
     * 删除集合中值等于value得元素
     *
     * @param key   键
     * @param count count > 0: Remove elements equal to value moving from head to tail.
     *              count < 0: Remove elements equal to value moving from tail to head.
     *              count = 0: Remove all elements equal to value.
     * @param value 要删除的值
     * @return 删除的数量
     */
    public Long lRemove(String key, long count, T value) {
        return listOps.remove(key, count, value);
    }

    /**
     * 裁剪list, 只保留list中指定范围内的部分
     *
     * @param key   键
     * @param start 开始位置
     * @param end   结束位置
     */
    public void lTrim(String key, long start, long end) {
        listOps.trim(key, start, end);
    }

    /**
     * 获取列表长度
     *
     * @param key 键
     * @return 列表长度
     */
    public Long lLen(String key) {
        return listOps.size(key);
    }

    /* ------------------- Set相关操作 ------------------- */

    /**
     * set添加元素
     *
     * @param key    键
     * @param values 值
     * @return size
     */
    @SafeVarargs
    public final Long sAdd(String key, T... values) {
        return setOps.add(key, values);
    }

    /**
     * set移除元素
     *
     * @param key    键
     * @param values 值
     * @return size
     */
    @SafeVarargs
    public final Long sRemove(String key, T... values) {
        return setOps.remove(key, (Object[]) values);
    }

    /**
     * 移除并返回集合的一个随机元素
     *
     * @param key 键
     * @return 随机元素
     */
    public T sPop(String key) {
        return setOps.pop(key);
    }

    /**
     * 将元素value从一个集合移到另一个集合
     *
     * @param key     键
     * @param value   元素
     * @param destKey 目标键
     * @return bool
     */
    public Boolean sMove(String key, T value, String destKey) {
        return setOps.move(key, value, destKey);
    }

    /**
     * 获取集合的大小
     *
     * @param key 键
     * @return 大小
     */
    public Long sSize(String key) {
        return setOps.size(key);
    }

    /**
     * 判断集合是否包含value
     *
     * @param key   键
     * @param value 元素
     * @return 是否存在
     */
    public Boolean sIsMember(String key, T value) {
        return setOps.isMember(key, value);
    }

    /**
     * 获取两个集合的交集
     *
     * @param key      键
     * @param otherKey 其它键
     * @return 结果集合
     */
    public Set<T> sIntersect(String key, String otherKey) {
        return setOps.intersect(key, otherKey);
    }

    /**
     * 获取key集合与多个集合的交集
     *
     * @param key       键
     * @param otherKeys 其它键
     * @return 结果集合
     */
    public Set<T> sIntersect(String key, Collection<String> otherKeys) {
        return setOps.intersect(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的交集存储到destKey集合中
     *
     * @param key      键
     * @param otherKey 其它键
     * @param destKey  目标键
     * @return 结果集合的大小
     */
    public Long sIntersectAndStore(String key, String otherKey, String destKey) {
        return setOps.intersectAndStore(key, otherKey, destKey);
    }

    /**
     * key集合与多个集合的交集存储到destKey集合中
     *
     * @param key       键
     * @param otherKeys 其它键
     * @param destKey   目标键
     * @return 结果集合的大小
     */
    public Long sIntersectAndStore(String key, Collection<String> otherKeys, String destKey) {
        return setOps.intersectAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取两个集合的并集
     *
     * @param key       键
     * @param otherKeys 其它键
     * @return 结果集合
     */
    public Set<T> sUnion(String key, String otherKeys) {
        return setOps.union(key, otherKeys);
    }

    /**
     * 获取key集合与多个集合的并集
     *
     * @param key       键
     * @param otherKeys 其它键
     * @return 结果集合
     */
    public Set<T> sUnion(String key, Collection<String> otherKeys) {
        return setOps.union(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的并集存储到destKey中
     *
     * @param key      键
     * @param otherKey 其它键
     * @param destKey  目标键
     * @return 结果集合的大小
     */
    public Long sUnionAndStore(String key, String otherKey, String destKey) {
        return setOps.unionAndStore(key, otherKey, destKey);
    }

    /**
     * key集合与多个集合的并集存储到destKey中
     *
     * @param key       键
     * @param otherKeys 其它键
     * @param destKey   目标键
     * @return 结果集合的大小
     */
    public Long sUnionAndStore(String key, Collection<String> otherKeys, String destKey) {
        return setOps.unionAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取两个集合的差集
     *
     * @param key      键
     * @param otherKey 其它键
     * @return 结果集合
     */
    public Set<T> sDifference(String key, String otherKey) {
        return setOps.difference(key, otherKey);
    }

    /**
     * 获取key集合与多个集合的差集
     *
     * @param key       键
     * @param otherKeys 其它键
     * @return 结果集合
     */
    public Set<T> sDifference(String key, Collection<String> otherKeys) {
        return setOps.difference(key, otherKeys);
    }

    /**
     * key集合与otherKey集合的差集存储到destKey中
     *
     * @param key      键
     * @param otherKey 其它键
     * @param destKey  目标键
     * @return 结果集合的大小
     */
    public Long sDifference(String key, String otherKey, String destKey) {
        return setOps.differenceAndStore(key, otherKey, destKey);
    }

    /**
     * key集合与多个集合的差集存储到destKey中
     *
     * @param key       键
     * @param otherKeys 其它键
     * @param destKey   目标键
     * @return 结果集合的大小
     */
    public Long sDifference(String key, Collection<String> otherKeys, String destKey) {
        return setOps.differenceAndStore(key, otherKeys, destKey);
    }

    /**
     * 获取集合所有元素
     *
     * @param key 键
     * @return 所有元素集合
     */
    public Set<T> setMembers(String key) {
        return setOps.members(key);
    }

    /**
     * 随机获取集合中的一个元素
     *
     * @param key 键
     * @return 随机元素
     */
    public T sRandomMember(String key) {
        return setOps.randomMember(key);
    }

    /**
     * 随机获取集合中count个元素
     *
     * @param key   键
     * @param count 个数
     * @return 随机元素列表
     */
    public List<T> sRandomMembers(String key, long count) {
        return setOps.randomMembers(key, count);
    }

    /**
     * 随机获取集合中count个元素并且去除重复的
     *
     * @param key   键
     * @param count 个数
     * @return 随机元素集合
     */
    public Set<T> sDistinctRandomMembers(String key, long count) {
        return setOps.distinctRandomMembers(key, count);
    }
}
