package org.lxp.rw.test.base;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

@ContextConfiguration(locations = { "classpath*:spring/test-*.xml" })
public abstract class MemoryDBTest {
    @Resource
    protected WebApplicationContext context;
}
