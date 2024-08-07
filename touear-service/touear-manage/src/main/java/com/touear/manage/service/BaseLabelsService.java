package com.touear.manage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.touear.manage.entity.BaseLabelsEntity;
import com.touear.manage.vo.BaseLabelsVo;

public interface BaseLabelsService extends IService<BaseLabelsEntity> {

    /**
     * 查找标签（分页）
     * @param page 分页信息
     * @param labels 卡样收费信息
     * @return IPage<BaseLabelsVo>
     */
    IPage<BaseLabelsVo> getPage(IPage<BaseLabelsEntity> page, BaseLabelsEntity labels);
}
