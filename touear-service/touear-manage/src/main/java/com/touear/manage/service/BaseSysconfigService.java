package com.touear.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.touear.manage.entity.BaseSysconfig;


import java.io.IOException;
import java.util.List;

/**
 * @Title: BaseSysconfigService.java
 * @Description: BaseSysconfigService
 * @author chenl
 * @date 2020-08-05 10:01:50
 * @version 1.0
 */
public interface BaseSysconfigService extends IService<BaseSysconfig> {


	/**
	 * 系统参数
	 * 
	 * @return
	 */
	List<BaseSysconfig> selectList(BaseSysconfig systemConfig);
	
	/**
	 * 系统参数
	 *
	 * @param code
	 * @return
	 */
	BaseSysconfig selectSysconfig(String code);

}
