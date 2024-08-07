package com.touear.core.tool.utils;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.codec.binary.Base64;

/**
 * @Title: RSAEncrypt.java
 * @Description: RSA
 * @author chenl
 * @date 2020-08-04 16:25:43
 * @version 1.0
 */
@Log4j2
public class RSAEncrypt {
	private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥
/*	public static void main(String[] args) throws Exception {
		//生成公钥和私钥
		genKeyPair();
		//加密字符串
		String message = "df723820";
		System.out.println("随机生成的公钥为:" + keyMap.get(0));
		System.out.println("随机生成的私钥为:" + keyMap.get(1));
		String messageEn = encrypt(message,keyMap.get(0));
		System.out.println(message + "\t加密后的字符串为:" + messageEn);
		String messageDe = decrypt(messageEn,keyMap.get(1));
		System.out.println("还原后的字符串为:" + messageDe);
	}
	*/

//	public static void main(String[] args) throws Exception {
//		String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCxVSWnudiHPtl6z+Tjqf5vOAh6haqeC6mZoQ1GuxWDC8/J7T8as9U6Ks4GpTTkxr1G+4+3jzuVKrDk5a9BYXqwHzh8EQgy4JILL7crsQfH/FFAANVMxMMQRxGOPTD5Rzkx9BNlZ2SwoFhoxAyYcIpOAk6hcn8KCx1CZ9p3WW+owIDAQAB";
//		String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMLFVJae52Ic+2XrP5OOp/m84CHqFqp4LqZmhDUa7FYMLz8ntPxqz1ToqzgalNOTGvUb7j7ePO5UqsOTlr0FherAfOHwRCDLgkgsvtyuxB8f8UUAA1UzEwxBHEY49MPlHOTH0E2VnZLCgWGjEDJhwik4CTqFyfwoLHUJn2ndZb6jAgMBAAECgYEAkSF9MGbjv0YdRgqPayJHpF3LHTpS/V9kU3XcWb3gERYvNn4d7GCicYCV5M5Zy3W4BqBzXME44daRD/6vudWdvMwTLmrd9uC/SHxwSpexvD/ajhcObgg7R0HYRh1ZcFJsih/y3JNq4svF+PFpD5GUObumoKBRLV07uApl6Jp/QXECQQDmi3cmCjNTV5Hx07m8y2VJhkz7jyPz5FIhBCff2MPd3eHd1apI4ThZ4iEpxEhTU3JmCZqaprtM/WbGePdSJu1XAkEA2EavW7pn72KehAMpXJ5BZ3+Ey3fIaKmuc0J8kwHScULIbcukxCAkvC3z3efgZZSpQQcJKC3y/wOfSzPaE4NdlQJBAMno+Tf6lQr31dz6dmAvroyfL4rdyToQDBHYZue4G4qTif5T4+giP80Qq8S9oC5CFh3haWLqhX2fLMTEKq7rYs8CQFi1fX3RUopE2CHrrg54Nvl9u3k9c21Ck/Rgw/oEWE3uh4lJaxaq3IZ3DrzpPAhOVrH+ccoSNsZUtQTCiuNQEZUCQCOihyqqYxdm7fW36+FbbXZ6YsJ7gzHe2MuPwpd71SUqIgowBqXST82wtlSvi7CgmdDCiNuNM7Wf7el64CXzNEU=";
//		String key = "account=gaoliqiang&token=sssssssssssssssssssssssssssssssssssssssssssszxcqw2312&loginFrom=app&timeStamp=1565654984894894";
//
////		String test = encrypt(key,publicKey);
//
//		String test = "YF837q6+Be1BvjgBX4M9psIPU0So9DCd9mgahpw/9xW0HDTzuFEnQmsA1M+8ZNaQovusBHQRjjn3072Np52SClJiwzsTiaXEN3/9ekzlu0OFI8reZXWsZ3bxnwOX6yfjJ41rMQFGPpw/q24j1qNYel+HSGUcPydpfKPcd0NA4iS46VlTg9pce/AT7L1aECtOQztPe6tvNOmzTDFiQDjDPPgBkCn6Oigp9x/Ul8u/XJUGGhSJw+GQoUY2fWdR/Vaq/XX244kL0FKh3N343T+N7ztM0c2yWUAyYpGUI3rjGwYffRei4zmPIRRJqUSsnZ6drC8DhlELaxz+Z5xWqHoYRw==";
//
//		System.out.println(test);
//
//		System.out.println(privateKeyDecrypt(test,privateKey,null));
//
//
//
//	}


	/**
	 * RSA公钥加密
	 *
	 * @param str       加密字符串
	 * @param publicKey 公钥
	 * @return 密文
	 * @throws Exception 加密过程中的异常信息
	 */
	public static String publicKeyEncrypt(String str, String publicKey, String point) throws Exception {
		log.info("{}|RSA公钥加密前的数据|str:{}|publicKey:{}", point, str, publicKey);
		//base64编码的公钥
		byte[] decoded = Base64.decodeBase64(publicKey);
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").
				generatePublic(new X509EncodedKeySpec(decoded));
		//RSA加密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);

		//当长度过长的时候，需要分割后加密 117个字节
		byte[] resultBytes = getMaxResultEncrypt(str, point, cipher);

		String outStr = Base64.encodeBase64String(resultBytes);
		log.info("{}|公钥加密后的数据|outStr:{}", point, outStr);
		return outStr;
	}

	private static byte[] getMaxResultEncrypt(String str, String point, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
		byte[] inputArray = str.getBytes();
		int inputLength = inputArray.length;
		log.info("{}|加密字节数|inputLength:{}", point, inputLength);
		// 最大加密字节数，超出最大字节数需要分组加密
		int MAX_ENCRYPT_BLOCK = 117;
		// 标识
		int offSet = 0;
		byte[] resultBytes = {};
		byte[] cache = {};
		while (inputLength - offSet > 0) {
			if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
				offSet += MAX_ENCRYPT_BLOCK;
			} else {
				cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
				offSet = inputLength;
			}
			resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
			System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
		}
		return resultBytes;
	}

	/**
	 * RSA私钥解密
	 *
	 * @param str        加密字符串
	 * @param privateKey 私钥
	 * @param point
	 * @return 铭文
	 * @throws Exception 解密过程中的异常信息
	 */
	public static String privateKeyDecrypt(String str, String privateKey, String point)  {
		log.info("{}|RSA私钥解密前的数据|str:{}|privateKey:{}", point, str, privateKey);
		try{
			//64位解码加密后的字符串
			byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
			//base64编码的私钥
			byte[] decoded = Base64.decodeBase64(privateKey);
			RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
					.generatePrivate(new PKCS8EncodedKeySpec(decoded));
			//RSA解密
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, priKey);
//        String outStr = new String(cipher.doFinal(inputByte));
			//当长度过长的时候，需要分割后解密 128个字节
			String outStr = new String(getMaxResultDecrypt(str, point, cipher));
			log.info("{}|RSA私钥解密后的数据|outStr:{}", point, outStr);
			return outStr;
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private static byte[] getMaxResultDecrypt(String str, String point, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
		byte[] inputArray = Base64.decodeBase64(str.getBytes("UTF-8"));
		int inputLength = inputArray.length;
		log.info("{}|解密字节数|inputLength:{}", point, inputLength);
		// 最大解密字节数，超出最大字节数需要分组加密
		int MAX_ENCRYPT_BLOCK = 128;
		// 标识
		int offSet = 0;
		byte[] resultBytes = {};
		byte[] cache = {};
		while (inputLength - offSet > 0) {
			if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
				offSet += MAX_ENCRYPT_BLOCK;
			} else {
				cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
				offSet = inputLength;
			}
			resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
			System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
		}
		return resultBytes;
	}

	/** 
	 * 随机生成密钥对 
	 * @throws NoSuchAlgorithmException 
	 */  
	public static void genKeyPair() throws NoSuchAlgorithmException {  
		// KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象  
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");  
		// 初始化密钥对生成器，密钥大小为96-1024位  
		keyPairGen.initialize(1024,new SecureRandom());  
		// 生成一个密钥对，保存在keyPair中  
		KeyPair keyPair = keyPairGen.generateKeyPair();  
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥  
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥  
		String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));  
		// 得到私钥字符串  
		String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));  
		// 将公钥和私钥保存到Map
		keyMap.put(0,publicKeyString);  //0表示公钥
		keyMap.put(1,privateKeyString);  //1表示私钥
	}  
	/** 
	 * RSA公钥加密 
	 *  
	 * @param str 
	 *            加密字符串
	 * @param publicKey 
	 *            公钥 
	 * @return 密文 
	 * @throws Exception 
	 *             加密过程中的异常信息 
	 */  
	public static String encrypt( String str, String publicKey ) throws Exception{
		//base64编码的公钥
		byte[] decoded = Base64.decodeBase64(publicKey);
		RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
		//RSA加密
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		String outStr = Base64.encodeBase64String(cipher.doFinal(str.getBytes("UTF-8")));
		return outStr;
	}

	/** 
	 * RSA私钥解密
	 *  
	 * @param str 
	 *            加密字符串
	 * @param privateKey 
	 *            私钥 
	 * @return 铭文
	 * @throws Exception 
	 *             解密过程中的异常信息 
	 */  
	public static String decrypt(String str, String privateKey) {
		String outStr = null;
		try {
			//64位解码加密后的字符串
			byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
			//base64编码的私钥
			byte[] decoded = Base64.decodeBase64(privateKey);  
	        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));  
			//RSA解密
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, priKey);
			outStr = new String(cipher.doFinal(inputByte));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return outStr;
	}



}