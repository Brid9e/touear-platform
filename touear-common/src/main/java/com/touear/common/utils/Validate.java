package com.touear.common.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Validator.class)
public @interface Validate {
	
	/**
	 * 参数校方法
	 * 数据在ValidatorTypeContant中
	 * 默认都为非null判断
	 */
	String method() default "notNull";
	
	/**
	 * 参数类型
	 */
	String paramType() default "object";
	
	/**
	 * 从map中解析数据的key值
	 * 默认全部解析不能为空
	 */
	String[] key() default "";
	
//	/**
//	 * 参数值（如果key值为存在，则表示该数据为json格式的字符串，需要解析数据进行校验）
//	 */
//	String value() default "";
	
	// 校验的失败的时候返回的信息,可以指定默认值
    String message() default "参数校验失败";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}