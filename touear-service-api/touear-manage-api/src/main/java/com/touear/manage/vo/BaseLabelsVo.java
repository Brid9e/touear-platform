package com.touear.manage.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.touear.manage.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "标签信息VO")
public class BaseLabelsVo extends BaseEntity {

    @ApiModelProperty(value = "标签code")
    private String code;
    @ApiModelProperty(value = "标签名称")
    private String labelName;
    @ApiModelProperty(value = "标签类型（景区标签：0 讲解项目标签：1）")
    private String type;

}
