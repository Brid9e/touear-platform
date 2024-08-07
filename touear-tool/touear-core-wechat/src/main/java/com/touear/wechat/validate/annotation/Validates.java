package com.touear.wechat.validate.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Title: Params.java
 * @Description: 实体类的属性值校验规则注解之多重注解
 * @author wangqq
 * @date 2019年12月5日 下午1:59:11
 * @version 1.0
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validates {

    /**
     * 中文名【优先使用Param注解的name属性值】
     */
    String name() default "";
	
    /**
     * 实体类的属性值规则校验注解
     */
	Validate[] value();

}