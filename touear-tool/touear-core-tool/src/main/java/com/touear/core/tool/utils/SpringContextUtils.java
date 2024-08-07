package com.touear.core.tool.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Title: SpringContextUtil.java
 * @Description: spring中Bean的管理
 * @author touear
 * @date 2018年10月23日 下午2:04:12
 * @version 1.0
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

	private static Logger logger = LoggerFactory.getLogger(SpringContextUtils.class);

	private static SpringContextUtils springBeanGetter = null;

	private static ApplicationContext applicationContext;

	private SpringContextUtils() {
	}

	public static SpringContextUtils getInstance() {
		if (springBeanGetter == null) {
			synchronized (SpringContextUtils.class) {
				if (springBeanGetter == null) {
					springBeanGetter = new SpringContextUtils();
				}
			}
		}
		return springBeanGetter;
	}

	/**
	 * 根据id查询context,得到spring bean
	 * 
	 * @author hjin
	 * @cratedate 2013-9-5 下午2:36:59
	 * @param beanid
	 * @return
	 * 
	 */
	@SuppressWarnings("unchecked")
	public <T> T getBean(String beanid) {
		if (applicationContext != null) {
			return (T) applicationContext.getBean(beanid);
		} else {
			logger.info("applicationContext is null");
			return null;
		}
	}

	public <T> T getBean(Class<T> clazz) {
		return getBean(StringUtil.uncapitalize(clazz.getSimpleName()));
	}

	public <T> T getBeanByClass(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}

	public <T> T getBean(String name, Class<T> requiredType) {
		return applicationContext.getBean(name, requiredType);
	}

	public boolean containsBean(String name) {
		return applicationContext.containsBean(name);
	}

	public boolean isSingleton(String name) {
		return applicationContext.isSingleton(name);
	}

	public Class<? extends Object> getType(String name) {
		return applicationContext.getType(name);
	}
	
	/**
	 * 获取应用上下文并获取相应接口的所有实现类
	 * @author wangqq
	 * @date 2018年11月12日 上午10:52:14
	 */
	public <T> Map<String, T> getAllClassByInterface(Class<T> clazz) {
		if (clazz.isInterface()) {
			return applicationContext.getBeansOfType(clazz);
		}
		return null;
	}
	

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContextUtils.applicationContext = applicationContext;
	}

}
