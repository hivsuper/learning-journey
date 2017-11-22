package org.lxp.springboot.service;

import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;

import org.lxp.springboot.dao.secondary.CustomerSlaveBaseMapper;
import org.lxp.springboot.model.secondary.CustomerSlaveBase;
import org.lxp.springboot.model.secondary.CustomerSlaveBaseExample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerSlaveService {
    private static final Logger LOG = LoggerFactory.getLogger(CustomerSlaveService.class);
    @Resource
    private CustomerSlaveBaseMapper customerSlaveBaseMapper;

    @Transactional
    public void addCustomer(String name, String email) {
        LOG.info("add name={} email={}", name, email);
        CustomerSlaveBase customer = new CustomerSlaveBase();
        customer.setName(name);
        customer.setEmail(email);
        customer.setCreatedDate(Calendar.getInstance().getTime());
        customerSlaveBaseMapper.insert(customer);
    }

    public List<CustomerSlaveBase> findAll() {
        CustomerSlaveBaseExample example = new CustomerSlaveBaseExample();
        return customerSlaveBaseMapper.selectByExample(example);
    }
}
