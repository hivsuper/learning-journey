package org.lxp.jpa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.lxp.jpa.config.MemoryDBTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@MemoryDBTest
public class CustomerControllerTest {
    @Inject
    private MockMvc mockMvc;

    @Test
    @Sql(statements = """
            DELETE FROM customer_password WHERE customer_id IN(SELECT id FROM customer WHERE email='555@555.com');
            DELETE FROM customer WHERE email='555@555.com';
            """)
    public void testAdd() throws Exception {
        ResultActions action = this.mockMvc.perform(post("/add.json")
                .param("name", "555")
                .param("password", "555P")
                .param("email", "555@555.com"));
        action.andExpect(status().isOk());
        action.andExpect(content().string(is("1")));
    }

    @Test
    @Sql(statements = """
            INSERT INTO customer(id, name,email,created_date) VALUES(1, '111','111@yahoo.com', '2017-02-11');
            INSERT INTO customer(id, name,email,created_date) VALUES(2, '222','222@yahoo.com', '2017-02-12');
            INSERT INTO customer(id, name,email,created_date) VALUES(3, '333','333@yahoo.com', '2017-02-13');

            INSERT INTO customer_password (customer_id, password, created_date, modified_date) VALUES
            (1, '11111a', '2024-06-28', '2024-06-30');
            """)
    public void testListByCustomerIds() throws Exception {
        ResultActions action = this.mockMvc.perform(post("/listByCustomerIds.json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsBytes(List.of(1)))).andDo(print());
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$.length()").value(is(1)));
        action.andExpect(jsonPath("$.[0].name").value(is("111")));
        action.andExpect(jsonPath("$.[0].email").value(is("111@yahoo.com")));
        action.andExpect(jsonPath("$.[0].password.password").value(is("11111a")));

        action.andExpect(jsonPath("$.[?(@.name == '111' && @.email == '111@yahoo.com')]").exists());
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
            """)
    public void testList() throws Exception {
        ResultActions action = this.mockMvc.perform(post("/list.json")).andDo(print());
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$.length()").value(is(3)));
        action.andExpect(jsonPath("$.[0].name").value(is("111")));
        action.andExpect(jsonPath("$.[0].email").value(is("111@yahoo.com")));
        action.andExpect(jsonPath("$.[0].password.password").value(is("11111a")));
        action.andExpect(jsonPath("$.[0].opsLogs.[0].createdDate").value(is("2024-06-28")));

        action.andExpect(jsonPath("$.[?(@.name == '111' && @.email == '111@yahoo.com')].opsLogs.length()").value(contains(2)))
                .andExpect(jsonPath("$.[?(@.name == '222' && @.email == '222@yahoo.com')].opsLogs.length()").value(contains(1)))
                .andExpect(jsonPath("$.[?(@.name == '333' && @.email == '333@yahoo.com')]").exists());

        action.andExpect(jsonPath("$.[*].id").value(containsInAnyOrder(1, 2, 3)));
    }
}
