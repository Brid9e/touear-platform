package com.touear.wechat.validate.annotation;

import com.touear.wechat.validate.service.Add;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title: Param.java
 * @Description: 实体类的属性值规则校验注解
 * @author wangqq
 * @date 2019年12月5日 下午1:59:11
 * @version 1.0
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {

	/**
	 * 属性分组
	 */
    Class<?>[] groups() default Add.class;
    
	/**
	 * 属性名
	 */
	String name() default "";

	/**
	 * 排序
	 */
	int order() default 0;

	/**
	 * 不可为空
	 */
	boolean notNull() default false;
	
	/**
	 * 长度
	 */
	int length() default -1;
	
	/**
	 * 属性值是属于某个数组之内
	 */
	String[] in() default {};
	
	/**
	 * 符合某个正则表达式
	 */
	String regex() default "";
	
}