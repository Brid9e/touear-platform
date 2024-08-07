package com.touear.gateway.gatewayFilter;

import cn.hutool.core.collection.ConcurrentHashSet;
import com.touear.constants.CommonConstants;
import com.touear.core.tool.constant.ErrorCode;
import com.touear.core.tool.utils.Func;
import com.touear.core.tool.utils.ReactiveIpAddressMatcher;
import com.touear.core.tool.utils.StringUtil;
import com.touear.gateway.location.ResourceLocator;
import com.touear.gateway.props.AuthProperties;
import com.touear.gateway.props.AuthorityResource;
import com.touear.gateway.provider.AuthProvider;

import com.touear.security.OpenAuthority;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 * @Title: AccessManager.java
 * @Description: 访问权限控制管理类
 * @author chenl
 * @date 2020-06-15 14:37:55
 * @version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
@EnableConfigurationProperties(AuthProperties.class)
public class AccessManager implements ReactiveAuthorizationManager<AuthorizationContext> {


	private ResourceLocator resourceLocator;

	private AuthProperties authProperties;

	private static final AntPathMatcher pathMatch = new AntPathMatcher();

	private Set<String> permitAll = new ConcurrentHashSet<>();

	private Set<String> authorityIgnores = new ConcurrentHashSet<>();

	@Override
	public Mono<AuthorizationDecision> check(Mono<Authentication> authentication,
			AuthorizationContext authorizationContext) {
		return null;
	}



	private boolean isSkip(String path) {
		return AuthProvider.getDefaultSkipUrl().stream()
				.map(url -> url.replace(AuthProvider.TARGET, AuthProvider.REPLACEMENT)).anyMatch(path::startsWith)
				|| authProperties.getSkipUrl().stream()
						.map(url -> url.replace(AuthProvider.TARGET, AuthProvider.REPLACEMENT))
						.anyMatch(path::startsWith);
	}

	/**
	 * 始终放行
	 *
	 * @param requestPath
	 * @return
	 */
	public boolean permitAll(String requestPath) {
		boolean permit = permitAll.stream().filter(r -> pathMatch.match(r, requestPath)).findFirst().isPresent();
		if (permit) {
			return true;
		}
		// 动态权限列表
		return resourceLocator.getAuthorityResources().stream().filter(res -> StringUtils.isNotBlank(res.getPath()))
				.filter(res -> {
					Boolean isAuth = res.getIsAuth() != null && res.getIsAuth().intValue() == 1 ? true : false;
					// 无需认证,返回true
					return pathMatch.match(res.getPath(), requestPath) && !isAuth;
				}).findFirst().isPresent();
	}

	/**
	 * 获取资源状态
	 *
	 * @param requestPath
	 * @return
	 */
	public AuthorityResource getResource(String requestPath) {
		List<AuthorityResource> authorityResources = resourceLocator.getAuthorityResources();
		if(Func.isEmpty(authorityResources)){
			resourceLocator.refresh();
		}
		// 动态权限列表
		return resourceLocator.getAuthorityResources().stream().filter(r -> r.getAuthority().startsWith("API")).filter(r -> StringUtils.isNotBlank(r.getPath()))
				.filter(r -> !"/**".equals(r.getPath())).filter(r -> pathMatch.match(r.getPath(), requestPath))
				.filter(r -> !permitAll(r.getPath())).findFirst().orElse(null);
	}

	/**
	 * 忽略鉴权
	 *
	 * @param requestPath
	 * @return
	 */
	private boolean authorityIgnores(String requestPath) {
		return authorityIgnores.stream().filter(r -> pathMatch.match(r, requestPath)).findFirst().isPresent();
	}

	public boolean mathAuthorities(ServerWebExchange exchange, Authentication authentication, String requestPath) {
		Collection<ConfigAttribute> attributes = getAttributes(requestPath);
		int result = 0;
		int expires = 0;
		if (authentication == null) {
			return false;
		} else {
			if (CommonConstants.ROOT.equals(authentication.getName())) {
				// 默认超级管理员账号,直接放行
				return true;
			}
			Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
			Iterator var6 = attributes.iterator();
			while (var6.hasNext()) {
				ConfigAttribute attribute = (ConfigAttribute) var6.next();
				Iterator var8 = authorities.iterator();
				while (var8.hasNext()) {
					GrantedAuthority authority = (GrantedAuthority) var8.next();
					if (attribute.getAttribute().equals(authority.getAuthority())) {
						result++;
						if (authority instanceof OpenAuthority) {
							OpenAuthority customer = (OpenAuthority) authority;
							if (customer.getIsExpired() != null && customer.getIsExpired()) {
								// 授权过期数
								expires++;
							}
						}
					}
				}
			}
			log.debug("mathAuthorities result[{}] expires[{}]", result, expires);
			if (expires > 0) {
				// 授权已过期
				throw new AccessDeniedException(ErrorCode.ACCESS_DENIED_AUTHORITY_EXPIRED.getMessage());
			}
			return result > 0;
		}
	}

	private Collection<ConfigAttribute> getAttributes(String requestPath) {
		// 匹配动态权限
		AtomicReference<Collection<ConfigAttribute>> attributes = new AtomicReference<>();
		resourceLocator.getConfigAttributes().keySet().stream().filter(r -> !"/**".equals(r))
				.filter(r -> pathMatch.match(r, requestPath)).findFirst().ifPresent(r -> {
					attributes.set(resourceLocator.getConfigAttributes().get(r));
				});
		if (attributes.get() != null) {
			return attributes.get();
		}
		return SecurityConfig.createList("AUTHORITIES_REQUIRED");
	}


	/**
	 * 匹配IP或域名
	 *
	 * @param values
	 * @param ipAddress
	 * @param origin
	 * @return
	 */
	public boolean matchIpOrOrigin(Set<String> values, String ipAddress, String origin) {
		ReactiveIpAddressMatcher ipAddressMatcher = null;
		for (String value : values) {
			if (StringUtil.matchIp(value)) {
				ipAddressMatcher = new ReactiveIpAddressMatcher(value);
				if (ipAddressMatcher.matches(ipAddress)) {
					return true;
				}
			} else {
				if (StringUtil.matchDomain(value) && StringUtils.isNotBlank(origin) && origin.contains(value)) {
					return true;
				}
			}
		}
		return false;
	}

}
