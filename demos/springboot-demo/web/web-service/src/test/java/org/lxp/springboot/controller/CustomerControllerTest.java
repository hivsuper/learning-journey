package org.lxp.springboot.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.lxp.springboot.BaseTest;
import org.lxp.springboot.config.CachingConfig;
import org.lxp.springboot.entity.Customer;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
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
    void testAddAsync() throws Exception {
        final var action = this.mockMvc.perform(post("/addAsync.json")
                .param("name", "555")
                .param("email", "555@555.com"));

        final var mvcResult = action.andExpect(request().asyncStarted()).andReturn();
        this.mockMvc.perform(asyncDispatch(mvcResult)).andExpect(status().isOk())
                .andExpect(content().string(is("1")));
    }

    @DisplayName("Fail to add customer asynchronously when name or email is blank")
    @ParameterizedTest
    @CsvSource(value = {
            "null, 555@555.com, must not be blank, name, null",
            "'', 555@555.com, must not be blank, name, ''",
            "'  ', 555@555.com, must not be blank, name, '  '",
            "555, null, email can't be null or empty, email, null",
            "555, '', 'must match \"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\"', email, ''",
            "555, '  ', 'must match \"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\"', email, '  '",
            "555, @555.com, 'must match \"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\"', email, @555.com",
            "555, 555@, 'must match \"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$\"', email, 555@"
    }, nullValues = "null")
    void testAddAsyncWithNullOrEmptyParameters(
            String name,
            String email,
            String expectedMessage,
            String field,
            String value
    ) throws Exception {
        final var action = this.mockMvc.perform(post("/addAsync.json")
                .param("name", name)
                .param("email", email));
        action.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.reason").value(is(expectedMessage)))
                .andExpect(jsonPath("$.field").value(is(field)))
                .andExpect(jsonPath("$.value").value(is(value)));
    }

    @Test
    void testAdd() throws Exception {
        final var action = this.mockMvc.perform(post("/add.json")
                .param("name", "555")
                .param("email", "555@555.com"));
        action.andExpect(status().isOk())
                .andExpect(content().string(is("1")));
    }

    @DisplayName("Fail to add customer when name or email is blank")
    @ParameterizedTest
    @CsvSource(value = {
            "null, 555@555.com, Required parameter 'name' is not present.",
            "'', 555@555.com, Validation failure",
            "'  ', 555@555.com, Validation failure",
            "555, null, Required parameter 'email' is not present.",
            "555, '', Validation failure",
            "555, '  ', Validation failure"
    }, nullValues = "null")
    void testAddWithNullOrEmptyParameters(String name, String email, String expectedMessage) throws Exception {
        final var action = this.mockMvc.perform(post("/add.json")
                .param("name", name)
                .param("email", email));
        action.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value(is(expectedMessage)));
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
        action.andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(is(1)))
                .andExpect(jsonPath("$.[0].name").value(is("111")))
                .andExpect(jsonPath("$.[0].email").value(is("111@yahoo.com")))

                .andExpect(jsonPath("$.[?(@.name == '111' && @.email == '111@yahoo.com')]").exists());
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
        action.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(is(1)))
                .andExpect(jsonPath("$.name").value(is("111")))
                .andExpect(jsonPath("$.email").value(is("111@yahoo.com")))

                .andExpect(jsonPath("$.[?(@.name == '111' && @.email == '111@yahoo.com')]").exists());

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
        final var action = this.mockMvc.perform(post("/list.json")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(is(3)))
                .andExpect(jsonPath("$.[0].name").value(is("111")))
                .andExpect(jsonPath("$.[0].email").value(is("111@yahoo.com")))

                .andExpect(jsonPath("$.[?(@.name == '111' && @.email == '111@yahoo.com')]").exists())
                .andExpect(jsonPath("$.[?(@.name == '222' && @.email == '222@yahoo.com')]").exists())
                .andExpect(jsonPath("$.[?(@.name == '333' && @.email == '333@yahoo.com')]").exists())

                .andExpect(jsonPath("$.[*].id").value(containsInAnyOrder(1, 2, 3)));
    }

    @Test
    void testNotify() throws Exception {
        final var action = this.mockMvc.perform(post("/notify.json")
                .param("toAddress", "1@1.com")).andDo(print());
        action.andExpect(status().isOk())
                .andExpect(content().string(Boolean.TRUE.toString()));
    }
}
