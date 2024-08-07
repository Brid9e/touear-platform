package com.touear.core.tool.utils;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.lang.Nullable;

import com.touear.core.tool.beans.AbstractBladeBeanCopier;
import com.touear.core.tool.beans.AbstractBladeBeanMap;
import com.touear.core.tool.beans.BeanProperty;
import com.touear.core.tool.convert.BladeConverter;

import lombok.experimental.UtilityClass;

/**
 * 实体工具类
 *
 * @author L.cm
 */
@UtilityClass
public class BeanUtil extends org.springframework.beans.BeanUtils {

	/**
	 * 实例化对象
	 *
	 * @param clazz 类
	 * @param <T>   泛型标记
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T newInstance(Class<?> clazz) {
		return (T) instantiateClass(clazz);
	}

	/**
	 * 实例化对象
	 *
	 * @param clazzStr 类名
	 * @param <T>      泛型标记
	 * @return 对象
	 */
	public static <T> T newInstance(String clazzStr) {
		try {
			Class<?> clazz = Class.forName(clazzStr);
			return newInstance(clazz);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取Bean的属性, 支持 propertyName 多级 ：test.user.name
	 *
	 * @param bean         bean
	 * @param propertyName 属性名
	 * @return 属性值
	 */
	@Nullable
	public static Object getProperty(@Nullable Object bean, String propertyName) {
		if (bean == null) {
			return null;
		}
		BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		return beanWrapper.getPropertyValue(propertyName);
	}

	/**
	 * 设置Bean属性, 支持 propertyName 多级 ：test.user.name
	 *
	 * @param bean         bean
	 * @param propertyName 属性名
	 * @param value        属性值
	 */
	public static void setProperty(Object bean, String propertyName, Object value) {
		Objects.requireNonNull(bean, "bean Could not null");
		BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(bean);
		beanWrapper.setPropertyValue(propertyName, value);
	}

	/**
	 * 深复制
	 *
	 * <p>
	 * 支持 map bean
	 * </p>
	 *
	 * @param source 源对象
	 * @param <T>    泛型标记
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	public static <T> T clone(@Nullable T source) {
		if (source == null) {
			return null;
		}
		return (T) BeanUtil.copy(source, source.getClass());
	}

	/**
	 * copy 对象属性，默认不使用Convert
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param source 源对象
	 * @param clazz  类名
	 * @param <T>    泛型标记
	 * @return T
	 */
	@Nullable
	public static <T> T copy(@Nullable Object source, Class<T> clazz) {
		if (source == null) {
			return null;
		}
		return BeanUtil.copy(source, source.getClass(), clazz);
	}

	/**
	 * copy 对象属性，默认不使用Convert
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param source      源对象
	 * @param sourceClazz 源类型
	 * @param targetClazz 转换成的类型
	 * @param <T>         泛型标记
	 * @return T
	 */
	@Nullable
	public static <T> T copy(@Nullable Object source, Class<?> sourceClazz, Class<T> targetClazz) {
		if (source == null) {
			return null;
		}
		AbstractBladeBeanCopier copier = AbstractBladeBeanCopier.create(sourceClazz, targetClazz, false);
		T to = newInstance(targetClazz);
		copier.copy(source, to, null);
		return to;
	}

	/**
	 * copy 列表对象，默认不使用Convert
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param sourceList  源列表
	 * @param targetClazz 转换成的类型
	 * @param <T>         泛型标记
	 * @return T
	 */
	public static <T> List<T> copy(@Nullable Collection<?> sourceList, Class<T> targetClazz) {
		if (sourceList == null || sourceList.isEmpty()) {
			return Collections.emptyList();
		}
		List<T> outList = new ArrayList<>(sourceList.size());
		Class<?> sourceClazz = null;
		for (Object source : sourceList) {
			if (source == null) {
				continue;
			}
			if (sourceClazz == null) {
				sourceClazz = source.getClass();
			}
			T bean = BeanUtil.copy(source, sourceClazz, targetClazz);
			outList.add(bean);
		}
		return outList;
	}

	/**
	 * 拷贝对象
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param source     源对象
	 * @param targetBean 需要赋值的对象
	 */
	public static void copy(@Nullable Object source, @Nullable Object targetBean) {
		if (source == null || targetBean == null) {
			return;
		}
		AbstractBladeBeanCopier copier = AbstractBladeBeanCopier
			.create(source.getClass(), targetBean.getClass(), false);

		copier.copy(source, targetBean, null);
	}

	/**
	 * 拷贝对象，source 属性做 null 判断，Map 不支持，map 会做 instanceof 判断，不会
	 *
	 * <p>
	 * 支持 bean copy
	 * </p>
	 *
	 * @param source     源对象
	 * @param targetBean 需要赋值的对象
	 */
	public static void copyNonNull(@Nullable Object source, @Nullable Object targetBean) {
		if (source == null || targetBean == null) {
			return;
		}
		AbstractBladeBeanCopier copier = AbstractBladeBeanCopier
			.create(source.getClass(), targetBean.getClass(), false, true);

		copier.copy(source, targetBean, null);
	}

	/**
	 * 拷贝对象并对不同类型属性进行转换
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param source      源对象
	 * @param targetClazz 转换成的类
	 * @param <T>         泛型标记
	 * @return T
	 */
	@Nullable
	public static <T> T copyWithConvert(@Nullable Object source, Class<T> targetClazz) {
		if (source == null) {
			return null;
		}
		return BeanUtil.copyWithConvert(source, source.getClass(), targetClazz);
	}

	/**
	 * 拷贝对象并对不同类型属性进行转换
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param source      源对象
	 * @param sourceClazz 源类
	 * @param targetClazz 转换成的类
	 * @param <T>         泛型标记
	 * @return T
	 */
	@Nullable
	public static <T> T copyWithConvert(@Nullable Object source, Class<?> sourceClazz, Class<T> targetClazz) {
		if (source == null) {
			return null;
		}
		AbstractBladeBeanCopier copier = AbstractBladeBeanCopier.create(sourceClazz, targetClazz, true);
		T to = newInstance(targetClazz);
		copier.copy(source, to, new BladeConverter(targetClazz));
		return to;
	}

	/**
	 * 拷贝列表并对不同类型属性进行转换
	 *
	 * <p>
	 * 支持 map bean copy
	 * </p>
	 *
	 * @param sourceList  源对象列表
	 * @param targetClazz 转换成的类
	 * @param <T>         泛型标记
	 * @return List
	 */
	public static <T> List<T> copyWithConvert(@Nullable Collection<?> sourceList, Class<T> targetClazz) {
		if (sourceList == null || sourceList.isEmpty()) {
			return Collections.emptyList();
		}
		List<T> outList = new ArrayList<>(sourceList.size());
		Class<?> sourceClazz = null;
		for (Object source : sourceList) {
			if (source == null) {
				continue;
			}
			if (sourceClazz == null) {
				sourceClazz = source.getClass();
			}
			T bean = BeanUtil.copyWithConvert(source, sourceClazz, targetClazz);
			outList.add(bean);
		}
		return outList;
	}

	/**
	 * Copy the property values of the given source bean into the target class.
	 * <p>Note: The source and target classes do not have to match or even be derived
	 * from each other, as long as the properties match. Any bean properties that the
	 * source bean exposes but the target bean does not will silently be ignored.
	 * <p>This is just a convenience method. For more complex transfer needs,
	 *
	 * @param source      the source bean
	 * @param targetClazz the target bean class
	 * @param <T>         泛型标记
	 * @return T
	 * @throws BeansException if the copying failed
	 */
	@Nullable
	public static <T> T copyProperties(@Nullable Object source, Class<T> targetClazz) throws BeansException {
		if (source == null) {
			return null;
		}
		T to = newInstance(targetClazz);
		BeanUtil.copyProperties(source, to);
		return to;
	}

	/**
	 * Copy the property values of the given source bean into the target class.
	 * <p>Note: The source and target classes do not have to match or even be derived
	 * from each other, as long as the properties match. Any bean properties that the
	 * source bean exposes but the target bean does not will silently be ignored.
	 * <p>This is just a convenience method. For more complex transfer needs,
	 *
	 * @param sourceList  the source list bean
	 * @param targetClazz the target bean class
	 * @param <T>         泛型标记
	 * @return List
	 * @throws BeansException if the copying failed
	 */
	public static <T> List<T> copyProperties(@Nullable Collection<?> sourceList, Class<T> targetClazz) throws BeansException {
		if (sourceList == null || sourceList.isEmpty()) {
			return Collections.emptyList();
		}
		List<T> outList = new ArrayList<>(sourceList.size());
		for (Object source : sourceList) {
			if (source == null) {
				continue;
			}
			T bean = BeanUtil.copyProperties(source, targetClazz);
			outList.add(bean);
		}
		return outList;
	}

	/**
	 * 将对象装成map形式
	 *
	 * @param bean 源对象
	 * @return {Map}
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> toMap(@Nullable Object bean) {
		if (bean == null) {
			return new HashMap<>(0);
		}
		return AbstractBladeBeanMap.create(bean);
	}

	/**
	 * 将map 转为 bean
	 *
	 * @param beanMap   map
	 * @param valueType 对象类型
	 * @param <T>       泛型标记
	 * @return {T}
	 */
	public static <T> T toBean(Map<String, Object> beanMap, Class<T> valueType) {
		Objects.requireNonNull(beanMap, "beanMap Could not null");
		T to = newInstance(valueType);
		if (beanMap.isEmpty()) {
			return to;
		}
		BeanUtil.copy(beanMap, to);
		return to;
	}

	/**
	 * 给一个Bean添加字段
	 *
	 * @param superBean 父级Bean
	 * @param props     新增属性
	 * @return {Object}
	 */
	@Nullable
	public static Object generator(@Nullable Object superBean, BeanProperty... props) {
		if (superBean == null) {
			return null;
		}
		Class<?> superclass = superBean.getClass();
		Object genBean = generator(superclass, props);
		BeanUtil.copy(superBean, genBean);
		return genBean;
	}

	/**
	 * 给一个class添加字段
	 *
	 * @param superclass 父级
	 * @param props      新增属性
	 * @return {Object}
	 */
	public static Object generator(Class<?> superclass, BeanProperty... props) {
		BeanGenerator generator = new BeanGenerator();
		generator.setSuperclass(superclass);
		generator.setUseCache(true);
		for (BeanProperty prop : props) {
			generator.addProperty(prop.getName(), prop.getType());
		}
		return generator.create();
	}
	/**
	 * 利用org.apache.commons.beanutils 工具类实现 Map --> Bean 
	 * @param map
	 * @param obj
	 */
/*    public static void transMap2Bean2(Map<String, Object> map, Object obj) {  
        if (map == null || obj == null) {  
            return;  
        }  
        try {  
            BeanUtils.populate(obj, map);  
        } catch (Exception e) {  
            System.out.println("transMap2Bean2 Error " + e);  
        }  
    } */
    
    /**
     * 利用Introspector,PropertyDescriptor实现 Map --> Bean
     * @param map
     * @param obj
     */
    public static void transMap2Bean(Map<String, Object> map, Object obj) {  
  
        try {  
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
  
            for (PropertyDescriptor property : propertyDescriptors) {  
                String key = property.getName();  
  
                if (map.containsKey(key)) {  
                    Object value = map.get(key);  
                    // 得到property对应的setter方法  
                    Method setter = property.getWriteMethod();  
                    setter.invoke(obj, value);  
                }  
  
            }  
  
        } catch (Exception e) {  
            System.out.println("transMap2Bean Error " + e);  
        }  
  
        return;  
  
    }  
    
    /**
     * 利用Introspector和PropertyDescriptor 将Bean --> Map  
     * @param obj
     * @return
     */
    public static Map<String, Object> transBean2Map(Object obj) {  
  
        if(obj == null){  
            return null;  
        }          
        Map<String, Object> map = new HashMap<String, Object>();  
        try {  
            BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());  
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();  
            for (PropertyDescriptor property : propertyDescriptors) {  
                String key = property.getName();  
  
                // 过滤class属性  
                if (!Func.equals("class", key)) {  
                    // 得到property对应的getter方法  
                    Method getter = property.getReadMethod();  
                    Object value = getter.invoke(obj);  
  
                    map.put(key, value);  
                }  
  
            }  
        } catch (Exception e) {  
            System.out.println("transBean2Map Error " + e);  
        }  
  
        return map;  
  
    } 
}
