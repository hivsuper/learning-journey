package org.lxp.springboot.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import javax.annotation.Resource;

import org.lxp.springboot.model.primary.CustomerBase;
import org.lxp.springboot.model.secondary.CustomerSlaveBase;
import org.lxp.springboot.service.CustomerService;
import org.lxp.springboot.service.CustomerSlaveService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

@RestController
public class CustomerController {
    @Resource
    private CustomerService customerService;
    @Resource
    private CustomerSlaveService customerSlaveService;

    @RequestMapping(value = "/add.json", method = POST)
    @ApiOperation(value = "Add Customer")
    public boolean add(@RequestParam String name, @RequestParam String email, @RequestParam boolean rollback) {
        customerService.addCustomer(name, email, rollback);
        return true;
    }

    @RequestMapping(value = "/list.json", method = GET)
    @ApiOperation(value = "Query All Customers")
    public List<CustomerBase> list1() {
        return customerService.findAll();
    }

    @RequestMapping(value = "/listSlave.json", method = GET)
    @ApiOperation(value = "Query All Customers")
    public List<CustomerSlaveBase> list2() {
        return customerSlaveService.findAll();
    }
}
