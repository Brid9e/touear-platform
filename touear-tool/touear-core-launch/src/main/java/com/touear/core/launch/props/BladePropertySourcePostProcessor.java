/*
 *      Copyright (c) 2018-2028, DreamLu All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: DreamLu 卢春梦 (596392912@qq.com)
 */

package com.touear.core.launch.props;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 自定义资源文件读取，优先级最低
 *
 * @author Administrator
 */
@Slf4j
class TouearPropertySourcePostProcessor implements BeanFactoryPostProcessor, InitializingBean, Ordered {
	private final ResourceLoader resourceLoader;
	private final List<PropertySourceLoader> propertySourceLoaders;

	public TouearPropertySourcePostProcessor() {
		this.resourceLoader = new DefaultResourceLoader();
		this.propertySourceLoaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, getClass().getClassLoader());
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		log.info("TouearPropertySourcePostProcessor process @TouearPropertySource bean.");
		Map<String, Object> beansWithAnnotation = beanFactory.getBeansWithAnnotation(TouearPropertySource.class);
		Set<Map.Entry<String, Object>> beanEntrySet = beansWithAnnotation.entrySet();
		// 没有 @YmlPropertySource 注解，跳出
		if (beanEntrySet.isEmpty()) {
			log.warn("Not found @TouearPropertySource on spring bean class.");
			return;
		}
		// 组装资源
		List<PropertyFile> propertyFileList = new ArrayList<>();
		for (Map.Entry<String, Object> entry : beanEntrySet) {
			Class<?> beanClass = ClassUtils.getUserClass(entry.getValue());
			TouearPropertySource propertySource = AnnotationUtils.getAnnotation(beanClass, TouearPropertySource.class);
			if (propertySource == null) {
				continue;
			}
			int order = propertySource.order();
			boolean loadActiveProfile = propertySource.loadActiveProfile();
			String location = propertySource.value();
			propertyFileList.add(new PropertyFile(order, location, loadActiveProfile));
		}

		// 装载 PropertySourceLoader
		Map<String, PropertySourceLoader> loaderMap = new HashMap<>(16);
		for (PropertySourceLoader loader : propertySourceLoaders) {
			String[] loaderExtensions = loader.getFileExtensions();
			for (String extension : loaderExtensions) {
				loaderMap.put(extension, loader);
			}
		}
		// 去重，排序
		List<PropertyFile> sortedPropertyList = propertyFileList.stream()
			.distinct()
			.sorted()
			.collect(Collectors.toList());
		ConfigurableEnvironment environment = beanFactory.getBean(ConfigurableEnvironment.class);
		MutablePropertySources propertySources = environment.getPropertySources();

		// 只支持 activeProfiles，没有必要支持 spring.profiles.include。
		String[] activeProfiles = environment.getActiveProfiles();
		ArrayList<PropertySource> propertySourceList = new ArrayList<>();
		for (String profile : activeProfiles) {
			for (PropertyFile propertyFile : sortedPropertyList) {
				// 不加载 ActiveProfile 的配置文件
				if (!propertyFile.loadActiveProfile) {
					continue;
				}
				String extension = propertyFile.getExtension();
				PropertySourceLoader loader = loaderMap.get(extension);
				if (loader == null) {
					throw new IllegalArgumentException("Can't find PropertySourceLoader for PropertySource extension:" + extension);
				}
				String location = propertyFile.getLocation();
				String filePath = StringUtils.stripFilenameExtension(location);
				String profiledLocation = filePath + "-" + profile + "." + extension;
				Resource resource = resourceLoader.getResource(profiledLocation);
				loadPropertySource(profiledLocation, resource, loader, propertySourceList);
			}
		}
		// 本身的 Resource
		for (PropertyFile propertyFile : sortedPropertyList) {
			String extension = propertyFile.getExtension();
			PropertySourceLoader loader = loaderMap.get(extension);
			String location = propertyFile.getLocation();
			Resource resource = resourceLoader.getResource(location);
			loadPropertySource(location, resource, loader, propertySourceList);
		}
		// 转存
		for (PropertySource propertySource : propertySourceList) {
			propertySources.addLast(propertySource);
		}
	}

	private static void loadPropertySource(String location, Resource resource,
										   PropertySourceLoader loader,
										   List<PropertySource> sourceList) {
		if (resource.exists()) {
			String name = "berserkerPropertySource: [" + location + "]";
			try {
				sourceList.addAll(loader.load(name, resource));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("TouearPropertySourcePostProcessor init.");
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}

	@Getter
	@ToString
	@EqualsAndHashCode
	private static class PropertyFile implements Comparable<PropertyFile> {
		private final int order;
		private final String location;
		private final String extension;
		private final boolean loadActiveProfile;

		PropertyFile(int order, String location, boolean loadActiveProfile) {
			this.order = order;
			this.location = location;
			this.loadActiveProfile = loadActiveProfile;
			this.extension = Objects.requireNonNull(StringUtils.getFilenameExtension(location));
		}

		@Override
		public int compareTo(PropertyFile other) {
			return Integer.compare(this.order, other.order);
		}
	}
}
