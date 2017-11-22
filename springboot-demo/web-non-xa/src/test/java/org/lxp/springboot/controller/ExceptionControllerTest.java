package org.lxp.springboot.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ExceptionControllerTest extends BaseControllerTest {
    @Resource
    private ExceptionController exceptionController;

    @Before
    public void setUp() {
        super.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testHandleError() throws Exception {
        /**
         * It seems ErrorController doesn't work when unit test
         */
        super.mvc.perform(post("/error")).andExpect(status().isOk());
        super.mvc.perform(get("/error")).andExpect(status().isOk());

        super.mvc.perform(post("/WERUIOPIUYGRFDGFHGKJLKJHGTYI")).andExpect(status().isNotFound());
        super.mvc.perform(get("/WERUIOPIUYGRFDGFHGKJLKJHGTYI")).andExpect(status().isNotFound());
    }
}
