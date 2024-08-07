package com.touear.common.utils;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * @Title: EnumValue.java
 * @Description: 自定义注解-校验指定值
 * @author xuegang
 * @date 2020-07-31 16:52:42
 * @version 1.0
 */
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {EnumValueValidator.class})
public @interface EnumValue {
 
    String message() default "必须为指定值";
    
    String[] strValues() default {};
    
    int[] intValues() default {};
 
    Class<?>[] groups() default {};
 
    Class<? extends Payload>[] payload() default {};
 
    //指定多个时使用
    @Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
    	EnumValue[] value();
    }
}