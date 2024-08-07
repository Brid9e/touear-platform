package com.touear.i2f.i2msk.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

import cn.hutool.core.codec.Base64;

public class RSAUtils {

	private final KeyPair keyPair = initKey();
	private final static short keyLength = 2048;

	private KeyPair initKey() {
		try {
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			SecureRandom random = new SecureRandom();
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
			generator.initialize(keyLength, random);
			return generator.generateKeyPair();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 生成public key
	 * 
	 * @return
	 */
	public String generateBase64PublicKey() {
		RSAPublicKey key = (RSAPublicKey) keyPair.getPublic();
		return new String(Base64.encode(key.getEncoded()));
	}

	/**
	 * 生成prviate key
	 * 
	 * @return
	 */
	public String generateBase64PrviateKey() {
		PrivateKey key = keyPair.getPrivate();
		return new String(Base64.encode(key.getEncoded()));
	}

	/**
	 * 解密
	 * 
	 * @param string
	 * @return
	 */
	public String decryptBase64(String string) {
		return new String(decrypt(Base64.decode(string)));
	}
	
	private byte[] decrypt(byte[] body) {
		try {
			Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", new org.bouncycastle.jce.provider.BouncyCastleProvider());
			RSAPrivateKey pbk = (RSAPrivateKey) keyPair.getPrivate();
			cipher.init(Cipher.DECRYPT_MODE, pbk);
			byte[] plainText = cipher.doFinal(body);
			return plainText;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}