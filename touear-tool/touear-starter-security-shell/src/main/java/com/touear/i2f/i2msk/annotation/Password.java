package com.touear.i2f.i2msk.annotation;

import java.lang.annotation.*;

import com.touear.i2f.i2msk.contant.DefaultTypeContant;

/**
 * 
 * @Title: Password.java
 * @Description: 密码过滤
 * @author chenl
 * @date 2020-09-10 11:22:16
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Password {

	String[] value() default DefaultTypeContant.PASSWORD;

	boolean remove() default true;
	
}

