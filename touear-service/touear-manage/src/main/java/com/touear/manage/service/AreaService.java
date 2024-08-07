package com.touear.manage.service;
import com.touear.manage.entity.BaseAreaEntity;
import java.util.List;

/**
 * 地区信息
 */
public interface AreaService {

    /**
     * 根据地区等级获取地区信息
     * @param grade 地区等级
     * @return 地区信息
     */
    List<BaseAreaEntity> getAreaListByGrade(int grade);

    /**
     * 根据父类code获取地区信息
     * @param parentCode 父类编码
     * @return 地区信息
     */
    List<BaseAreaEntity> getAreaListByParentCode(int parentCode);
}
