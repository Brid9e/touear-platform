package com.touear.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 
 * @Title: BaseAccount.java
 * @Description: 系统用户-登录账号
 * @date 2020-06-09 14:44:30
 * @version 1.0
 */
@Data
@TableName("base_account")
public class BaseAccount implements Serializable {

    private static final long serialVersionUID = 1L;
	/** 系统用户Id */
	@ApiModelProperty("id")
	@TableId(type = IdType.AUTO)
    private Long id; 
	/** 登录账号 */
	@ApiModelProperty("登录账号")
    private String account; 
    /** 密码凭证：站内的保存密码、站外的不保存或保存token） */
    @ApiModelProperty("密码凭证")
    private String password; 
    /** 注册IP */
    @ApiModelProperty("注册IP")
    private String registerIp;
    /** 状态 */
    @ApiModelProperty("状态")
    private Integer status; 
    /** 姓名 */
    @ApiModelProperty("姓名")
    private String name; 
    /** 头像 */

    /** 用户类型 */
    @ApiModelProperty("用户类型")
    private String userType; 


    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    public Date createTime;
    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    public Date updateTime;
}
