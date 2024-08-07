package com.touear.i2f.i2msk;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.touear.i2f.i2msk.cache.Cache;
import com.touear.i2f.i2msk.codec.AsymmetryCodec;
import com.touear.i2f.i2msk.enums.KeyboardEnum;
import com.touear.i2f.i2msk.util.I2MskClassLoader;


  
public class Security {
	static I2MskClassLoader i2MskClassLoader=new I2MskClassLoader();
	/**
	 * 安全键盘配置
	 * @param cacheSize 缓存大小(默认：100)
	 * @param pollingTime 缓存更新时间（单位毫秒） （默认：60000）
	 * @param securityKeyboardOverdue 键盘过期时间（单位毫秒）（默认：1800000）
	 * @param standardFontSize 全键盘字符大小 （默认：40）
	 * @param numberFontSize 数字键盘字符大小 （默认：30）
	 * @param keyboardCache 键盘缓存的实现。为默认实现（内存缓存）。
	 * @param rsaClass  RSA算法实现类
	 * @param sm2Class  sm2算法实现类
	 */
	@SuppressWarnings("rawtypes")
	public static void config(Long cacheSize,Long pollingTime,Long securityKeyboardOverdue,Integer standardFontSize,Integer numberFontSize,Cache keyboardCache,Class<? extends AsymmetryCodec> rsaClass,Class<? extends AsymmetryCodec> sm2Class){
		try {
			Class cache = i2MskClassLoader.loadClass("com.i2f.i2msk.cache.Cache");
			
			Class<?> config = i2MskClassLoader.loadClass("com.i2f.i2msk.Config");
			Method method = config.getMethod("config"
					, Long.class
					, Long.class
					, Long.class
					, Integer.class
					, Integer.class
					, cache
					, Class.class
					, Class.class);
			method.invoke(null, cacheSize,
					pollingTime,
					securityKeyboardOverdue,
					standardFontSize,
					numberFontSize,
					keyboardCache,
					rsaClass,
					sm2Class);
			
		} catch (ClassNotFoundException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (NoSuchMethodException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (SecurityException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (IllegalAccessException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (IllegalArgumentException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (InvocationTargetException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		}  
	}
	public static String[] decoderPwd(String... password){
		try {
			Class<?> config = i2MskClassLoader.loadClass("com.i2f.i2msk.Config");
			Method method = config.getMethod("decoderPwd",password.getClass());
			return (String[]) method.invoke(null, (Object)password);
		} catch (ClassNotFoundException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (NoSuchMethodException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (SecurityException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (IllegalAccessException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (IllegalArgumentException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (InvocationTargetException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		}
	}
	/**
	 * 获取键盘
	 * @param type  键盘类型
	 * @return 键盘
	 * @throws IOException
	 */
	public static String keyboardFactory(KeyboardEnum type) throws IOException{
		try {
			Class<?> config = i2MskClassLoader.loadClass("com.i2f.i2msk.Config");
			Method method = config.getMethod("keyboardFactory",KeyboardEnum.class);
			return (String)method.invoke(null, type);
		} catch (ClassNotFoundException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (NoSuchMethodException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (SecurityException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (IllegalAccessException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (IllegalArgumentException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (InvocationTargetException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		}
	}
	/**
	 * 获取键盘
	 * @param type 键盘类型
	 * 				Number 数字键盘
	 * 			    Standard 全键盘
	 * @param options 键盘选项
	 * 				第一位： 是否乱序   1 乱序， 0 不乱序
	 * 				第二位： 加密算法   RSA SM2
	 * @return 键盘
	 * @throws IOException
	 */
	public static String keyboardFactory(String type, String... options) throws IOException {
		try {
			Class<?> config = i2MskClassLoader.loadClass("com.i2f.i2msk.Config");
			Method method = config.getMethod("keyboardFactory",String.class,options.getClass());
			return (String)method.invoke(null, type,options);
		} catch (ClassNotFoundException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (NoSuchMethodException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (SecurityException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (IllegalAccessException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (IllegalArgumentException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		} catch (InvocationTargetException e) {
			throw new java.lang.RuntimeException("版本出现问题",e);
		}
	}
	
}

