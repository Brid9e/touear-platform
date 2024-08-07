package com.touear.i2f.i2msk.aspect;

import com.touear.core.tool.utils.ClassUtil;
import com.touear.core.tool.utils.Func;
import com.touear.i2f.i2msk.annotation.Password;
import com.touear.i2f.i2msk.feign.IBaseEquipmentAuthorityCilent;
import com.touear.i2f.i2msk.feign.IKeyboardFeign;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 密码
 *
 * @author Chen
 */
@Aspect
@Component
@Slf4j
public class PasswordAspect implements ApplicationContextAware {
	
	@Autowired
	private IKeyboardFeign keyboardFeign;
	@Autowired
	private IBaseEquipmentAuthorityCilent baseEquipmentAuthorityCilent;
	
	@Pointcut("@annotation(com.touear.i2f.i2msk.annotation.Password)")
	public void passwordPointCut() {

	}

	/**
	 * 切 方法 和 类上的 @Password 注解
	 *
	 * @param point
	 *            切点
	 * @return Object
	 * @throws Throwable
	 *             没有权限的异常
	 */
	@SuppressWarnings("unchecked")
	@Around("passwordPointCut()")
	public Object password(ProceedingJoinPoint point) throws Throwable {
		MethodSignature ms = (MethodSignature) point.getSignature();
		Method method = ms.getMethod();
		Password password = ClassUtil.getAnnotation(method, Password.class);
		// 判断表达式
		List<String> condition = Arrays.asList(password.value());
		boolean remove = password.remove();
		Object[] args = point.getArgs();
		try {
			int i = 0;
			for (Object object : args) {
				log.info("aop:" + object.getClass());
				Map<String, Object> map = new HashMap<>();
				if (Func.toStr(object.getClass()).indexOf("Map") != -1) {
					map = (Map<String, Object>) object;
				}else if(Func.toStr(object.getClass()).indexOf("String") != -1){
					String param = String.valueOf(object);
					if(param.split("\\$1\\$").length > 1){
						map.put("password",param);
					}
				} else {
					map = Func.toMap(object);
				}
				for(String key : map.keySet()) {
					if(condition.contains(key)) {
						passwordMap(map,key,remove);
						if (Func.toStr(object.getClass()).indexOf("Map") != -1) {
							args[i] = map;
						}else if(Func.toStr(object.getClass()).indexOf("String") != -1){
							args[i] = map.get("password");
						}else {
							args[i] = Func.mapToBean(map, object.getClass());
						}
					}
				}
				i++;
			}
		}catch (Exception e){
			args = point.getArgs();
			e.printStackTrace();
		}
		return point.proceed(args);
	}

	public void passwordMap(Map<String, Object> map, String parameterName,boolean remove) {
		String passwordStr = Func.toStr(map.get(parameterName));
		log.info("passwordStr:"+passwordStr);
		String[] passwordArr = passwordStr.split("\\$1\\$");
		if (passwordArr.length > 1) {
			String type = passwordArr[0];
			log.info("type:"+type);
			if ("0".equals(type)) {
				map.put(parameterName, baseEquipmentAuthorityCilent.checkAuthority(Func.urlEncode(passwordArr[1])).getData());
			} else {
				String password = null;
				if(passwordArr.length == 2){
					// !#!
					password = passwordArr[0] + "!#!" + passwordArr[1];
				}else {
					password = passwordArr[1] + "!#!" + passwordArr[2];
				}
				String decryptPassword = keyboardFeign.checkPassword(Func.urlEncode(password),remove);
				map.put(parameterName, decryptPassword.contains("\"code\":400")?passwordStr:decryptPassword);
			}
			log.info("parameterName--->:"+map.get(parameterName));
		}
	}


	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

}
