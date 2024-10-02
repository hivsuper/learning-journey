package org.lxp.gradle.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.lxp.gradle.BaseTest;
import org.lxp.gradle.entity.TestTable;
import org.lxp.gradle.repository.TestTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class TestControllerTest extends BaseTest {
    @Autowired
    private TestTableRepository testTableRepository;
    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    public void tearDown() {
        testTableRepository.deleteAll();
    }

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

    @Test
    void testListByPage() throws Exception {
        // Given
        List.of("111", "222", "333").forEach(name -> {
            testTableRepository.save(TestTable.builder().name(name).build());
        });
        // Execution
        final var action = this.mockMvc.perform(get("/findAll")
                .param("pageNumber", "0")
                .param("pageSize", "2")
        ).andDo(print());
        // Verification
        action.andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(is(3)))
                .andExpect(jsonPath("$.totalPages").value(is(2)))
                .andExpect(jsonPath("$.content.length()").value(is(2)))
                .andExpect(jsonPath("$.content.*.name").value(containsInAnyOrder("111", "222")));
    }
}
