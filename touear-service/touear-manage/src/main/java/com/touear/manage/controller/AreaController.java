package com.touear.manage.controller;



import com.touear.manage.entity.BaseAreaEntity;
import com.touear.manage.service.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 地区信息服务
 */
@Api(tags="地区信息列表")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("area")
public class AreaController {

    private final AreaService areaService;

    /**
     * 获取第二级地区信息列表
     * @return 地区信息列表
     */
    @GetMapping("parent")
    @ApiOperation(value = "获取第二级地区信息列表", notes = "")
    public List<BaseAreaEntity> getParentAreaInfoList() {
        return areaService.getAreaListByGrade(2);
    }

    /**
     * 获取子类地区信息列表
     * @param parentCode 父类编码
     * @return 地区信息列表
     */
    @GetMapping("child")
    @ApiOperation(value = "获取子类地区信息列表", notes = "")
    public List<BaseAreaEntity> getChildAreaInfoList(@RequestParam int parentCode) {
        return areaService.getAreaListByParentCode(parentCode);
    }

}
