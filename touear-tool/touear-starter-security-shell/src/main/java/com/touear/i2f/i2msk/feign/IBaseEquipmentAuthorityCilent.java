package com.touear.i2f.i2msk.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.touear.core.tool.api.R;

/**
 * @Title: IBaseEquipmentAuthorityCilent.java
 * @Description: IBaseEquipmentAuthorityCilent
 * @author chenl
 * @date 2020-08-06 17:51:07
 * @version 1.0
 */

@FeignClient(
		value = "touear-base"/*,
		fallback = IBaseEquipmentAuthorityCilentFallback.class*/
	)
public interface IBaseEquipmentAuthorityCilent {

	String API_PREFIX = "/equipment";
	
	@GetMapping(API_PREFIX+"/check-authority")
	R<String> checkAuthority(@RequestParam(value = "token") String token);
	
}
