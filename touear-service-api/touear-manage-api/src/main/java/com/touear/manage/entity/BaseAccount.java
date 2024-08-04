package com.touear.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@TableName("base_account")
@ApiModel(description = "用户实体类")
public class BaseAccount  {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String account;
    private String name;
    private String password;
}
