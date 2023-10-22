package com.lxp.notes.test.base;

import javax.inject.Inject;

import org.junit.Rule;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.context.WebApplicationContext;

/**
 * @Description: 单元测试基类
 * @author Super.Li
 * @date Apr 27, 2017
 */
@ContextConfiguration(locations = { "classpath*:spring/*.xml" })
public abstract class BaseTest {
    protected final String TARGET_SNIPPETS_GENERATED = "target/snippets/generated";
    protected final String SRC_ASCIIDOC = "src/docs/asciidoc";
    protected final String TARGET_ASCIIDOC = "target/asciidoc";
    protected final String TARGET_ASCIIDOC_GENERATED = TARGET_ASCIIDOC + "/generated";
    @Inject
    protected WebApplicationContext context;
    /**
     * JUnitRestDocumentation设置生成的snippets路径
     */
    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation(TARGET_SNIPPETS_GENERATED);
}
