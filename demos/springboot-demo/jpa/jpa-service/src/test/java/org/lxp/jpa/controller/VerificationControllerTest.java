package org.lxp.jpa.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.lxp.jpa.BaseTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VerificationControllerTest extends BaseTest {
    @Inject
    private MockMvc mockMvc;
    private static String key;

    @DisplayName("Generate verification code")
    @Test
    @Order(1)
    void generate() throws Exception {
        final var action = this.mockMvc.perform(post("/verification/generate")
                .param("key", "aaaa")
                .param("value", "1111"));
        action.andExpect(status().isOk());
        key = JsonPath.parse(action.andReturn().getResponse().getContentAsString()).read("key").toString();
    }

    @DisplayName("Verify the input verification code but unmatched")
    @Test
    @Order(2)
    void verify() throws Exception {
        final var action = this.mockMvc.perform(post("/verification/verify")
                .param("verifyCodeKey", key)
                .param("verifyCode", "bbbb"));
        action.andExpect(status().is5xxServerError())
                .andExpect(jsonPath("$.code").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                .andExpect(jsonPath("$.data").value("Verification code unmatched"));
    }
}