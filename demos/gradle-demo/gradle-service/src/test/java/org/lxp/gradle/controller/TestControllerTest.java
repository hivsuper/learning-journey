package org.lxp.gradle.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.lxp.gradle.BaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class TestControllerTest extends BaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAdd() throws Exception {
        final var action = this.mockMvc.perform(post("/add").param("name", "555"));
        action.andExpect(status().isOk()).andExpect(content().string(is("1")));
    }

    @DisplayName("Fail to list customers when customerIds is null or empty")
    @ParameterizedTest
    @NullAndEmptySource
    void testListByCustomerIdsWithNullOrEmptyIds(String name) throws Exception {
        final var action = this.mockMvc.perform(post("/add").param("name", name));
        action.andExpect(status().isBadRequest());
    }
}
