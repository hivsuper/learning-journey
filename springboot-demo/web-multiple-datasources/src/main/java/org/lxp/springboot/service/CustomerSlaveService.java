package org.lxp.springboot.service;

import java.util.List;

import javax.annotation.Resource;

import org.lxp.springboot.dao.secondary.CustomerSlaveBaseMapper;
import org.lxp.springboot.model.secondary.CustomerSlaveBase;
import org.lxp.springboot.model.secondary.CustomerSlaveBaseExample;
import org.springframework.stereotype.Service;

@Service
public class CustomerSlaveService {
    @Resource
    private CustomerSlaveBaseMapper customerSlaveBaseMapper;

    public List<CustomerSlaveBase> findAll() {
        CustomerSlaveBaseExample example = new CustomerSlaveBaseExample();
        return customerSlaveBaseMapper.selectByExample(example);
    }
}
