package org.lxp.springboot.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
public class IndexController {

    @ResponseBody
    @RequestMapping(value = "/", method = GET)
    @ApiOperation(value = "Index Page")
    public String index() {
        return "this is index page";
    }
}
