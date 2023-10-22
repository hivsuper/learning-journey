package org.lxp.rw.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lxp.rw.model.User;
import org.lxp.rw.service.UserService;
import org.lxp.rw.test.base.BaseTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * remove @Transactional to test master/slave replication
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
public class UserControllerTest extends BaseTest {
    private static final String TEST_QUERY_NAME = "测试查询";
    private static final String TEST_UPDATE_NAME = "测试更新";
    @Inject
    private UserService userService;
    protected MockMvc mockMvc;
    private int deleteUserId;
    private int getUserId;
    private int udpateUserId;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testAdd() throws Exception {
        final String name = "测试添加";
        String rtn = mockMvc.perform(post("/user/add.json").param("name", name)).andExpect(status().isOk())
                .andDo(print()).andReturn().getResponse().getContentAsString();
        User tmp = objectMapper.readValue(rtn, User.class);
        Assert.assertEquals(name, tmp.getName());
    }

    @Test
    public void testDelete() throws Exception {
        /**
         * mock user to delete
         */
        deleteUserId = userService.add("测试删除").getId();

        String rtn = mockMvc.perform(delete("/user/" + deleteUserId + ".json")).andExpect(status().isOk())
                .andDo(print()).andReturn().getResponse().getContentAsString();
        Assert.assertTrue(Boolean.valueOf(rtn));
    }

    @Test
    public void testQuery() throws Exception {
        /**
         * mock user to query
         */
        getUserId = userService.add(TEST_QUERY_NAME).getId();

        String rtn = mockMvc.perform(get("/user/" + getUserId + ".json")).andExpect(status().isOk()).andDo(print())
                .andReturn().getResponse().getContentAsString();
        User tmp = objectMapper.readValue(rtn, User.class);
        Assert.assertEquals(TEST_QUERY_NAME, tmp.getName());
    }

    @Test
    public void testUpdate() throws Exception {
        /**
         * mock user to update
         */
        udpateUserId = userService.add(TEST_UPDATE_NAME).getId();

        String newName = TEST_UPDATE_NAME + "_1";
        String rtn = mockMvc.perform(put("/user/" + udpateUserId + ".json").param("name", newName))
                .andExpect(status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
        User tmp = objectMapper.readValue(rtn, User.class);
        Assert.assertEquals(newName, tmp.getName());
    }
}
