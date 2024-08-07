package com.touear.core.tool.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.core.convert.Property;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.lang.Nullable;
import org.springframework.util.ReflectionUtils;

import lombok.experimental.UtilityClass;

/**
 * 反射工具类
 *
 * @author L.cm
 */
@UtilityClass
public class ReflectUtil extends ReflectionUtils {

	/**
	 * 获取 Bean 的所有 get方法
	 *
	 * @param type 类
	 * @return PropertyDescriptor数组
	 */
	public static <T> PropertyDescriptor[] getBeanGetters(Class<T> type) {
		return getPropertiesHelper(type, true, false);
	}

	/**
	 * 获取 Bean 的所有 set方法
	 *
	 * @param type 类
	 * @return PropertyDescriptor数组
	 */
	public static <T> PropertyDescriptor[] getBeanSetters(Class<T> type) {
		return getPropertiesHelper(type, false, true);
	}

	/**
	 * 获取 Bean 的所有 PropertyDescriptor
	 *
	 * @param type 类
	 * @param read 读取方法
	 * @param write 写方法
	 * @return PropertyDescriptor数组
	 */
	public static <T> PropertyDescriptor[] getPropertiesHelper(Class<T> type, boolean read, boolean write) {
		try {
			PropertyDescriptor[] all = BeanUtil.getPropertyDescriptors(type);
			if (read && write) {
				return all;
			} else {
				List<PropertyDescriptor> properties = new ArrayList<>(all.length);
				for (PropertyDescriptor pd : all) {
					if (read && pd.getReadMethod() != null) {
						properties.add(pd);
					} else if (write && pd.getWriteMethod() != null) {
						properties.add(pd);
					}
				}
				return properties.toArray(new PropertyDescriptor[0]);
			}
		} catch (BeansException ex) {
			throw new CodeGenerationException(ex);
		}
	}

	/**
	 * 获取 bean 的属性信息
	 * @param propertyType 类型
	 * @param propertyName 属性名
	 * @return {Property}
	 */
	@Nullable
	public static Property getProperty(Class<?> propertyType, String propertyName) {
		PropertyDescriptor propertyDescriptor = BeanUtil.getPropertyDescriptor(propertyType, propertyName);
		if (propertyDescriptor == null) {
			return null;
		}
		return ReflectUtil.getProperty(propertyType, propertyDescriptor, propertyName);
	}

	/**
	 * 获取 bean 的属性信息
	 * @param propertyType 类型
	 * @param propertyDescriptor PropertyDescriptor
	 * @param propertyName 属性名
	 * @return {Property}
	 */
	public static Property getProperty(Class<?> propertyType, PropertyDescriptor propertyDescriptor, String propertyName) {
		Method readMethod = propertyDescriptor.getReadMethod();
		Method writeMethod = propertyDescriptor.getWriteMethod();
		return new Property(propertyType, readMethod, writeMethod, propertyName);
	}

	/**
	 * 获取 bean 的属性信息
	 * @param propertyType 类型
	 * @param propertyName 属性名
	 * @return {Property}
	 */
	@Nullable
	public static TypeDescriptor getTypeDescriptor(Class<?> propertyType, String propertyName) {
		Property property = ReflectUtil.getProperty(propertyType, propertyName);
		if (property == null) {
			return null;
		}
		return new TypeDescriptor(property);
	}

	/**
	 * 获取 类属性信息
	 * @param propertyType 类型
	 * @param propertyDescriptor PropertyDescriptor
	 * @param propertyName 属性名
	 * @return {Property}
	 */
	public static TypeDescriptor getTypeDescriptor(Class<?> propertyType, PropertyDescriptor propertyDescriptor, String propertyName) {
		Method readMethod = propertyDescriptor.getReadMethod();
		Method writeMethod = propertyDescriptor.getWriteMethod();
		Property property = new Property(propertyType, readMethod, writeMethod, propertyName);
		return new TypeDescriptor(property);
	}

	/**
	 * 获取 类属性
	 * @param clazz 类信息
	 * @param fieldName 属性名
	 * @return Field
	 */
	@Nullable
	public static Field getField(Class<?> clazz, String fieldName) {
		while (clazz != Object.class) {
			try {
				return clazz.getDeclaredField(fieldName);
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			}
		}
		return null;
	}

	/**
	 * 获取 所有 field 属性上的注解
	 * @param clazz 类
	 * @param fieldName 属性名
	 * @param annotationClass 注解
	 * @param <T> 注解泛型
	 * @return 注解
	 */
	@Nullable
	public static <T extends Annotation> T getAnnotation(Class<?> clazz, String fieldName, Class<T> annotationClass) {
		Field field = ReflectUtil.getField(clazz, fieldName);
		if (field == null) {
			return null;
		}
		return field.getAnnotation(annotationClass);
	}
	
	/**
	 * 类中是否含有这个属性
	 * @author wangqq
	 * @date 2019年3月3日 下午12:24:23
	 */
	public static boolean isHasField(Class<?> clazz, String name) {
		Field[] fields = clazz.getDeclaredFields();
		boolean flag = false;
		for (int i = 0; i < fields.length; i++) {
			if (fields[i].getName().equals(name)) {
				flag = true;
				break;
			}
		}
		if (flag) {
			return true;
		}
		return false;
	}
	

	/**
	 * 新建实例
	 * 
	 * @param className
	 *            类名
	 * @param constructorArgs
	 *            构造函数的参数
	 * @return 新建的实例
	 * @throws Exception
	 */
	public static Object newInstance(String className, Object... constructorArgs) {
		try {
			Class<?> newoneClass = Class.forName(className);
			if (constructorArgs == null) {
				Constructor<?> cons = newoneClass.getConstructor();
				return cons.newInstance();
			}

			Class<?>[] argsClass = new Class[constructorArgs.length];
			for (int i = 0, j = constructorArgs.length; i < j; i++) {
				if (constructorArgs[i] != null) {
					argsClass[i] = constructorArgs[i].getClass();
				} else {
					argsClass[i] = null;
				}
			}
			Constructor<?> cons = newoneClass.getConstructor(argsClass);
			return cons.newInstance(constructorArgs);
		} catch (SecurityException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return null;
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 反射执行get方法
	 * 
	 * @author hjin
	 * @cratedate 2013-9-2 下午4:03:46
	 * @param target
	 *            对象
	 * @param propertyName
	 *            属性名
	 * @return
	 * 
	 */
	public static Object invokeGetMethod(Object target, String propertyName) {
		if (target != null) {
			// 找到对应的get方法
			Method method = ReflectionUtils.findMethod(target.getClass(),
					getGetMethodName(propertyName));

			return invokeMethod(target, method);
		} else {
			return null;
		}

	}

	/**
	 * 反射执行set方法
	 * 
	 * @author hjin
	 * @cratedate 2013-9-2 下午4:03:06
	 * @param target
	 *            对象
	 * @param propertyName
	 *            属性名
	 * @param value
	 *            赋值
	 * 
	 */
	public static void invokeSetMethod(Object target, String propertyName,
			Object value) {
		if (target != null) {
			// set方法名
			String methodName = getSetMethodName(propertyName);
			// 找到property
			Field field = ReflectionUtils.findField(target.getClass(),
					propertyName);
			if (field == null) {
				return;
			}
			// 找到对应的set方法
			Method method = ReflectionUtils.findMethod(target.getClass(),
					methodName, field.getType());

			invokeMethod(target, method, value);
		}
	}

	/**
	 * Attempt to find a Method on the supplied target's class with the supplied
	 * name and no parameters. Searches all superclasses up to Object. And
	 * invoke this method.
	 * 
	 * @author hjin
	 * @cratedate 2013-9-4 下午4:02:22
	 * @param target
	 *            目标
	 * @param methodName
	 *            方法名
	 * @param objects
	 *            参数
	 * @return
	 * 
	 */
	public static Object invokeMethod(Object target, String methodName,
			Object... objects) {
		Method method = null;
		if (objects != null && objects.length > 0) {
			Class<?>[] parameterTypes = new Class[objects.length];
			for (int i = 0; i < objects.length; i++) {
				parameterTypes[i] = objects[i].getClass();
			}
			method = ReflectionUtils.findMethod(target.getClass(), methodName,
					parameterTypes);
		} else {
			method = ReflectionUtils.findMethod(target.getClass(), methodName);
		}
		return invokeMethod(target, method, objects);
	}

	/**
	 * Attempt to find a Method on the supplied class with the supplied name and
	 * parameter types. Searches all superclasses up to Object. And invoke this
	 * method.
	 * 
	 * @author hjin
	 * @cratedate 2013-9-4 下午4:07:23
	 * @param target
	 *            目标
	 * @param methodName
	 *            方法名
	 * @param parameterTypes
	 *            参数类型
	 * @param parameters
	 *            参数
	 * @return
	 * 
	 */
	public static Object invokeMethod(Object target, String methodName,
			Class<?>[] parameterTypes, Object[] parameters) {
		Method method = ReflectionUtils.findMethod(target.getClass(),
				methodName, parameterTypes);
		return invokeMethod(target, method, parameters);
	}

	/**
	 * 反射执行方法
	 * 
	 * @author hjin
	 * @cratedate 2013-9-2 下午4:02:55
	 * @param target
	 *            对象
	 * @param method
	 *            方法
	 * @param parameters
	 *            参数
	 * 
	 */
	public static Object invokeMethod(Object target, Method method,
			Object... parameters) {
		if (target == null) {
			return null;
		}
		if (method == null) {
			return null;
		}
		if (parameters == null) {
			return null;
		}
		return ReflectionUtils.invokeMethod(method, target, parameters);
	}

	/**
	 * 根据名称找方法
	 * 
	 * @author hjin
	 * @cratedate 2013-9-9 下午2:00:38
	 * @param clazz
	 * @param name
	 *            方法名
	 * @return
	 * 
	 */
	public static Method findMethodByName(Class<?> clazz, String name) {
		Method result = null;
		Method[] methods = ReflectionUtils.getAllDeclaredMethods(clazz);
		for (Method method : methods) {
			String mname = method.getName();
			if (mname.equals(name)) {
				result = method;
				break;
			}
		}
		return result;
	}

	/**
	 * 反射匹配方法
	 * 
	 * @author hjin
	 * @cratedate 2013-9-6 上午8:53:49
	 * @param clazz
	 * @param name
	 * @param paramTypes
	 * @return
	 * 
	 */
	public static Method findMethodByParamType(Class<?> clazz, String name,
			Class<?>... paramTypes) {
		if (paramTypes == null) {
			return ReflectionUtils.findMethod(clazz, name);
		} else {
			return ReflectionUtils.findMethod(clazz, name, paramTypes);
		}
	}

	/**
	 * 获得get/set方法
	 * 
	 * @author hjin
	 * @cratedate 2013-9-6 上午9:09:40
	 * @param clazz
	 * @param property
	 * @return [0]get,[1]set
	 * 
	 */
	public static Method[] findGetAndSetMethod(Class<?> clazz, String property) {
		Method[] result = new Method[2];
		// get方法
		String getmethodName = ReflectUtil.getGetMethodName(property);
		Method getMethod = ReflectUtil.findMethodByParamType(clazz,
				getmethodName);
		if (getMethod == null) {
			result[0] = null;
			result[1] = null;
			return result;
		} else {
			// get方法返回类型
			Class<?> cls = getMethod.getReturnType();

			// set方法
			String setmethod = ReflectUtil.getSetMethodName(property);
			Method setMethod = ReflectUtil.findMethodByParamType(clazz,
					setmethod, cls);

			result[0] = getMethod;
			result[1] = setMethod;
			return result;
		}
	}

	/**
	 * 属性对应的set方法名字
	 * 
	 * @author hjin
	 * @cratedate 2013-9-2 下午4:01:55
	 * @param propertyName
	 *            属性名
	 * @return
	 * 
	 */
	public static String getGetMethodName(String propertyName) {
		return "get" + StringUtil.capitalize(propertyName);
	}

	/**
	 * 属性对应的set方法名字
	 * 
	 * @author hjin
	 * @cratedate 2013-9-2 下午4:01:55
	 * @param propertyName
	 *            属性名
	 * @return
	 * 
	 */
	public static String getSetMethodName(String propertyName) {
		return "set" + StringUtil.capitalize(propertyName);
	}

	/**
	 * c是否是szInterface的接口实现类
	 * 
	 * @param c
	 * @param szInterface
	 * @return
	 */
	public static boolean isInterface(Class<?> c, String szInterface) {
		Class<?>[] face = c.getInterfaces();
		for (int i = 0, j = face.length; i < j; i++) {
			if (face[i].getName().equals(szInterface)) {
				return true;
			} else {
				Class<?>[] face1 = face[i].getInterfaces();
				for (int x = 0; x < face1.length; x++) {
					if (face1[x].getName().equals(szInterface)) {
						return true;
					} else if (isInterface(face1[x], szInterface)) {
						return true;
					}
				}
			}
		}
		if (null != c.getSuperclass()) {
			return isInterface(c.getSuperclass(), szInterface);
		}
		return false;
	}
	
	/**
	 * 利用反射实现map转对象bean
	 * 
	 * @param map
	 * @param obj——bean对象
	 * @return
	 */
	public static Object transMapBean(Map<String, Object> map, Object obj) {
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo
					.getPropertyDescriptors();

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
			System.out.println("transMapBean Error " + e);
		}
		return obj;
	}
}
