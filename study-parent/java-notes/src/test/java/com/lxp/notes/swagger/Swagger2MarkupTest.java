package com.lxp.notes.swagger;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.lxp.notes.test.base.BaseTest;

import springfox.documentation.staticdocs.Swagger2MarkupResultHandler;

/**
 * mvn clean test
 * 
 * @Title: Swagger2MarkupTest.java
 * @Package com.lxp.notes.swagger
 * @Description: 生成API静态说明书
 * @author Super.Li
 * @date Mar 15, 2017
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class Swagger2MarkupTest extends BaseTest {
    @Inject
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws IOException {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(super.restDocumentation)).build();
        /**
         * 把汇总配置复制到生成文件夹，为文档生成做准备
         */
        FileUtils.copyDirectory(new File(super.SRC_ASCIIDOC), new File(super.TARGET_ASCIIDOC));
    }

    @Test
    public void convertSwaggerToAsciiDoc() throws Exception {
        this.mockMvc.perform(get("/v2/api-docs").accept(MediaType.APPLICATION_JSON)).andDo(Swagger2MarkupResultHandler
                .outputDirectory(super.TARGET_ASCIIDOC_GENERATED).withExamples(super.TARGET_SNIPPETS_GENERATED).build())
                .andExpect(status().isOk());
    }
}
