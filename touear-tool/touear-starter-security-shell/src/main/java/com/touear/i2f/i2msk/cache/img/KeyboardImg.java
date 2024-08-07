package com.touear.i2f.i2msk.cache.img;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.touear.i2f.i2msk.Config;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.IoUtil;

public class KeyboardImg {
	public static Map<String, Object> imageCache = new HashMap<String, Object>();

	static {
		try {
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			InputStream inputStream = KeyboardImg.class.getResourceAsStream("/keyboard/numberKeyboard.png");
			IoUtil.copy(inputStream, byteArray);
			imageCache.put("numberKeyboard", byteArray.toByteArray());
			byteArray.close();
			inputStream.close();

			inputStream = KeyboardImg.class.getResourceAsStream("/keyboard/yLetterKeyboard.png");
			byteArray = new ByteArrayOutputStream();
			IoUtil.copy(inputStream, byteArray);
			imageCache.put("yLetterKeyboard", byteArray.toByteArray());
			byteArray.close();
			inputStream.close();

			inputStream = KeyboardImg.class.getResourceAsStream("/keyboard/XLetterKeyboard.png");
			byteArray = new ByteArrayOutputStream();
			IoUtil.copy(inputStream, byteArray);
			imageCache.put("XLetterKeyboard", byteArray.toByteArray());
			byteArray.close();
			inputStream.close();

			inputStream = KeyboardImg.class.getResourceAsStream("/keyboard/numberKeyboard1.png");
			byteArray = new ByteArrayOutputStream();
			IoUtil.copy(inputStream, byteArray);
			imageCache.put("numberKeyboard1", byteArray.toByteArray());
			byteArray.close();
			inputStream.close();

			inputStream = KeyboardImg.class.getResourceAsStream("/keyboard/symbolKeyboard.png");
			byteArray = new ByteArrayOutputStream();
			IoUtil.copy(inputStream, byteArray);
			imageCache.put("symbolKeyboard", byteArray.toByteArray());
			byteArray.close();
			inputStream.close();

			for (int i = 0; i < 10; i++) {
				inputStream = KeyboardImg.class.getResourceAsStream("/keyboard/number/" + i + ".png");
				BufferedImage image = ImageIO.read(inputStream);
				imageCache.put("number_" + i, image);
				inputStream.close();
			}
		} catch (IOException e) {
			throw new RuntimeException("键盘图片文件错误。无法读取", e);
		}
	}

	/**
	 * 数字图片
	 * 
	 * @param num
	 * @return
	 * @throws IOException
	 * @author chenl
	 * @date 2020-08-20 18:28:43
	 */
	public static String[] createNumberKeyboard(List<Integer> num) throws IOException {
		BufferedImage image = null;
		ByteArrayOutputStream out = null;
		String[] images = new String[10];
		// int r = ramd
		for (int i = 0; i < 10; i++) {
			image = (BufferedImage) imageCache.get("number_" + num.get(i));
			// image = transparentImage((BufferedImage) imageCache.get("number_" +
			// num.get(i)),25);
			out = new ByteArrayOutputStream();
			ImageIO.write(image, "png", out);
			byte[] b = out.toByteArray();
			images[i] = Base64.encode(b);
			out.close();
		}
		return images;
	}

	public static BufferedImage transparentImage(BufferedImage srcImage, int alpha) throws IOException {
		int imgHeight = srcImage.getHeight();// 取得图片的长和宽
		int imgWidth = srcImage.getWidth();
		int c = srcImage.getRGB(3, 3);
		// 防止越位
		if (alpha < 0) {
			alpha = 0;
		} else if (alpha > 10) {
			alpha = 10;
		}
		BufferedImage bi = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_4BYTE_ABGR);// 新建一个类型支持透明的BufferedImage
		for (int i = 0; i < imgWidth; ++i)// 把原图片的内容复制到新的图片，同时把背景设为透明
		{
			for (int j = 0; j < imgHeight; ++j) {
				// 把背景设为透明
				if (srcImage.getRGB(i, j) == c) {
					bi.setRGB(i, j, c & 0x00ffffff);
				}
				// 设置透明度
				else {
					int rgb = bi.getRGB(i, j);
					rgb = ((alpha * 255 / 10) << 24) | (rgb & 0x00ffffff);
					bi.setRGB(i, j, rgb);
				}
			}
		}
		return bi;
	}

	public static String createNumberKeyboardOld(List<Integer> num) throws IOException {
		BufferedImage image = ImageIO.read(new ByteArrayInputStream((byte[]) imageCache.get("numberKeyboard")));
		int index = 0;
		int w = 122;
		int h = 58;
		Graphics g = image.getGraphics();
		g.setColor(Color.black);
		g.setFont(new Font("Arial", 1, Config.numberFontSize));
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				g.drawImage((BufferedImage) imageCache.get("number_" + num.get(index)), j * w + (j * 2) + 2,
						i * h + 3 + (i * 3), null);
				index++;
			}
		}
		g.drawImage((BufferedImage) imageCache.get("number_" + num.get(index)), w + 4, 3 * h + 12, null);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "png", out);
		byte[] b = out.toByteArray();
		return Base64.encode(b);
	}

	protected static final List<Integer> numberKey = Arrays.asList(new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 });
	protected static final List<Character> yLetterKey = Arrays.asList(new Character[] { 'q', 'w', 'e', 'r', 't', 'y',
			'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'v', 'b', 'n', 'm' });
	protected static final List<Character> XLetterKey = Arrays.asList(new Character[] { 'Q', 'W', 'E', 'R', 'T', 'Y',
			'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 'N', 'M' });
	protected static final List<Character> symbolKey = Arrays.asList(new Character[] { '*', '\\', '>', '-', ']', '{',
			'/', '!', ',', '<', '?', '~', '}', '&', '@', '#', '[', '.', ':', '+', '|', '`', '%', '\'' });

	public static boolean binarySearch(char[] a, char c) {
		for (char cc : a) {
			if (cc == c) {
				return true;
			}
		}
		return false;
	}

	public static int size(char c) {
		if (binarySearch("lijtf".toCharArray(), c)) {
			return 30;
		}
		if (binarySearch("wm".toCharArray(), c)) {
			return 20;
		}
		if (binarySearch("qeryuopasdghkzxcvbn".toCharArray(), c)) {
			return 25;
		}
		if (binarySearch("I".toCharArray(), c)) {
			return 33;
		}
		if (binarySearch("W".toCharArray(), c)) {
			return 18;
		}
		if (binarySearch("M".toCharArray(), c)) {
			return 20;
		}
		if (binarySearch("QERTYUOPASDFGHJKLZXCVBN".toCharArray(), c)) {
			return 23;
		}
		if (binarySearch("@%".toCharArray(), c)) {
			return 17;
		}
		if (binarySearch("|:'\\/`!*.]".toCharArray(), c)) {
			return 30;
		}
		if (binarySearch(">-{,<?~}&#[+".toCharArray(), c)) {
			return 25;
		}
		return 20;
	}

	/*
	 * public static void main(String[] args) {
	 * System.out.println(drawStringPic('A',0)); }
	 */

	public static String drawStringPic(Character drawStr, int size) {
		int width = 200;
		int height = 200;
		try {
			BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D gd = buffImg.createGraphics();

			// 设置透明 start
			buffImg = gd.getDeviceConfiguration().createCompatibleImage(width, height, Transparency.TRANSLUCENT);
			gd = buffImg.createGraphics();
			// 设置透明 end
			Font font = new Font("Arial", 0, Config.standardFontSize + size);
			gd.setFont(font); // 设置字体
			gd.setColor(new Color(0x333333)); // 设置颜色
			// gd.drawRect(0, 0, width - 1, height - 1); //画边框
			FontMetrics fm = gd.getFontMetrics(font);

			// FontDesignMetrics fm = FontDesignMetrics.getMetrics(font);

			String text = drawStr.toString();
			int textWidth = fm.stringWidth(text);
			int widthX = (width - textWidth) / 2;
			int textHeight = fm.getHeight();
			int heightY = (height - textHeight) / 2 + fm.getAscent();
			gd.drawString(text, widthX, heightY); // 输出文字

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(buffImg, "png", out);
			byte[] b = out.toByteArray();
			out.close();
			// 释放对象
			gd.dispose();
			return Base64.encode(b);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 字母
	 * 
	 * @param letter
	 * @return
	 * @throws IOException
	 * @author chenl
	 * @date 2020-08-20 17:33:11
	 */
	public static String[] createLowerLetterKeyboard(List<Character> letter) throws IOException {
		String[] images = new String[26];
		for (int i = 0; i < 26; i++) {
			images[i] = drawStringPic(letter.get(i), 10);
		}
		return images;
	}

	public static String[] createUpperLetterKeyboard(List<Character> letter) throws IOException {
		String[] images = new String[26];
		for (int i = 0; i < 26; i++) {
			images[i] = drawStringPic(letter.get(i), 0);
		}
		return images;
	}

	/**
	 * 小写
	 * 
	 * @param letter
	 * @param c
	 * @return
	 * @throws IOException
	 * @author chenl
	 * @date 2020-08-20 17:33:11
	 */
	public static String createLetterKeyboardOld(List<Character> letter, char c) throws IOException {
		BufferedImage image = ImageIO.read(new ByteArrayInputStream((byte[]) imageCache.get(c + "LetterKeyboard")));
		// Graphics g = image.getGraphics();
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		g.setColor(Color.black);
		g.setFont(new Font("Arial", 1, Config.standardFontSize));
		int w = 73;
		int j = 4;
		int index = 0;
		for (int i = 0; i < 10; i++) {
			g.drawString(letter.get(index) + "", i * w + i * j + size(((Character) letter.get(index)).charValue()), 70);
			index++;
		}
		for (int i = 0; i < 9; i++) {
			g.drawString(letter.get(index) + "", 38 + i * w + i * j + size(((Character) letter.get(index)).charValue()),
					180);
			index++;
		}
		for (int i = 0; i < 7; i++) {
			g.drawString(letter.get(index) + "",
					115 + i * w + i * j + size(((Character) letter.get(index)).charValue()), 290);
			index++;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "png", out);
		byte[] b = out.toByteArray();
		return Base64.encode(b);
	}

	public static String[] createExtendNumberKeyboard(List<Integer> num) throws IOException {
		BufferedImage image = null;
		ByteArrayOutputStream out = null;
		String[] images = new String[10];
		// int r = ramd
		for (int i = 0; i < 10; i++) {
			image = (BufferedImage) imageCache.get("number_" + num.get(i));
			// image = transparentImage((BufferedImage) imageCache.get("number_" +
			// num.get(i)),25);
			out = new ByteArrayOutputStream();
			ImageIO.write(image, "png", out);
			byte[] b = out.toByteArray();
			images[i] = Base64.encode(b);
			out.close();
		}
		return images;
	}

	public static String createExtendNumberKeyboardOld(List<Integer> num, List<Character> extendnumberKey)
			throws IOException {
		BufferedImage image = ImageIO.read(new ByteArrayInputStream((byte[]) imageCache.get("numberKeyboard1")));
		Graphics g = image.getGraphics();
		g.setColor(Color.black);
		g.setFont(new Font("Arial", 1, Config.standardFontSize));
		int w = 144;
		int j = 4;
		int index = 0;
		for (int i = 0; i < 5; i++) {
			g.drawString(num.get(index) + "", 15 + i * w + i * j + w / 2 - 10, 70);
			index++;
		}
		for (int i = 0; i < 5; i++) {
			g.drawString(num.get(index) + "", 15 + i * w + i * j + w / 2 - 10, 180);
			index++;
		}
		index = 0;
		w = 73;
		j = 4;
		for (int i = 0; i < 7; i++) {
			g.drawString(extendnumberKey.get(index) + "",
					115 + i * w + i * j + size(((Character) extendnumberKey.get(index)).charValue()), 290);
			index++;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "png", out);
		byte[] b = out.toByteArray();
		return Base64.encode(b);
	}

	public static String[] createSymbolKeyboard(List<Character> symbolKey) throws IOException {
		String[] images = new String[29];
		for (int i = 0; i < 29; i++) {
			images[i] = drawStringPic(symbolKey.get(i), 0);
		}
		return images;
	}

	public static String createSymbolKeyboardOld(List<Character> symbolKey) throws IOException {
		BufferedImage image = ImageIO.read(new ByteArrayInputStream((byte[]) imageCache.get("symbolKeyboard")));
		Graphics g = image.getGraphics();
		g.setColor(Color.black);
		g.setFont(new Font("Arial", 1, Config.standardFontSize));
		int w = 73;
		int j = 4;
		int index = 0;
		for (int i = 0; i < 10; i++) {
			g.drawString(symbolKey.get(index) + "",
					i * w + i * j + size(((Character) symbolKey.get(index)).charValue()), 70);
			index++;
		}
		j = 5;
		for (int i = 0; i < 9; i++) {
			g.drawString(symbolKey.get(index) + "",
					38 + i * w + i * j - i * 1 + size(((Character) symbolKey.get(index)).charValue()), 180);

			index++;
		}
		j = 4;
		for (int i = 0; i < 5; i++) {
			g.drawString(symbolKey.get(index) + "",
					188 + i * w + i * j + size(((Character) symbolKey.get(index)).charValue()), 290);
			index++;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(image, "png", out);
		byte[] b = out.toByteArray();
		return Base64.encode(b);
	}
}
