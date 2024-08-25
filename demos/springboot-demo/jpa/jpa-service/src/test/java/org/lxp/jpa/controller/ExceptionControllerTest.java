package org.lxp.jpa.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ExceptionControllerTest {
    @Inject
    private MockMvc mockMvc;

    @Test
    void testHandleError() throws Exception {
        this.mockMvc.perform(post("/WERUIOPIUYGRFDGFHGKJLKJHGTYI")).andExpect(status().isNotFound());
        this.mockMvc.perform(get("/WERUIOPIUYGRFDGFHGKJLKJHGTYI")).andExpect(status().isNotFound());
    }
}
