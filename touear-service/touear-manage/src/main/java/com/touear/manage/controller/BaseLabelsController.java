package com.touear.manage.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.touear.manage.entity.BaseLabelsEntity;
import com.touear.manage.service.BaseLabelsService;
import com.touear.manage.vo.BaseLabelsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.touear.support.Condition;
import javax.management.Query;


@Api(tags = "标签")
@RestController
@RequestMapping("/labels")
@AllArgsConstructor
public class BaseLabelsController {

    @Autowired
    private BaseLabelsService labelsService;

    @ApiOperation(value = "分页-标签信息")
    @GetMapping("/page")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页", dataType = "number", required = true),
            @ApiImplicitParam(name = "size", value = "每页的数量", dataType = "number", required = true),
            @ApiImplicitParam(name = "code", value = "标签code",dataType = "string", required = false),
            @ApiImplicitParam(name = "type", value = "景区标签：0 讲解项目标签：1",required = false)
    })
    public R<IPage<BaseLabelsVo>> getPage(Query query,BaseLabelsEntity labels) {
        return R.data(labelsService.getPage(Condition.getPage(query),labels));
    }

    @PostMapping("/add")
    public R<Boolean> addLabels(@RequestBody BaseLabelsEntity lablesEntity) {
        boolean result = labelsService.save(lablesEntity);
        if(result){
            return R.data(true);
        }else {
            return  R.fail("添加失败");
        }
    }

    @PostMapping("/edit")
    public R<Boolean> editLabels(@RequestBody BaseLabelsEntity lablesEntity) {
        boolean result = labelsService.save(lablesEntity);
        if(result){
            return R.data(true);
        }else {
            return  R.fail("添加失败");
        }
    }
}
