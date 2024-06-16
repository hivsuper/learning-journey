package org.lxp.springboot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.springboot.dao.CustomerMapper;
import org.lxp.springboot.dto.Customer;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidParameterException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {
    public static final String RETRY_MAX_ATTEMPTS = "${retry.maxAttempts}";
    public static final String RETRY_MAX_DELAY = "${retry.maxDelay}";
    public static final String RECOVER = "recover";

    private final CustomerMapper customerMapper;

    @Recover
    public CompletableFuture<Integer> recover(Exception exception, String name, String email) {
        log.error(exception.getMessage(), exception);
        return CompletableFuture.completedFuture(1);
    }

    @Async
    @Retryable(retryFor = {InvalidParameterException.class},
            maxAttemptsExpression = RETRY_MAX_ATTEMPTS,
            backoff = @Backoff(delayExpression = RETRY_MAX_DELAY), recover = RECOVER)
    @Transactional
    public Future<Integer> addCustomerAsync(String name, String email) {
        Future<Integer> future = CompletableFuture.completedFuture(doAddCustomer(name, email));
        return future;
    }

    @Retryable(retryFor = {InvalidParameterException.class},
            maxAttemptsExpression = RETRY_MAX_ATTEMPTS,
            backoff = @Backoff(delayExpression = RETRY_MAX_DELAY))
    @Transactional
    public Integer addCustomer(String name, String email) {
        Integer integer = doAddCustomer(name, email);
        return integer;
    }

    private Integer doAddCustomer(String name, String email) {
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
