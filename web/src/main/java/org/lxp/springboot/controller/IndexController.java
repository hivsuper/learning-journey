package org.lxp.springboot.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
public class IndexController {
    static final String INDEX_PATH = "/";
    static final String INDEX_RESPONSE_BODY = "This is index page";

    @RequestMapping(value = INDEX_PATH, method = GET)
    @ApiOperation(value = "Index Page")
    public String index(@RequestParam int sessionId) {
        return INDEX_RESPONSE_BODY;
    }
}
