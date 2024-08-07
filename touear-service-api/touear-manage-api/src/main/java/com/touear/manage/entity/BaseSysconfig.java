package com.touear.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Title: BaseSysconfig.java
 * @Description: 系统参数
 * @version 1.0
 */
@Data
@NoArgsConstructor
@TableName("base_systemconfig")
public class BaseSysconfig {

	/** id */
	@TableId(type= IdType.ID_WORKER)

	private Long id;
	/** 代码 */

	private String code;
	/** 名称 */

	private String name;
	/** 类型【0：string；1：list；2：map】 */

	private String type;
	/** 参数值 */

	private String value;
	/** 描述 */
	private String description;
	
}
