package org.lxp.springboot.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.lxp.springboot.model.primary.CustomerBase;
import org.lxp.springboot.service.CustomerService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Transactional
public class CustomerControllerTest extends BaseControllerTest {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        super.mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void testAdd() throws Exception {
        super.mvc.perform(post("/add.json").param("name", "555").param("email", "555@555.com"))
                .andExpect(status().isOk()).andExpect(content().string("true"));
    }

    @Test
    public void testList() throws Exception {
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        CustomerService customerService = context.getBean(CustomerService.class);
        if (customerService.findAll().isEmpty()) {
            customerService.addCustomer("444", "444@444.com");
        }

        String rtn = super.mvc.perform(get("/list.json")).andExpect(status().isOk()).andReturn().getResponse()
                .getContentAsString();
        final TypeReference<List<CustomerBase>> REFERENCE = new TypeReference<List<CustomerBase>>() {
        };
        List<CustomerBase> list = objectMapper.readValue(rtn, REFERENCE);
        Assert.assertFalse(list.isEmpty());
    }

}
