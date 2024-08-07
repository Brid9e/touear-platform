package com.touear.manage.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginUser {
    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "用户姓名")
    private String account;
}