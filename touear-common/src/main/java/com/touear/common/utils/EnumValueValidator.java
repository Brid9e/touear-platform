package com.touear.common.utils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Title: EnumValueValidator.java
 * @Description: 校验器-指定参数
 * @author xuegang
 * @date 2020-07-31 16:54:28
 * @version 1.0
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, Object>{
 
	private String[] strValues;
	private int[] intValues;
 
	@Override
	public void initialize(EnumValue constraintAnnotation) {
		strValues = constraintAnnotation.strValues();
		intValues = constraintAnnotation.intValues();
	}
 
	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context ) {
		if (value instanceof String) {
			for (String s : strValues) {
				if (s.equals(value)) {
					return true;
				}
			}
		} else if (value instanceof Integer) {
			for (Integer s : intValues) {
				if (s == value) {
					return true;
				}
			}
		}
		return false;
	}
 
}