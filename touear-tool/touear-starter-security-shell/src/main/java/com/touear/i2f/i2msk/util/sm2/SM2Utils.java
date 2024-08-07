package com.touear.i2f.i2msk.util.sm2;

import java.io.IOException;
import java.math.BigInteger;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.math.ec.ECPoint;

public class SM2Utils implements java.io.Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7896713903496020924L;
	public ECPoint publicKey;
	public BigInteger privateKey;
	
	
	public SM2Utils() {
		super();
		generateKeyPair();
	}

	//生成随机秘钥对
	public void generateKeyPair(){
		SM2 sm2 = SM2.Instance();
		AsymmetricCipherKeyPair key = sm2.ecc_key_pair_generator.generateKeyPair();
		CipherParameters ecpriv = key.getPrivate();
		CipherParameters ecpub = key.getPublic();
		privateKey = ((ECPrivateKeyParameters) ecpriv).getD();
		publicKey = ((ECPublicKeyParameters) ecpub).getQ();
	}
	public String getPublicKey(){
		return Util.byteToHex(publicKey.getEncoded());
	}
	public String getPrivateKey(){
		return Util.byteToHex(privateKey.toByteArray());
	}
	//数据加密
	public String encrypt(byte[] data) throws IOException
	{
		
		if (data == null || data.length == 0)
		{
			return null;
		}
		
		byte[] source = new byte[data.length];
		System.arraycopy(data, 0, source, 0, data.length);
		
		Cipher cipher = new Cipher();
		SM2 sm2 = SM2.Instance();
		
		ECPoint c1 = cipher.Init_enc(sm2, publicKey);
		cipher.Encrypt(source);
		byte[] c3 = new byte[32];
		cipher.Dofinal(c3);
		//C1 C2 C3拼装成加密字串
		return Util.byteToHex(c1.getEncoded()) + Util.byteToHex(c3) + Util.byteToHex(source);
		
	}
	
	//数据解密
	public byte[] decrypt(byte[] encryptedData) throws IOException
	{
		if (encryptedData == null || encryptedData.length == 0)
		{
			return null;
		}
		//加密字节数组转换为十六进制的字符串 长度变为encryptedData.length * 2
		String data = Util.byteToHex(encryptedData);
		/***分解加密字串
		 * （C1 = C1标志位2位 + C1实体部分128位 = 130）
		 * （C3 = C3实体部分64位  = 64）
		 * （C2 = encryptedData.length * 2 - C1长度  - C2长度）
		 */
		byte[] c1Bytes = Util.hexToByte(data.substring(0,130));
		int c2Len = encryptedData.length - 97;
		byte[] c2 = Util.hexToByte(data.substring(194,194 + 2 * c2Len));
//		System.out.println(data);
		byte[] c3 = Util.hexToByte(data.substring(130,194));
//		System.out.println(data);
//		System.out.println("c1\t"+Util.byteToHex(c1Bytes));
//		System.out.println("c2\t"+Util.byteToHex(c2));
//		System.out.println("c3\t"+Util.byteToHex(c3));
		SM2 sm2 = SM2.Instance();
		
		//通过C1实体字节来生成ECPoint
		ECPoint c1 = sm2.ecc_curve.decodePoint(c1Bytes);
		Cipher cipher = new Cipher();
		cipher.Init_dec(privateKey, c1);
		cipher.Decrypt(c2);
		cipher.Dofinal(c3);
		
		//返回解密结果
		return c2;
	}
}
