package com.touear.manage.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户信息表
 * 
 * @author sjw
 * @email ${email}
 * @date 2024-08-05 23:15:20
 */
@Data
@TableName("base_account")
@ApiModel(description = "用户实体类")
public class BaseAccountEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id;
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 密码
	 */
	private String password;
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty(value = "创建时间")
	private Date creatTime;
	@TableField(value = "update_time", fill = FieldFill.UPDATE)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "GMT+8")
	@ApiModelProperty(value = "更新时间")
	private Date updateTime;
	/**
	 * 用户类型0：管理员 1：用户
	 */
	private String userType;

}
