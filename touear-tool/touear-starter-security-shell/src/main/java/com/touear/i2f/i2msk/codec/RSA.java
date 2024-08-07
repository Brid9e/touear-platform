package com.touear.i2f.i2msk.codec;

import com.touear.i2f.i2msk.util.RSAUtils;

public class RSA implements AsymmetryCodec {
	RSAUtils rsaUtil=new RSAUtils();
	@Override
	public String getPublicKey() {
		return rsaUtil.generateBase64PublicKey();
	}

	@Override
	public String getPrivateKey() {
		return rsaUtil.generateBase64PrviateKey();
	}
	@Override
	public String encoder(String rxpress) {
		return null;
	}

	@Override
	public String decoder(String ciphertext) {
		String s=rsaUtil.decryptBase64(ciphertext);
		return s;
	}

	@Override
	public String getAlgorithmName() {
		return "RSA";
	}

}
