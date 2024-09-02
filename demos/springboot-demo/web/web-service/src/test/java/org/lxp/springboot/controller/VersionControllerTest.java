package org.lxp.springboot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.inject.Inject;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:git-test.properties")
class VersionControllerTest {
    @Inject
    private MockMvc mockMvc;

    @Test
    void version() throws Exception {
        ResultActions action = this.mockMvc.perform(get("/version"));
        action.andExpect(status().isOk())
                .andExpect(jsonPath("$.abbrev").value(is("76ed7e7")))
                .andExpect(jsonPath("$.full").value(is("76ed7e788e2e02c839424ad1a59eb84bff598bc3")))
                .andExpect(jsonPath("$.messageShort").value(is("update README.md")))
                .andExpect(jsonPath("$.time").value(is("2024-06-09T23:21:32+0800")));
    }
}