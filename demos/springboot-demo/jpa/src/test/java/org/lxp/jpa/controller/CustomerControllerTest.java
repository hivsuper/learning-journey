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
import java.util.Arrays;

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
            DELETE FROM customer WHERE email='555@555.com';
            """)
    public void testAdd() throws Exception {
        ResultActions action = this.mockMvc.perform(post("/add.json")
                .param("name", "555")
                .param("email", "555@555.com"));
        action.andExpect(status().isOk());
        action.andExpect(content().string(is("1")));
    }

    @Test
    @Sql(statements = """
            INSERT INTO customer(id, name,email,created_date) VALUES(1, '111','111@yahoo.com', '2017-02-11');
            INSERT INTO customer(id, name,email,created_date) VALUES(2, '222','222@yahoo.com', '2017-02-12');
            INSERT INTO customer(id, name,email,created_date) VALUES(3, '333','333@yahoo.com', '2017-02-13');
            """)
    public void testListByCustomerIds() throws Exception {
        ResultActions action = this.mockMvc.perform(post("/listByCustomerIds.json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsBytes(Arrays.asList(1)))).andDo(print());
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$.length()").value(is(1)));
        action.andExpect(jsonPath("$.[0].name").value(is("111")));
        action.andExpect(jsonPath("$.[0].email").value(is("111@yahoo.com")));
    }

    @Test
    @Sql(statements = """
            INSERT INTO customer(id, name,email,created_date) VALUES(1, '111','111@yahoo.com', '2017-02-11');
            INSERT INTO customer(id, name,email,created_date) VALUES(2, '222','222@yahoo.com', '2017-02-12');
            INSERT INTO customer(id, name,email,created_date) VALUES(3, '333','333@yahoo.com', '2017-02-13');
            """)
    public void testList() throws Exception {
        ResultActions action = this.mockMvc.perform(post("/list.json")).andDo(print());
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$.length()").value(is(3)));
        action.andExpect(jsonPath("$.[0].name").value(is("111")));
        action.andExpect(jsonPath("$.[0].email").value(is("111@yahoo.com")));
    }
}
