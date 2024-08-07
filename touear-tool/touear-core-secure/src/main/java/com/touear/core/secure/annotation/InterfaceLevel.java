package com.touear.core.secure.annotation;

import com.touear.core.secure.enums.InterfaceLevelEnum;

import java.lang.annotation.*;

/**
 * @Title: InterfaceLevel.java
 * @Description: 接口访问级别
 * @author touear
 * @date 2018年10月31日
 * @version 1.0
 */
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InterfaceLevel {

	InterfaceLevelEnum value() default InterfaceLevelEnum.USER;

}