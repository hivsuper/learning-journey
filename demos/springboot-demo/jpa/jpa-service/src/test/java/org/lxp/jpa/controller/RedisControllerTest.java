package org.lxp.jpa.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.lxp.jpa.config.RedisContainerInitializer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.inject.Inject;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RedisControllerTest {
    @Inject
    private MockMvc mockMvc;

    @DisplayName("Test adding key/value to redis")
    @Test
    @Order(1)
    void set() throws Exception {
        ResultActions action = this.mockMvc.perform(post("/redis/add")
                .param("key", "aaaa")
                .param("value", "1111"));
        action.andExpect(status().isOk());
    }

    @DisplayName("Test retrieving value with a key from redis")
    @Test
    @Order(2)
    void get() throws Exception {
        ResultActions action = this.mockMvc.perform(MockMvcRequestBuilders.get("/redis/get")
                .param("key", "aaaa"));
        action.andExpect(status().isOk());
        action.andExpect(content().string(is("1111")));
    }

    @DisplayName("Test deleting a key from redis")
    @Test
    @Order(3)
    void delete() throws Exception {
        ResultActions action = this.mockMvc.perform(MockMvcRequestBuilders.delete("/redis/delete")
                .param("key", "aaaa"));
        action.andExpect(status().isOk());
        action.andExpect(content().string(is(Boolean.TRUE.toString())));
    }
}