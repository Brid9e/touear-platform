package com.touear.core.log.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * LogUsual视图实体类
 *
 * @author Chen
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LogUsualVo extends LogUsual {
	private static final long serialVersionUID = 1L;

	private String strId;

}
