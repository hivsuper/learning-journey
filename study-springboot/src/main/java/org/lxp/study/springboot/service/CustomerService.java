package org.lxp.study.springboot.service;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.lxp.study.springboot.dao.CustomerBaseMapper;
import org.lxp.study.springboot.model.CustomerBase;
import org.lxp.study.springboot.model.CustomerBaseExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerService.class);
    @Resource
    private CustomerBaseMapper customerBaseMapper;

    @Transactional
    public void addCustomer(String name, String email) {
        LOG.info("add name={} email={}", name, email);
        CustomerBase customer = new CustomerBase();
        customer.setName(name);
        customer.setEmail(email);
        customer.setCreatedDate(Calendar.getInstance().getTime());
        customerBaseMapper.insert(customer);
    }

    public List<CustomerBase> findAll() {
        CustomerBaseExample example = new CustomerBaseExample();
        return customerBaseMapper.selectByExample(example);
    }
}
