package com.touear.manage.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.touear.manage.entity.BaseAreaEntity;
import com.touear.manage.mappers.BaseAreaMapper;
import com.touear.manage.service.AreaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 地区信息服务
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AreaServiceImpl implements AreaService {

    private final BaseAreaMapper areaInfoMapper;

    /**
     * 根据地区等级获取地区信息
     *
     * @param grade 地区等级
     * @return 地区信息
     */
    @Override
    public List<BaseAreaEntity> getAreaListByGrade(int grade) {
        QueryWrapper<BaseAreaEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BaseAreaEntity::getGrade,grade);
        return areaInfoMapper.selectList(queryWrapper);
    }

    /**
     * 根据父类code获取地区信息
     *
     * @param parentCode 父类编码
     * @return 地区信息
     */
    @Override
    public List<BaseAreaEntity> getAreaListByParentCode(int parentCode) {
        Integer[]  specialAreaInfos = {110000, 120000, 310000, 500000, 810000, 820000};
        boolean specialMatch =  Arrays.stream(specialAreaInfos).anyMatch(info -> info == parentCode);
        if (specialMatch) {
            List<BaseAreaEntity> areaInfoList = new ArrayList<>();
//            QueryWrapper<BaseAreaEntity> queryWrapper = new QueryWrapper<>();
//            queryWrapper.lambda().eq(BaseAreaEntity::getCode,parentCode);
//            BaseAreaEntity specialAreaInfo = areaInfoMapper.selectOne(queryWrapper);
//            areaInfoList.add(specialAreaInfo);
            return new ArrayList<>();
        }
        QueryWrapper<BaseAreaEntity> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().eq(BaseAreaEntity::getParentCode,parentCode);
        return areaInfoMapper.selectList(queryWrapper1);
    }
}
