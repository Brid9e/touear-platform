package com.touear.manage.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 地区编码信息
 */
@Data
@TableName("base_area")
@ApiModel(value = "area对象", description = "area对象")
public class BaseAreaEntity {

    /**
     * 地区编码
     */
   // @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "主键id")
    @TableId(type = IdType.AUTO)
    private Integer code;

    private String name;

    private int parentCode;

    /**
     * 等级分为四级 1级对应国家 2级省市直辖市级 3级次级地区 选取三级使用
     */
    private Integer grade;

    private String referred;

    /**
     * 拼音
     */
    private String pinyin;

    /**
     * 拼音简称
     */
    private String py;
}
