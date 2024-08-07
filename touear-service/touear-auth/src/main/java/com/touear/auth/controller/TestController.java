package com.touear.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("auto/test")
public class TestController {
    @RequestMapping(value = "/index")
    public String test() {
        return "测试";
    }
}
