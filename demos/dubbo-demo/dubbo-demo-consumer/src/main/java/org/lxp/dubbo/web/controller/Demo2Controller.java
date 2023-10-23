package org.lxp.dubbo.web.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.annotation.Resource;

import org.lxp.dubbo.service.DemoService;
import org.lxp.dubbo.vo.DemoVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.annotations.ApiOperation;

@Controller
public class Demo2Controller {
    private static final Logger LOG = LoggerFactory.getLogger(Demo2Controller.class);
    @Resource
    private DemoService demoService2;

    @ResponseBody
    @RequestMapping(value = "/getDemoVo", method = POST)
    @ApiOperation(value = "调用getDemoVo方法")
    public List<DemoVo> test1(@RequestBody List<String> names) {
        LOG.debug("names={}", names);
        return demoService2.getDemoVo(names);
    }

}