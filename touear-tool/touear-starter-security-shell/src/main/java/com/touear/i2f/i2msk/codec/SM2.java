package com.touear.i2f.i2msk.codec;

import com.touear.i2f.i2msk.util.sm2.SM2Utils;
import com.touear.i2f.i2msk.util.sm2.Util;

public class SM2 implements AsymmetryCodec {
	SM2Utils sm2Utils=new SM2Utils();
	@Override
	public String getPublicKey() {
		return sm2Utils.getPublicKey();
	}

	@Override
	public String getPrivateKey() {
		return sm2Utils.getPrivateKey();
	}

	@Override
	public String encoder(String rxpress) {
		try {
			return sm2Utils.encrypt(Util.hexToByte(rxpress));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}


	@Override
	public String decoder(String ciphertext) {
		try {
			return new String(sm2Utils.decrypt(Util.hexToByte(ciphertext)), "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	@Override
	public String getAlgorithmName() {
		return "SM2";
	}


}
