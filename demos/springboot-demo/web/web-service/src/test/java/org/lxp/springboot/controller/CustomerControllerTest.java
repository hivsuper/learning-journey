package org.lxp.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.lxp.springboot.BaseTest;
import org.lxp.springboot.config.CachingConfig;
import org.lxp.springboot.dto.Customer;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.inject.Inject;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
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
    @Inject
    private CacheManager cacheManager;

    @Test
    void testAdd() throws Exception {
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
    void testListByCustomerIds() throws Exception {
        ResultActions action = this.mockMvc.perform(post("/listByCustomerIds.json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsBytes(List.of(1)))).andDo(print());
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$.length()").value(is(1)));
        action.andExpect(jsonPath("$.[0].name").value(is("111")));
        action.andExpect(jsonPath("$.[0].email").value(is("111@yahoo.com")));

        action.andExpect(jsonPath("$.[?(@.name == '111' && @.email == '111@yahoo.com')]").exists());
    }

    @Test
    @Sql(statements = """
            INSERT INTO customer(id, name,email,created_date) VALUES(1, '111','111@yahoo.com', '2017-02-11');
            INSERT INTO customer(id, name,email,created_date) VALUES(2, '222','222@yahoo.com', '2017-02-12');
            INSERT INTO customer(id, name,email,created_date) VALUES(3, '333','333@yahoo.com', '2017-02-13');
            """)
    void testFindCustomerById() throws Exception {
        final var cache = cacheManager.getCache(CachingConfig.CUSTOMER_CACHE);
        assertNotNull(cache);
        assertNull(cache.get(1));

        ResultActions action = this.mockMvc.perform(get("/findCustomerById.json")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("customerId", "1")).andDo(print());
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$.id").value(is(1)));
        action.andExpect(jsonPath("$.name").value(is("111")));
        action.andExpect(jsonPath("$.email").value(is("111@yahoo.com")));

        action.andExpect(jsonPath("$.[?(@.name == '111' && @.email == '111@yahoo.com')]").exists());

        final var result = cache.get(1);
        assertNotNull(result);
        final var customer = (Customer) result.get();
        assertNotNull(customer);
        assertEquals(1, customer.getId());
        assertEquals("111", customer.getName());
        assertEquals("111@yahoo.com", customer.getEmail());
    }

    @Test
    @Sql(statements = """
            INSERT INTO customer(id, name,email,created_date) VALUES(1, '111','111@yahoo.com', '2017-02-11');
            INSERT INTO customer(id, name,email,created_date) VALUES(2, '222','222@yahoo.com', '2017-02-12');
            INSERT INTO customer(id, name,email,created_date) VALUES(3, '333','333@yahoo.com', '2017-02-13');
            """)
    void testList() throws Exception {
        ResultActions action = this.mockMvc.perform(post("/list.json")).andDo(print());
        action.andExpect(status().isOk());
        action.andExpect(jsonPath("$.length()").value(is(3)));
        action.andExpect(jsonPath("$.[0].name").value(is("111")));
        action.andExpect(jsonPath("$.[0].email").value(is("111@yahoo.com")));

        action.andExpect(jsonPath("$.[?(@.name == '111' && @.email == '111@yahoo.com')]").exists())
                .andExpect(jsonPath("$.[?(@.name == '222' && @.email == '222@yahoo.com')]").exists())
                .andExpect(jsonPath("$.[?(@.name == '333' && @.email == '333@yahoo.com')]").exists());

        action.andExpect(jsonPath("$.[*].id").value(containsInAnyOrder(1, 2, 3)));
    }

    @Test
    void testNotify() throws Exception {
        ResultActions action = this.mockMvc.perform(post("/notify.json")
                .param("toAddress", "1@1.com")).andDo(print());
        action.andExpect(status().isOk());
        action.andExpect(content().string(Boolean.TRUE.toString()));
    }
}
