package me.chanjar.weixin.common.util.locks;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @Title: RedisTemplateSimpleDistributedLock
 * @Description: 重写加锁的类，原始类因为序列化的问题报错
 * @author wangqq
 * @date 2023-10-09 10:14:07
 * @version 1.0
 */
public class RedisTemplateSimpleDistributedLock implements Lock {
    private final StringRedisTemplate redisTemplate;
    private final String key;
    private final int leaseMilliseconds;
    private final ThreadLocal<String> valueThreadLocal;

    public RedisTemplateSimpleDistributedLock(StringRedisTemplate redisTemplate, int leaseMilliseconds) {
        this(redisTemplate, "lock:" + UUID.randomUUID().toString(), leaseMilliseconds);
    }

    public RedisTemplateSimpleDistributedLock(StringRedisTemplate redisTemplate, String key, int leaseMilliseconds) {
        this.valueThreadLocal = new ThreadLocal();
        if (leaseMilliseconds <= 0) {
            throw new IllegalArgumentException("Parameter 'leaseMilliseconds' must grate then 0: " + leaseMilliseconds);
        } else {
            this.redisTemplate = redisTemplate;
            this.key = key;
            this.leaseMilliseconds = leaseMilliseconds;
        }
    }

    public void lock() {
        while(!this.tryLock()) {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException var2) {
            }
        }

    }

    public void lockInterruptibly() throws InterruptedException {
        while(!this.tryLock()) {
            Thread.sleep(1000L);
        }

    }

    public boolean tryLock() {
        String value = (String)this.valueThreadLocal.get();
        RedisSerializer keySerializer = this.redisTemplate.getKeySerializer();
        RedisSerializer valueSerializer = this.redisTemplate.getValueSerializer();
        byte[] valueBytes;
        if (value == null || value.length() == 0) {
            value = UUID.randomUUID().toString();
            valueBytes = valueSerializer.serialize(value.getBytes(StandardCharsets.UTF_8));
            value = new String(valueBytes);
            this.valueThreadLocal.set(value);
        } else {
            valueBytes = value.getBytes(StandardCharsets.UTF_8);
        }
        byte[] keyBytes = keySerializer.serialize(this.key);
        int leaseMilliseconds = this.leaseMilliseconds;
        List<Object> redisResults = this.redisTemplate.executePipelined(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                connection.set(keyBytes, valueBytes, Expiration.milliseconds((long)leaseMilliseconds), SetOption.SET_IF_ABSENT);
                connection.get(keyBytes);
                return null;
            }
        }, valueSerializer);
        Object valueObject = valueSerializer.deserialize(valueBytes);
        Object currentLockSecret = redisResults.size() > 1 ? redisResults.get(1) : redisResults.get(0);
        return currentLockSecret != null && currentLockSecret.toString().equals(valueObject.toString());
    }

    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        long waitMs = unit.toMillis(time);

        boolean locked;
        for(locked = this.tryLock(); !locked && waitMs > 0L; locked = this.tryLock()) {
            long sleep = waitMs < 1000L ? waitMs : 1000L;
            Thread.sleep(sleep);
            waitMs -= sleep;
        }

        return locked;
    }

    public void unlock() {
        if (this.valueThreadLocal.get() != null) {
            String valueStr = this.valueThreadLocal.get();
            Object valueObj = this.redisTemplate.getValueSerializer().deserialize(valueStr.getBytes(StandardCharsets.UTF_8));
            if (valueObj != null && this.redisTemplate.hasKey(this.key)) {
                String s = this.redisTemplate.opsForValue().get(this.key);
                if (valueObj.toString().equals(s)) {
                    this.redisTemplate.delete(this.key);
                }
            }
            this.valueThreadLocal.remove();
        }
    }

    public Condition newCondition() {
        throw new UnsupportedOperationException();
    }

    public String getLockSecretValue() {
        return (String)this.valueThreadLocal.get();
    }

    public StringRedisTemplate getRedisTemplate() {
        return this.redisTemplate;
    }

    public String getKey() {
        return this.key;
    }

    public int getLeaseMilliseconds() {
        return this.leaseMilliseconds;
    }
}
