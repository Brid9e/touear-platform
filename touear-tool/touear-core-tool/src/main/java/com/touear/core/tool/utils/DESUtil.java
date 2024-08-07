package com.touear.core.tool.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * @author wangqq
 * @version 1.0
 * @Title: DESUtil.java
 * @Description: DES加密解密工具类
 * @date 2018年1月24日 上午11:16:38
 */
public class DESUtil {

    private final static String DES_ALGORITHM = "DES";
    private final static String PADDING = "DES/CBC/PKCS5Padding";
    private final static byte[] DESIV = {0x12, 0x34, 0x56, 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};

    /**
     * DES加密
     *
     * @param plainData 原始字符串
     * @param secretKey 密钥
     * @param offset    偏移量
     * @return 加密后的字符串
     * @throws Exception
     */
    public static String encryption(String plainData, String secretKey, String offset) throws Exception {
        // 设置密钥参数
        DESKeySpec keySpec = new DESKeySpec(secretKey.getBytes());
        // 设置向量
        AlgorithmParameterSpec iv = new IvParameterSpec(offset.getBytes());
        // 获得密钥工厂
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        // 得到密钥对象
        SecretKey key = keyFactory.generateSecret(keySpec);
        // 得到加密对象Cipher
        Cipher enCipher = Cipher.getInstance(PADDING);
        // 设置工作模式为加密模式，给出密钥和向量
        enCipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] pasByte = enCipher.doFinal(plainData.getBytes("utf-8"));
        return Base64.getEncoder().encodeToString(pasByte);
    }

    /**
     * DES加密
     *
     * @param plainData 原始字符串
     * @param secretKey 密钥
     * @return 加密后的字符串
     * @throws Exception
     */
    public static String encryption(String plainData, String secretKey) throws Exception {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(DES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, generateKey(secretKey));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        try {
            byte[] buf = cipher.doFinal(plainData.getBytes());
            return NewBase64Util.encode(buf);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * DES解密
     *
     * @param secretData 密码字符串
     * @param secretKey  密钥
     * @return 原始字符串
     * @throws Exception
     */
    public static String decryption(String secretData, String secretKey) throws Exception {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(DES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, generateKey(secretKey));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        try {
            byte[] buf = cipher.doFinal(NewBase64Util.decode(secretData.toCharArray()));
            return new String(buf);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 加密
     */
    public static byte[] DES_CBC_Encrypt(byte[] content, byte[] keyBytes) {
        try {
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
            SecretKey key = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(DESIV));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            System.err.println("exception:" + e.toString());
        }
        return null;
    }

    /**
     * 解密
     */
    public static byte[] DES_CBC_Decrypt(byte[] content, byte[] keyBytes) {
        try {
            DESKeySpec keySpec = new DESKeySpec(keyBytes);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
            SecretKey key = keyFactory.generateSecret(keySpec);

            Cipher cipher = Cipher.getInstance(PADDING);
            cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(DESIV));
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            System.err.println("exception:" + e.toString());
        }
        return null;
    }

    /**
     * 获得秘密密钥
     *
     * @param secretKey
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     */
    private static SecretKey generateKey(String secretKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES_ALGORITHM);
        DESKeySpec keySpec = new DESKeySpec(secretKey.getBytes());
        keyFactory.generateSecret(keySpec);
        return keyFactory.generateSecret(keySpec);
    }

}