# Redis-starter 文档

## 如何使用

1. 引入

```xml

<dependency>
    <groupId>com.touearcom.touear</groupId>
    <artifactId>touear-starter-redis</artifactId>
    <version>${touear.tool.version}</version>
</dependency>
```

2. 直接操作Redis

```java
// 操作String类型的数据
private final RedisRepo<String> strRepo;

strRepo.set("test:1", "123");

String test = strRepo.get("test:1");
```

3. 配置缓存

default-validity 配置的是默认的过期时间

validity 的子项配置的是指定的缓存的过期时间，例如 schoolIdByStudent

时间单位 秒 -> s, 分钟 -> m, 小时 -> h, 天 -> d

```yaml
spring:
  cache:
    type: redis
touear:
  cache:
    default-validity: 60s
    validity:
      schoolIdByStudent: 2d
```

```java
@Cacheable(value = "schoolIdByStudent", key = "#studentId")
@Override
public String schoolId(String studentId) {
    return attRecordMapper.getSchoolId(studentId);
}
```
