package com.touear.i2f.i2msk.cache.memory;

import java.util.concurrent.ConcurrentHashMap;

import com.touear.i2f.i2msk.Config;
import com.touear.i2f.i2msk.cache.Cache;

public class MemoryCache implements Cache{
	
	ConcurrentHashMap<String, Element> elementMap=new ConcurrentHashMap<String, Element>();
	/**
	 * 默认值
	 */
	public MemoryCache() {
		super();
	}

	public Object get(String key){
		Element element=getElement(key);
		if(element==null){
			return null;
		}
		return element.getValue();
	}
	public Element getElement(String key){
		Element element=elementMap.get(key);
		if(element == null){
			return null;
		}
		if(element.isOverdue()){
			return null;
		}
		element.setVisitTime(System.currentTimeMillis());
		return element;
	}
	/**
	 * 设置缓存
	 * @param key 
	 * @param value
	 * @param timeToIdleMsec 空闲多少毫秒后清理
	 * @param timeToLiveMsec 最多存在多少毫秒
	 */
	public void put(String key, Object value, long timeToIdleMsec, long timeToLiveMsec){
		elementMap.put(key, new Element(key, value, timeToIdleMsec, timeToLiveMsec));
	}
	/**
	 * 设置缓存
	 * @param key 
	 * @param value
	 */
	public void put(String key, Object value){
		elementMap.put(key, new Element(key, value, Config.securityKeyboardOverdue, Config.securityKeyboardOverdue));
	}
	
	public void remove(String key){
		elementMap.remove(key);	
	}
	
}
