package org.lxp.springboot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.springboot.dao.CustomerMapper;
import org.lxp.springboot.dto.Customer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerMapper customerMapper;

    @Transactional
    public int addCustomer(String name, String email) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setCreatedDate(Calendar.getInstance().getTime());
        customerMapper.add(customer);
        log.info("add customer:{} successfully", customer.getId());
        return customer.getId();
    }

    public List<Customer> findCustomerByIds(List<Integer> customerIds) {
        return customerMapper.findByIds(customerIds);
    }

    public List<Customer> findAll() {
        return customerMapper.findAllCustomers();
    }
}
