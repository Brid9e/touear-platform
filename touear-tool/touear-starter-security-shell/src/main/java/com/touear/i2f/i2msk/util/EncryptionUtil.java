package com.touear.i2f.i2msk.util;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class EncryptionUtil {

	private static final Character[] dictionaries=new Character[]{'!','"','#','$','%','&','\'','(',')','*','+',',','-','.','/','0','1','2','3','4','5','6','7','8','9',':',';','<','=','>','?','@','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','[','\\',']','^','_','`','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','{','|','}','~'};

	private char[] encryption;

	public EncryptionUtil() {
		super();
		encryption=new char[dictionaries.length*2];   //字典
		Character[] etd=new Character[dictionaries.length];  //偶数集
		Character[] otd=new Character[dictionaries.length];  //奇数集
		System.arraycopy(dictionaries, 0, etd, 0, dictionaries.length);
		System.arraycopy(dictionaries, 0, otd, 0, dictionaries.length);
		int x=UUID.randomUUID().hashCode();
		int x1=UUID.randomUUID().hashCode();
		RandomSortUtil.changePosition(etd, new Random(x));
		RandomSortUtil.changePosition(otd, new Random(x1));
		for (int i = 0; i < encryption.length; i+=2) {
			encryption[i]=etd[i/2];
			encryption[i+1]=otd[i/2];
		}
	}

	public String encoder(List<?> pwd){
		StringBuffer sb=new StringBuffer();
		for (Object object : pwd) {
			for (char c : object.toString().toCharArray()) {
				for (int i = 0; i < encryption.length; i+=2) {
					if(c==encryption[i]){
						sb.append(encryption[i+1]);
						break;
					}
				}
			}
		}
		return sb.toString();
	}
	public String decoder(String pwd){
		StringBuffer ss=new StringBuffer();
		for (char c : pwd.toCharArray()) {
//			if(Keyboard.konggeKey.contains(c)){
//				ss.append(' ');
//				continue;
//			}
			for (int i = 1; i < encryption.length; i+=2) {
				if(c==encryption[i]){
					ss.append(encryption[i-1]);
				}
			}
		}
		return ss.toString();
	}
}
