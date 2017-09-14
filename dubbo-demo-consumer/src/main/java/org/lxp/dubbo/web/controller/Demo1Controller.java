package org.lxp.dubbo.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import javax.annotation.Resource;

import org.lxp.dubbo.service.DemoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
public class Demo1Controller {
    @Resource
    private DemoService demoService1;

    @ResponseBody
    @RequestMapping(value = "/sayHello", method = GET)
    @ApiOperation(value = "调用sayHello方法")
    public String test1(@ApiParam(required = true, value = "输入name") @RequestParam String name) {
        return demoService1.sayHello(name);
    }

}