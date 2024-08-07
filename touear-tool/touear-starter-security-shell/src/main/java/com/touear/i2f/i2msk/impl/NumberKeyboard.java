package com.touear.i2f.i2msk.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.touear.i2f.i2msk.Keyboard;
import com.touear.i2f.i2msk.cache.img.KeyboardImg;
import com.touear.i2f.i2msk.enums.KeyboardEnum;

public class NumberKeyboard extends Keyboard{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 3662628393546263525L;
	private List<Integer> randomKey;

	public NumberKeyboard(KeyboardEnum keyboardEnum)  {
		super(keyboardEnum);
		randomKey=new ArrayList<Integer>(Keyboard.numberKey);
		if(keyboardEnum == KeyboardEnum.NUMBER_DISORDER_RSA || keyboardEnum == KeyboardEnum.NUMBER_DISORDER_SM2 || keyboardEnum == KeyboardEnum.NUMBER_DISORDER){
			Collections.shuffle(randomKey);
		}
	}

	@Override
	public Map<String,Object> getKeyboardImage() {
		Map<String,Object> sb = new HashMap<String, Object>();
		try {
			sb.put("numberKeyboardImage", KeyboardImg.createNumberKeyboard(randomKey));
			sb.put("numberKeyboard", encryptionUtil.encoder(randomKey));
			return sb;
		} catch (IOException e) {
			throw new RuntimeException("键盘图片文件错误。无法读取",e);
		}
		
		
	}
}
