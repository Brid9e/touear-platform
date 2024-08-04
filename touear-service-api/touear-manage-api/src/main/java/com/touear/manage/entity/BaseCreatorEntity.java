package com.touear.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("base_creator")
@ApiModel(description = "创作者实体类")
public class BaseCreatorEntity extends  BaseEntity{
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "创作者姓名")
    private String name;
    @ApiModelProperty(value = "头像")
    private String avatar;
    @ApiModelProperty(value = "简介")
    private String introduction;
    @ApiModelProperty(value = "是否删除（未删除：1 已删除 0）")
    private String isDelete;

}
