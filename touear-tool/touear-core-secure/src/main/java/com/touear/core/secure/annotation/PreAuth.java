package com.touear.core.secure.annotation;

import com.touear.core.secure.constant.DefaultTypeContant;

import java.lang.annotation.*;

/**
 * 权限注解 用于检查权限 规定访问权限
 *
 * @example @PreAuth("#userVO.id<10")
 * @example @PreAuth("hasRole(#test, #test1)")
 * @example @PreAuth("hasPermission(#test) and @PreAuth.hasPermission(#test)")
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface PreAuth {

	/**
	 * Spring el
	 * 文档地址：https://docs.spring.io/spring/docs/4.3.16.RELEASE/spring-framework-reference/htmlsingle/#expressions-operators-logical
	 */
	String value() default DefaultTypeContant.OPERATOR;

}

