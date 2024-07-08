package org.lxp.jpa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.jpa.entity.Customer;
import org.lxp.jpa.entity.Password;
import org.lxp.jpa.repository.CustomerRepository;
import org.lxp.jpa.repository.PasswordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final PasswordRepository passwordRepository;

    @Transactional
    public Integer addCustomer(String name, String email, String password) {
        Integer integer = doAddCustomer(name, email, password);
        return integer;
    }

    private Integer doAddCustomer(String name, String email, String password) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setCreatedDate(LocalDate.now());
        customerRepository.save(customer);

        Password p = new Password();
        p.setCustomerId(customer.getId());
        p.setPassword(password);
        p.setCreatedDate(LocalDate.now());
        passwordRepository.save(p);

        log.info("add customer:{} successfully", customer.getId());
        return customer.getId();
    }

    public List<Customer> findCustomerByIds(List<Integer> customerIds) {
        return customerRepository.findAllById(customerIds);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
}
