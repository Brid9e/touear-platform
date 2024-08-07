package com.touear.gateway.location;

import com.touear.gateway.props.AuthorityResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @Title: ResourceLocator.java
 * @Description: ResourceLocator
 * @version 1.0
 */
@Slf4j
@Component
public class ResourceLocator  /*implements ApplicationListener<RemoteRefreshRouteEvent>*/  {

	/**
	 * 单位时间
	 */
	/**
	 * 1分钟
	 */
	public static final long SECONDS_IN_MINUTE = 60;
	/**
	 * 一小时
	 */
	public static final long SECONDS_IN_HOUR = 3600;
	/**
	 * 一天
	 */
	public static final long SECONDS_IN_DAY = 24 * 3600;

	/**
	 * 请求总时长
	 */
	public static final int PERIOD_SECOND_TTL = 10;
	public static final int PERIOD_MINUTE_TTL = 2 * 60 + 10;
	public static final int PERIOD_HOUR_TTL = 2 * 3600 + 10;
	public static final int PERIOD_DAY_TTL = 2 * 3600 * 24 + 10;

	/**
	 * 权限资源
	 */
	private List<AuthorityResource> authorityResources;


	/**
	 * 权限列表
	 */
	private Map<String, Collection<ConfigAttribute>> configAttributes = new ConcurrentHashMap<>();
	/**
	 * 缓存
	 */
	private Map<String, Object> cache = new ConcurrentHashMap<>();



	@Autowired
	private RouteDefinitionLocator routeDefinitionLocator;

	public ResourceLocator() {
		authorityResources = new CopyOnWriteArrayList<>();

	}



	/**
	 * 清空缓存并刷新
	 */
	public void refresh() {
		this.configAttributes.clear();
		this.cache.clear();
	}


	/**
	 * 获取路由后的地址
	 *
	 * @return
	 */
	protected String getFullPath(String serviceId, String path) {
		final String[] fullPath = { path.startsWith("/") ? path : "/" + path };
		routeDefinitionLocator.getRouteDefinitions()
				.filter(routeDefinition -> routeDefinition.getId().equals(serviceId)).subscribe(routeDefinition -> {
					routeDefinition.getPredicates().stream()
							.filter(predicateDefinition -> ("Path").equalsIgnoreCase(predicateDefinition.getName()))
							.filter(predicateDefinition -> !predicateDefinition.getArgs().containsKey("_rateLimit"))
							.forEach(predicateDefinition -> {
								fullPath[0] = predicateDefinition.getArgs().get("pattern").replace("/**",
										path.startsWith("/") ? path : "/" + path);
							});
				});
		return fullPath[0];
	}


	/**
	 * 获取单位时间内刷新时长和请求总时长
	 *
	 * @param timeUnit
	 * @return
	 */
	public static long[] getIntervalAndQuota(String timeUnit) {
		if (timeUnit.equalsIgnoreCase(TimeUnit.SECONDS.name())) {
			return new long[] { SECONDS_IN_MINUTE, PERIOD_SECOND_TTL };
		} else if (timeUnit.equalsIgnoreCase(TimeUnit.MINUTES.name())) {
			return new long[] { SECONDS_IN_MINUTE, PERIOD_MINUTE_TTL };
		} else if (timeUnit.equalsIgnoreCase(TimeUnit.HOURS.name())) {
			return new long[] { SECONDS_IN_HOUR, PERIOD_HOUR_TTL };
		} else if (timeUnit.equalsIgnoreCase(TimeUnit.DAYS.name())) {
			return new long[] { SECONDS_IN_DAY, PERIOD_DAY_TTL };
		} else {
			throw new IllegalArgumentException("Don't support this TimeUnit: " + timeUnit);
		}
	}

	public List<AuthorityResource> getAuthorityResources() {
		return authorityResources;
	}

	public void setAuthorityResources(List<AuthorityResource> authorityResources) {
		this.authorityResources = authorityResources;
	}

	
	public Map<String, Collection<ConfigAttribute>> getConfigAttributes() {
		return configAttributes;
	}

	public void setConfigAttributes(Map<String, Collection<ConfigAttribute>> configAttributes) {
		this.configAttributes = configAttributes;
	}

	public Map<String, Object> getCache() {
		return cache;
	}

	public void setCache(Map<String, Object> cache) {
		this.cache = cache;
	}

	public RouteDefinitionLocator getRouteDefinitionLocator() {
		return routeDefinitionLocator;
	}

	public void setRouteDefinitionLocator(RouteDefinitionLocator routeDefinitionLocator) {
		this.routeDefinitionLocator = routeDefinitionLocator;
	}


}
