package com.touear.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;

public interface MyBaseMapper<T> extends BaseMapper<T> {

    /**
     * 物理删除
     *
     * @param id
     * @return
     */
    int deleteAbsoluteById(Serializable id);

    /**
     * 批量物理删除
     *
     * @param idList
     * @return
     */
    int deleteAbsoluteByIds(@Param(Constants.COLLECTION) Collection<? extends Serializable> idList);


}