package com.touear.i2f.i2msk;

import com.touear.core.tool.exception.ServiceException;
import com.touear.i2f.i2msk.codec.AsymmetryCodec;
import com.touear.i2f.i2msk.enums.KeyboardEnum;
import com.touear.i2f.i2msk.enums.ScheduleKeyboardEnum;
import com.touear.i2f.i2msk.impl.NumberKeyboard;
import com.touear.i2f.i2msk.impl.StandardKeyboard;
import com.touear.i2f.i2msk.util.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
public abstract class Keyboard implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7667455458556801514L;

	static Timer createKeyboard = new Timer("createKeyboard", true);
	static Map<KeyboardEnum, ConcurrentLinkedQueue<Keyboard>> keyboardQueueMap = new HashMap<KeyboardEnum, ConcurrentLinkedQueue<Keyboard>>();

	static {
		createKeyboard.schedule(new TimerTask() {

			@Override
			public void run() {
				for (ScheduleKeyboardEnum scheduleKeyboardEnum : ScheduleKeyboardEnum.values()) {
					KeyboardEnum keyboardEnum = KeyboardEnum.valueOf(scheduleKeyboardEnum.name());
					ConcurrentLinkedQueue<Keyboard> keyboardQueue = keyboardQueueMap.get(keyboardEnum);
					if (keyboardQueue == null) {
						keyboardQueue = new ConcurrentLinkedQueue<Keyboard>();
						keyboardQueueMap.put(keyboardEnum, keyboardQueue);
					}
					for (int i = keyboardQueue.size(); i < Config.cacheSize; i++) {
						// log.info(keyboardEnum.name() + "----->" + i);
						keyboardQueue.add(keyboardFactory(keyboardEnum));
					}
				}

				/*
				 * for (KeyboardEnum keyboardEnum : KeyboardEnum.values()) {
				 * ConcurrentLinkedQueue<Keyboard> keyboardQueue =
				 * keyboardQueueMap.get(keyboardEnum); if (keyboardQueue == null) {
				 * keyboardQueue = new ConcurrentLinkedQueue<Keyboard>();
				 * keyboardQueueMap.put(keyboardEnum, keyboardQueue); } for (int i =
				 * keyboardQueue.size(); i < Config.cacheSize; i++) {
				 * log.info(keyboardEnum.name() + "----->" + i);
				 * keyboardQueue.add(keyboardFactory(keyboardEnum)); } }
				 */
			}
		}, 0, Config.pollingTime);
	}

	private static Keyboard keyboardFactory(KeyboardEnum keyboardEnum) {
		Keyboard keyboard = null;
		switch (keyboardEnum) {
		case NUMBER_DISORDER_RSA:
		case NUMBER_DISORDER_SM2:
		case NUMBER_DISORDER:
		case NUMBER_ORDER_RSA:
		case NUMBER_ORDER_SM2:
		case NUMBER_ORDER:
			keyboard = new NumberKeyboard(keyboardEnum);
			break;
		case STANDARD_DISORDER_RSA:
		case STANDARD_DISORDER_SM2:
		case STANDARD_DISORDER:
		case STANDARD_ORDER_RSA:
		case STANDARD_ORDER_SM2:
		case STANDARD_ORDER:
			keyboard = new StandardKeyboard(keyboardEnum);
			break;
		}
		keyboard.init();
		return keyboard;
	}

	public static Map<String, Object> createKeyboard(KeyboardEnum keyboardEnum) {
		if (keyboardEnum == null) {
			throw new IllegalArgumentException("键盘类型不允许为NULL：");
		}
		Keyboard keyboard = null;
		if (keyboardQueueMap.get(keyboardEnum) != null) {
			keyboard = keyboardQueueMap.get(keyboardEnum).poll();
		}
		if (keyboard == null) {
			keyboard = keyboardFactory(keyboardEnum);
		}
		if (keyboard == null) {
			throw new IllegalArgumentException("不存在该类型键盘：" + keyboardEnum.toString());
		}
		return keyboard.buildKeypad();
	}

	public static String[] decoderPwd(boolean remove, String... pwdItem) {
		String[] rp = new String[pwdItem.length];
		for (int i = 0; i < pwdItem.length; i++) {
			String[] p = pwdItem[i].split("!#!");
			log.info("pwdItem["+i+"]:"+pwdItem[i]);
			Object keyboard = Config.keyboardCache.get(p[1]);
			if (keyboard == null) {
				throw new ServiceException("键盘已经过期");
			}
			if (i == pwdItem.length - 1 && remove) {
				Config.keyboardCache.remove(p[1]);
			}
			rp[i] = ((Keyboard) keyboard).getPwd(p[0]);
			// rp[i] = rp[i].substring(0, rp[i].length() - 13);
		}
		return rp;
	}

	protected final static List<Integer> numberKey = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
	protected final static List<Character> yLetterKey = Arrays.asList('q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p',
			'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm');
	protected final static List<Character> XLetterKey = Arrays.asList('Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P',
			'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M');
	protected final static List<Character> symbolKey = Arrays.asList('*', '\\',  '-', '[',']', '{','}', '/', '!', ',', '<','>',
			'?', '~',  '&', '@', '#',  '.', ':', '+', '|', '`', '%', '\'','$',';','^','"','_');
	public final static List<Character> konggeKey = Arrays.asList('^', '。', '，', '‘');

	protected EncryptionUtil encryptionUtil;
	String uuid;
	AsymmetryCodec asymmetryCodec;
	KeyboardEnum keyboardEnum;
	StringBuilder dataStr = new StringBuilder();
	Map<String, Object> data = new HashMap<String, Object>();

	public Keyboard(KeyboardEnum keyboardEnum) {
		super();
		try {
			this.keyboardEnum = keyboardEnum;
			encryptionUtil = new EncryptionUtil();
			uuid = UUID.randomUUID().toString();
			switch (keyboardEnum) {
			case NUMBER_DISORDER_RSA:
			case NUMBER_ORDER_RSA:
			case STANDARD_DISORDER_RSA:
			case STANDARD_ORDER_RSA:
				asymmetryCodec = Config.rsaClass.newInstance();
				break;
			case NUMBER_DISORDER_SM2:
			case NUMBER_ORDER_SM2:
			case STANDARD_DISORDER_SM2:
			case STANDARD_ORDER_SM2:
				asymmetryCodec = Config.sm2Class.newInstance();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Map<String, Object> buildKeypad() {
		Map<String, Object> t = data;
		close();
		return t;
	}

	private void close() {
		data = null;
		if (keyboardQueueMap.get(keyboardEnum) != null) {
			keyboardQueueMap.get(keyboardEnum).remove(this);
		}
		Config.keyboardCache.put(uuid, this);

	}

	protected abstract Map<String, Object> getKeyboardImage();

	private String getPwd(String pwd) {
		// String decryptedPass = asymmetryCodec.decoder(pwd);
		return encryptionUtil.decoder(pwd);
	}

	private void init() {
		dataStr.append(getKeyboardImage());
		dataStr.append("!#!");
		dataStr.append(uuid);
		dataStr.append("!#!");
		dataStr.append(konggeKey.get(randomInt(0, konggeKey.size())));
		dataStr.append("!#!");
		dataStr.append(this.keyboardEnum.name());
		data.putAll(getKeyboardImage());
		data.put("uuid", uuid);

	}

	public static int randomInt(int min, int max) {
		Random random = new Random();
		return random.nextInt(max) % (max - min) + min;
	}
}
