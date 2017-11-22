package org.lxp.springboot.controller;

import javax.annotation.Resource;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

public class BaseControllerTest {
    protected MockMvc mvc;
    @Resource
    protected WebApplicationContext context;
}
