package org.lxp.rw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lxp.rw.service.UserService;
import org.lxp.rw.test.base.MemoryDBTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * remove @Transactional to test master/slave replication
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
public class UserControllerWithMemoryDBTest extends MemoryDBTest {
    private static final String TEST_QUERY_NAME = "testQuery";
    private static final String TEST_UPDATE_NAME = "testUpdate";
    @Resource
    private UserService userService;
    protected MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testAdd() throws Exception {
        final String name = "testAdd";
        ResultActions action = mockMvc.perform(post("/user/add.json").param("name", name)).andExpect(status().isOk())
                .andDo(print());
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$.name").exists());
        action.andExpect(jsonPath("$.name").value(is("testAdd")));
    }

    @Test
    public void testDelete() throws Exception {
        /**
         * mock user to delete
         */
        int deleteUserId = userService.add("testDelete").getId();

        ResultActions action = mockMvc.perform(delete("/user/" + deleteUserId + ".json")).andExpect(status().isOk())
                .andDo(print());
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$").value(is(Boolean.TRUE)));
    }

    @Test
    public void testQuery() throws Exception {
        /**
         * mock user to query
         */
        int getUserId = userService.add(TEST_QUERY_NAME).getId();

        ResultActions action = mockMvc.perform(get("/user/" + getUserId + ".json")).andExpect(status().isOk())
                .andDo(print());
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$.name").exists());
        action.andExpect(jsonPath("$.name").value(is(TEST_QUERY_NAME)));
    }

    @Test
    public void testUpdate() throws Exception {
        /**
         * mock user to update
         */
        int updateUserId = userService.add(TEST_UPDATE_NAME).getId();

        String newName = TEST_UPDATE_NAME + "_1";
        ResultActions action = mockMvc.perform(put("/user/" + updateUserId + ".json").param("name", newName))
                .andExpect(status().isOk()).andDo(print());
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$.name").exists());
        action.andExpect(jsonPath("$.name").value(is(newName)));
    }
}

