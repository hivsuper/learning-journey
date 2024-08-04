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
        return doAddCustomer(name, email, password);
    }

    private Integer doAddCustomer(String name, String email, String password) {
        Customer customer = Customer.builder()
                .name(name)
                .email(email)
                .createdDate(LocalDate.now())
                .build();
        customerRepository.save(customer);

        Password p = Password.builder()
                .customerId(customer.getId())
                .password(password).createdDate(LocalDate.now()).build();
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
