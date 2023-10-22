package com.lxp.activemq.test.base;

import javax.inject.Inject;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.context.WebApplicationContext;

/**
 * @Description: 单元测试基类
 * @author Super.Li
 * @date May 12, 2017
 */
@ContextConfiguration(locations = { "classpath*:spring/*.xml" })
public abstract class BaseTest {
    @Inject
    protected WebApplicationContext context;
}
