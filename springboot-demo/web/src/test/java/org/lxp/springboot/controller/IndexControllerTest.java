package org.lxp.springboot.controller;

import static org.junit.Assert.assertEquals;
import static org.lxp.springboot.controller.IndexController.INDEX_PATH;
import static org.lxp.springboot.controller.IndexController.INDEX_RESPONSE_BODY;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lxp.springboot.vo.BaseVO;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IndexControllerTest extends BaseControllerTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        super.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testIndex() throws Exception {
        String rtn = null;
        BaseVO baseVO = null;

        rtn = super.mvc.perform(post(INDEX_PATH)).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        baseVO = objectMapper.readValue(rtn, BaseVO.class);
        assertEquals(405, baseVO.getResCode());

        rtn = super.mvc.perform(get(INDEX_PATH)).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        baseVO = objectMapper.readValue(rtn, BaseVO.class);
        assertEquals(400, baseVO.getResCode());

        rtn = super.mvc.perform(get(INDEX_PATH).param("sessionId", "aaa")).andExpect(status().isOk()).andReturn()
                .getResponse().getContentAsString();
        baseVO = objectMapper.readValue(rtn, BaseVO.class);
        assertEquals(400, baseVO.getResCode());

        rtn = super.mvc.perform(get(INDEX_PATH).param("sessionId", "111")).andExpect(status().isOk()).andReturn()
                .getResponse().getContentAsString();
        assertEquals(INDEX_RESPONSE_BODY, rtn);
    }

}
