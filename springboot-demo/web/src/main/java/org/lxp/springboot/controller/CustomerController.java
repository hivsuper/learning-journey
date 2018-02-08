package org.lxp.springboot.controller;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.annotation.Resource;

import org.lxp.springboot.model.CustomerBase;
import org.lxp.springboot.service.CustomerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
public class CustomerController {
    @Resource
    private CustomerService customerService;

    @RequestMapping(value = "/add.json", method = POST)
    @ApiOperation(value = "Add Customer")
    public boolean add(@RequestParam String name, @RequestParam String email, @RequestParam boolean isSleep) {
        customerService.addCustomer(name, email, isSleep);
        return true;
    }

    @RequestMapping(value = "/list.json", method = POST)
    @ApiOperation(value = "Query All Customers")
    public List<CustomerBase> list() {
        return customerService.findAll();
    }
}
