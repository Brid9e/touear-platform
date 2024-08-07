package com.touear.i2f.i2msk;

import java.io.IOException;
import java.util.Map;

import com.touear.i2f.i2msk.cache.Cache;
import com.touear.i2f.i2msk.cache.memory.MemoryCacheManager;
import com.touear.i2f.i2msk.codec.AsymmetryCodec;
import com.touear.i2f.i2msk.codec.RSA;
import com.touear.i2f.i2msk.codec.SM2;
import com.touear.i2f.i2msk.enums.KeyboardEnum;

  
public class Config {

	public static long cacheSize=100;
	public static long pollingTime=60000;
	public static long securityKeyboardOverdue=1800000;
	public static int standardFontSize=140;
	public static int numberFontSize=30;
	/**
	 * 默认内存缓存 30分钟过期
	 */
	public static com.touear.i2f.i2msk.cache.Cache keyboardCache = MemoryCacheManager.getMemoryCache("securityKeyboard");
	public static Class<? extends AsymmetryCodec> rsaClass=RSA.class;
	public static Class<? extends AsymmetryCodec> sm2Class=SM2.class;
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void config(
			Long cacheSize,
			Long pollingTime,
			Long securityKeyboardOverdue,
			Integer standardFontSize,
			Integer numberFontSize,
			Cache keyboardCache,
			Class rsaClass,
			Class sm2Class){
		
		if(cacheSize != null ){
			Config.cacheSize=cacheSize;
		}
		
		if(pollingTime != null){
			Config.pollingTime=pollingTime;
		}
		
		if(securityKeyboardOverdue!=null){
			Config.securityKeyboardOverdue = securityKeyboardOverdue;	
		}

		if(standardFontSize != null){
			Config.standardFontSize = standardFontSize;
		}
		
		if(numberFontSize != null){
			Config.numberFontSize = numberFontSize;
		}
		
		if(keyboardCache != null){
			Config.keyboardCache=keyboardCache;
		}

		if(rsaClass != null){
			Config.rsaClass=rsaClass;
		}
		if(sm2Class != null){
			Config.sm2Class=sm2Class;
		}
	}
	/**
	 * 密文解密
	 * @param password 密文
	 * @return 明文
	 */
	public static String[] decoderPwd(boolean remove,String... password){
		return Keyboard.decoderPwd(remove,password);
	}
	/**
	 * 获取键盘
	 * @param type  键盘类型
	 * @return 键盘
	 * @throws IOException
	 */
	public static Map<String,Object> keyboardFactory(KeyboardEnum type) throws IOException{
		return Keyboard.createKeyboard(type);
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
	public static Map<String,Object> keyboardFactory(String type, String... options) throws IOException {
		String[] tmp=new String[2];
		tmp[0] = ( options.length >= 1 && "0".equals(options[0]) ) ? "0" : "1";
		tmp[1] = options.length == 2 ? ("SM2".equals(options[1]) ? "SM2" : "RSA") : "";
		if("Number".equals(type)){
			if(tmp[0].equals("0")){
				if(tmp[1].equals("SM2")){
					return keyboardFactory(KeyboardEnum.NUMBER_ORDER_SM2);
				}else if(tmp[1].equals("RSA")){
					return keyboardFactory(KeyboardEnum.NUMBER_ORDER_RSA);
				}else {
					return keyboardFactory(KeyboardEnum.NUMBER_ORDER);
				}
			}else{
				if(tmp[1].equals("SM2")){
					return keyboardFactory(KeyboardEnum.NUMBER_DISORDER_SM2);
				}else if(tmp[1].equals("RSA")){
					return keyboardFactory(KeyboardEnum.NUMBER_DISORDER_RSA);
				}else {
					return keyboardFactory(KeyboardEnum.NUMBER_DISORDER);
				}
			}
		}else {
			if(tmp[0].equals("0")){
				if(tmp[1].equals("SM2")){
					return keyboardFactory(KeyboardEnum.STANDARD_ORDER_SM2);
				}else if(tmp[1].equals("RSA")) {
					return keyboardFactory(KeyboardEnum.STANDARD_ORDER_RSA);
				} else{
					return keyboardFactory(KeyboardEnum.STANDARD_ORDER);
				}
			}else{
				if(tmp[1].equals("SM2")){
					return keyboardFactory(KeyboardEnum.STANDARD_DISORDER_SM2);
				}else if(tmp[1].equals("RSA")){
					return keyboardFactory(KeyboardEnum.STANDARD_DISORDER_RSA);
				}else {
					return keyboardFactory(KeyboardEnum.STANDARD_DISORDER);
				}
			}
		}
	}
	
	
	
}

