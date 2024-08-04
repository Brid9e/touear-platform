package com.touear.manage.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.touear.common.utils.R;
import com.touear.manage.entity.BaseLabelsEntity;
import com.touear.manage.service.BaseLabelsService;
import com.touear.manage.vo.BaseLabelsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import java.util.List;
import java.util.concurrent.locks.Condition;

@RestController
@RequestMapping("/lables")
public class BaseLabelsController {

    @Autowired
    private BaseLabelsService labelsService;

    @GetMapping("/list")
    public R<IPage<BaseLabelsVo>> getPage(Query query,BaseLabelsEntity labels) {
        return R.data(null);
    }

    @PostMapping("/add")
    public R<Boolean> addUser(@RequestBody BaseLabelsEntity lablesEntity) {
        boolean result = labelsService.save(lablesEntity);
        if(result){
            return R.data(true);
        }else {
            return  R.fail("添加失败");
        }
    }
}
