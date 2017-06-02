package org.lxp.springboot.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lxp.springboot.vo.BaseVO;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IndexControllerTest {
    private MockMvc mvc;
    @Resource
    private WebApplicationContext context;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testIndex() throws Exception {
        String rtn = null;
        BaseVO baseVO = null;

        this.mvc.perform(post("/WERUIOPIUYGRFDGFHGKJLKJHGTYI")).andExpect(status().isNotFound());
        this.mvc.perform(get("/WERUIOPIUYGRFDGFHGKJLKJHGTYI")).andExpect(status().isNotFound());

        rtn = this.mvc.perform(post("/")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        baseVO = objectMapper.readValue(rtn, BaseVO.class);
        assertEquals(405, baseVO.getResCode());

        rtn = this.mvc.perform(get("/")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        baseVO = objectMapper.readValue(rtn, BaseVO.class);
        assertEquals(400, baseVO.getResCode());

        rtn = this.mvc.perform(get("/").param("sessionId", "aaa")).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        baseVO = objectMapper.readValue(rtn, BaseVO.class);
        assertEquals(400, baseVO.getResCode());

        rtn = this.mvc.perform(get("/").param("sessionId", "111")).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        assertEquals(IndexController.INDEX, rtn);
    }

}
