package org.lxp.springboot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.inject.Inject;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class VersionControllerTest {
    @Inject
    private MockMvc mockMvc;

    @Test
    public void version() throws Exception {
        ResultActions action = this.mockMvc.perform(get("/version"));
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$.abbrev").value(is("76ed7e7")));
        action.andExpect(jsonPath("$.full").value(is("76ed7e788e2e02c839424ad1a59eb84bff598bc3")));
        action.andExpect(jsonPath("$.messageShort").value(is("update README.md")));
        action.andExpect(jsonPath("$.time").value(is("2024-06-09T23:21:32+0800")));
    }
}