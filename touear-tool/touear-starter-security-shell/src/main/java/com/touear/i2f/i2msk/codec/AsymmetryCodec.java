package com.touear.i2f.i2msk.codec;

/**
 * 非对称加密接口
 * 需要一个无参的构造函数
 */
public interface AsymmetryCodec {
	
	/**
	 * 获取算法名称
	 * @return
	 */
	public String getAlgorithmName();
	
	public String getPublicKey();
	public String getPrivateKey();
	/**
	 * 编码
	 * @param rxpress 需要编码的数据
	 * @return 密文
	 */
	public String encoder(String rxpress);
	/**
	 * 解码
	 * @param ciphertext 需要解密密文
	 * @return 明文
	 */
	public String decoder(String ciphertext);
}
