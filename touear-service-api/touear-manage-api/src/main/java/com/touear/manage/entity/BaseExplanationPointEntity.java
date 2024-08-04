package com.touear.manage.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("base_explanation_point")
@ApiModel(description = "讲解点实体类")
public class BaseExplanationPointEntity extends  BaseEntity{
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "讲解点id")
    private Long id;
    @ApiModelProperty(value = "讲解项目id")
    private Long explanationId;
    @ApiModelProperty(value = "景区id")
    private Long scenicSpotId;
    @ApiModelProperty(value = "讲解点名称")
    private String pointTitle;
    @ApiModelProperty(value = "讲解点图片")
    private String pointPicture;
    @ApiModelProperty(value = "讲解点音频")
    private String pointAudio;
    @ApiModelProperty(value = "价格")
    private BigDecimal price;
    @ApiModelProperty(value = "状态（上架：1 下架 0）")
    private String status;
    @ApiModelProperty(value = "是否删除（未删除：1 已删除 0）")
    private String isDelete;

}
