package com.touear.i2f.i2msk.cache.memory;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryCacheManager {
	
	static java.util.Timer clearTimer=new Timer("MemoryCacheClear",true);
	
	static{
		clearTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				for (MemoryCache memoryCache : cacheMap.values()) {
					for (Element element : memoryCache.elementMap.values()) {
						if(element.isOverdue()){
							memoryCache.remove(element.getKey());
						}
					}
				}
			}
		}, 60000,60000);
	}
	
	static java.util.concurrent.ConcurrentHashMap<String, MemoryCache> cacheMap=new ConcurrentHashMap<String, MemoryCache>();

	/**
	 * 根据缓存名称获取缓存
	 * @param cacheName 缓存名称
	 * @return
	 */
	public static MemoryCache getMemoryCache(String cacheName){
		MemoryCache memoryCache=cacheMap.get(cacheName);
		if(memoryCache==null){
			synchronized (cacheName) {
				memoryCache=cacheMap.get(cacheName);
				if(memoryCache==null){
					memoryCache=new MemoryCache();
					cacheMap.put(cacheName, memoryCache);
				}
			}	
		}
		
		return memoryCache;
		
	}
}
