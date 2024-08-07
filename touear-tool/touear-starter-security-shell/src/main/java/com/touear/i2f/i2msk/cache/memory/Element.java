package com.touear.i2f.i2msk.cache.memory;

public class Element {
	private String key;
	private Object value;
	/**
	 * 创建时间
	 */
	private long createTime;
	/**
	 * 访问时间
	 */
	private long visitTime;
	
	/**
	 * 空闲多少毫秒后清理
	 */
	private long timeToIdleMsec;
	/**
	 * 最多存在多少毫秒
	 */
	private long timeToLiveMsec;
	
	public Element(String key, Object value, long timeToIdleMsec, long timeToLiveMsec) {
		super();
		long currentTimeMillis=System.currentTimeMillis();
		this.key = key;
		this.value = value;
		this.createTime = currentTimeMillis;
		this.visitTime = currentTimeMillis;
		this.timeToIdleMsec = timeToIdleMsec;
		this.timeToLiveMsec = timeToLiveMsec;
	}
	
	public String getKey() {
		return key;
	}
	public Object getValue() {
		return value;
	}
	public long getCreateTime() {
		return createTime;
	}
	public long getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(long visitTime) {
		this.visitTime = visitTime;
	}

	public long getTimeToIdleMsec() {
		return timeToIdleMsec;
	}

	public long getTimeToLiveMsec() {
		return timeToLiveMsec;
	}

	protected boolean isOverdue() {
		long currentTimeMillis=System.currentTimeMillis();
		if(currentTimeMillis-createTime > timeToLiveMsec){		//超过最大存在时间
			return true;
		}
		if(currentTimeMillis-visitTime > timeToIdleMsec){		//超过最大空闲时间
			return true;
		}
		return false;
	}
}
