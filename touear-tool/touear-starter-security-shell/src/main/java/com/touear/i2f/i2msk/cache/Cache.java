package com.touear.i2f.i2msk.cache;

public interface Cache {
	/**
	 * 通过key从缓存中获取对象
	 * @param key
	 * @return
	 */
	
	public Object get(String key);
	/**
	 * 向缓存添加对象
	 * @param key
	 * @param value
	 */
	public void put(String key,Object value);
	/**
	 * 删除对象
	 * @param key
	 */
	public void remove(String key);
}
