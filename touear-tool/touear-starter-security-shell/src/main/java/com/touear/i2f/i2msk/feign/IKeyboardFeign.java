package com.touear.i2f.i2msk.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Title: IKeyboardFeign.java
 * @Description: IKeyboardFeign
 * @author chenl
 * @date 2020-09-18 10:45:08
 * @version 1.0
 */
@FeignClient(value = "touear-secure"/*, fallback = IKeyboardFeignFallback.class*/)
public interface IKeyboardFeign {
	
	String API_PREFIX = "/keyboard";
	/**
	 * 获取原始密码
	 * 
	 * @param password
	 * @return
	 * @author chenl
	 * @date 2020-09-18 10:51:00
	 */
	@GetMapping(API_PREFIX+"/check")
	String checkPassword(@RequestParam(value = "password") String password,@RequestParam(value = "remove") boolean remove);
}
