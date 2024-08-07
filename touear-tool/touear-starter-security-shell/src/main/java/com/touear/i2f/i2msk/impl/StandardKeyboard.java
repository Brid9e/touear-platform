package com.touear.i2f.i2msk.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.touear.i2f.i2msk.Keyboard;
import com.touear.i2f.i2msk.cache.img.KeyboardImg;
import com.touear.i2f.i2msk.enums.KeyboardEnum;

public class StandardKeyboard extends Keyboard {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2202273499641819869L;
	List<Integer> randomNumberKey;
	List<Character> randomyLetterKey;
	List<Character> randomXLetterKey;
	List<Character> randomSymbolKey;
	int index;

	public StandardKeyboard(KeyboardEnum keyboardEnum) {
		super(keyboardEnum);
		this.randomNumberKey = new ArrayList<Integer>(numberKey);
		this.randomyLetterKey = new ArrayList<Character>(yLetterKey);
		this.randomXLetterKey = new ArrayList<Character>(XLetterKey);
		this.randomSymbolKey = new ArrayList<Character>(symbolKey);

		if (keyboardEnum == KeyboardEnum.STANDARD_DISORDER_RSA || keyboardEnum == KeyboardEnum.STANDARD_DISORDER_SM2
				|| keyboardEnum == KeyboardEnum.STANDARD_DISORDER) {
			Collections.shuffle(randomNumberKey);
			Collections.shuffle(randomyLetterKey);
			Collections.shuffle(randomXLetterKey);
			Collections.shuffle(randomSymbolKey);
		}

		index = new Random().nextInt(randomSymbolKey.size() - 7);
	}

	@Override
	public Map<String, Object> getKeyboardImage() {

		Map<String, Object> sb = new HashMap<String, Object>();

		try {
			// List<Character> tmp = symbolKey.subList(index, index + 7);

			sb.put("lowerLetterKeyboardImage", KeyboardImg.createLowerLetterKeyboard(randomyLetterKey));
			sb.put("lowerLetterKeyboard", encryptionUtil.encoder(randomyLetterKey));

			sb.put("upperLetterKeyboardImage", KeyboardImg.createUpperLetterKeyboard(randomXLetterKey));
			sb.put("upperLetterKeyboard", encryptionUtil.encoder(randomXLetterKey));

			sb.put("numberKeyboardImage", KeyboardImg.createExtendNumberKeyboard(randomNumberKey));
			sb.put("numberKeyboard", encryptionUtil.encoder(randomNumberKey));

			sb.put("symbolKeyboardImage", KeyboardImg.createSymbolKeyboard(randomSymbolKey));
			sb.put("symbolKeyboard", encryptionUtil.encoder(randomSymbolKey));

		} catch (IOException e) {
			throw new RuntimeException("键盘图片文件错误。无法读取", e);
		}
		return sb;
	}
}
