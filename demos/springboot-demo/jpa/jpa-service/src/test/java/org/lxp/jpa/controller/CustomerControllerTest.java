package org.lxp.jpa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.lxp.jpa.BaseTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.Matchers.contains;
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
class CustomerControllerTest extends BaseTest {
    @Inject
    private MockMvc mockMvc;

    @Test
    void testAdd() throws Exception {
        final var action = this.mockMvc.perform(post("/add.json")
                .param("name", "555")
                .param("password", "555P")
                .param("email", "555@555.com"));
        action.andExpect(status().isOk())
                .andExpect(content().string(is("1")));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "null, 555P, 555@555.com, Required parameter 'name' is not present.",
            "'', 555P, 555@555.com, Validation failure",
            "'  ', 555P, 555@555.com, Validation failure",
            "555, null, 555@555.com, Required parameter 'email' is not present.",
            "555, '', 555@555.com, Validation failure",
            "555, '  ', 555@555.com, Validation failure",
            "555, 555P, null, Validation failure"
    }, nullValues = "null")
    void testAddWithNullOrEmptyParameters(String name, String email, String password, String expectedMessage) throws Exception {
        final var action = this.mockMvc.perform(post("/add.json")
                .param("name", name)
                .param("password", password)
                .param("email", email));
        action.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(is(expectedMessage)));
    }

    @Test
    @Sql(statements = """
            INSERT INTO customer(id, name,email,created_date) VALUES(1, '111','111@yahoo.com', '2017-02-11');
            INSERT INTO customer(id, name,email,created_date) VALUES(2, '222','222@yahoo.com', '2017-02-12');
            INSERT INTO customer(id, name,email,created_date) VALUES(3, '333','333@yahoo.com', '2017-02-13');

            INSERT INTO customer_password (customer_id, password, created_date, modified_date) VALUES
            (1, '11111a', '2024-06-28', '2024-06-30');
            """)
    void testListByCustomerIds() throws Exception {
        final var action = this.mockMvc.perform(post("/listByCustomerIds.json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsBytes(List.of(1)))).andDo(print());
        action.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(is(1)))
                .andExpect(jsonPath("$.[0].name").value(is("111")))
                .andExpect(jsonPath("$.[0].email").value(is("111@yahoo.com")))
                .andExpect(jsonPath("$.[0].password.password").value(is("11111a")))

                .andExpect(jsonPath("$.[?(@.name == '111' && @.email == '111@yahoo.com')]").exists());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testListByCustomerIdsWithNullOrEmptyIds(List<Integer> customerIds) throws Exception {
        final var action = this.mockMvc.perform(post("/listByCustomerIds.json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsBytes(customerIds))).andDo(print());
        action.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Bad Request"));
    }

    @Test
    @Sql(statements = """
            INSERT INTO customer(id, name,email,created_date) VALUES(1, '111','111@yahoo.com', '2017-02-11');
            INSERT INTO customer(id, name,email,created_date) VALUES(2, '222','222@yahoo.com', '2017-02-12');
            INSERT INTO customer(id, name,email,created_date) VALUES(3, '333','333@yahoo.com', '2017-02-13');

            INSERT INTO customer_password (customer_id, password, created_date, modified_date) VALUES
            (2, '22222a', '2024-06-28', '2024-06-30');
            """)
    void testListByPage() throws Exception {
        final var action = this.mockMvc.perform(get("/listByPage.json")
                .param("pageNumber", "1")
                .param("pageSize", "1")).andDo(print());
        action.andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(is(3)))
                .andExpect(jsonPath("$.totalPages").value(is(3)))
                .andExpect(jsonPath("$.content.length()").value(is(1)))
                .andExpect(jsonPath("$.content.[0].name").value(is("222")))
                .andExpect(jsonPath("$.content.[0].email").value(is("222@yahoo.com")))
                .andExpect(jsonPath("$.content.[0].password.password").value(is("22222a")))

                .andExpect(jsonPath("$.content.[?(@.name == '222' && @.email == '222@yahoo.com')]").exists());
    }

    @Test
    @Sql(statements = """
            INSERT INTO customer(id, name,email,created_date) VALUES(1, '111','111@yahoo.com', '2017-02-11');
            INSERT INTO customer(id, name,email,created_date) VALUES(2, '222','222@yahoo.com', '2017-02-12');
            INSERT INTO customer(id, name,email,created_date) VALUES(3, '333','333@yahoo.com', '2017-02-13');

            INSERT INTO customer_password (customer_id, password, created_date, modified_date) VALUES
            (1, '11111a', '2024-06-28', '2024-06-30'),
            (2, '22222b', '2024-06-29', '2024-06-30'),
            (3, '33333c', '2024-06-30', '2024-06-30');

            INSERT INTO customer_ops_log (customer_id, event, created_date) VALUES
            (1, 'create password', '2024-06-28'),
            (1, 'create password', '2024-06-29'),
            (2, 'create password', '2024-06-30');
            """, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void testList() throws Exception {
        final var action = this.mockMvc.perform(post("/list.json")).andDo(print());
        action.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(is(3)))
                .andExpect(jsonPath("$.[0].name").value(is("111")))
                .andExpect(jsonPath("$.[0].email").value(is("111@yahoo.com")))
                .andExpect(jsonPath("$.[0].password.password").value(is("11111a")))
                .andExpect(jsonPath("$.[0].opsLogs.[0].createdDate").value(is("2024-06-28")))

                .andExpect(jsonPath("$.[?(@.name == '111' && @.email == '111@yahoo.com')].opsLogs.length()").value(contains(2)))
                .andExpect(jsonPath("$.[?(@.name == '222' && @.email == '222@yahoo.com')].opsLogs.length()").value(contains(1)))
                .andExpect(jsonPath("$.[?(@.name == '333' && @.email == '333@yahoo.com')]").exists())

                .andExpect(jsonPath("$.[*].id").value(containsInAnyOrder(1, 2, 3)));
    }
}
