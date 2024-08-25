package org.lxp.springboot.service;

import org.junit.jupiter.api.Test;
import org.lxp.springboot.config.AsyncConfig;
import org.lxp.springboot.dao.CustomerMapper;
import org.lxp.springboot.dto.Customer;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.slf4j.MDC;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.retry.ExhaustedRetryException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Inject;
import java.security.InvalidParameterException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest(classes = CustomerService.class)
@ContextConfiguration(classes = AsyncConfig.class)
class CustomerServiceTest {
    private final String NAME = "name";
    private final String EMAIL = "email";
    @MockBean
    private CustomerMapper customerMapper;
    @MockBean
    private EmailService emailService;
    @Inject
    private CustomerService customerService;
    @Captor
    private ArgumentCaptor<Customer> captor;

    @Test
    void recover() throws ExecutionException, InterruptedException {
        assertThat(customerService.recover(new RuntimeException(), NAME, EMAIL).get()).isEqualTo(1);
    }

    @Test
    void addCustomerAsync() {
        MDC.put("key", "addCustomerAsync");
        Future<Integer> future = customerService.addCustomerAsync(NAME, EMAIL);
        await().until(future::isDone);

        verify(customerMapper, times(1)).add(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo(NAME);
        assertThat(captor.getValue().getEmail()).isEqualTo(EMAIL);
    }

    @Test
    void addCustomerAsyncWhenThrowInvalidParameterException() throws ExecutionException, InterruptedException {
        doAnswer((invocation) -> {
            throw new InvalidParameterException("test");
        }).when(customerMapper).add(any(Customer.class));

        Future<Integer> future = customerService.addCustomerAsync(NAME, EMAIL);
        await().until(future::isDone);

        assertThat(future.get()).isGreaterThan(0);
        verify(customerMapper, times(2)).add(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo(NAME);
        assertThat(captor.getValue().getEmail()).isEqualTo(EMAIL);
    }

    @Test
    void addCustomerAsyncWhenThrowRuntimeException() throws ExecutionException, InterruptedException {
        doAnswer((invocation) -> {
            throw new RuntimeException("test");
        }).when(customerMapper).add(any(Customer.class));

        Future<Integer> future = customerService.addCustomerAsync(NAME, EMAIL);
        await().until(future::isDone);

        assertThat(future.get()).isGreaterThan(0);
        verify(customerMapper, times(1)).add(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo(NAME);
        assertThat(captor.getValue().getEmail()).isEqualTo(EMAIL);
    }

    @Test
    void addCustomer() {
        MDC.put("key", "addCustomer");
        customerService.addCustomer(NAME, EMAIL);

        verify(customerMapper, times(1)).add(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo(NAME);
        assertThat(captor.getValue().getEmail()).isEqualTo(EMAIL);
    }

    @Test
    void addCustomerWhenThrowInvalidParameterException() {
        doAnswer((invocation) -> {
            throw new InvalidParameterException("test");
        }).when(customerMapper).add(any(Customer.class));

        var exception = assertThrows(ExhaustedRetryException.class, () -> customerService.addCustomer(NAME, EMAIL), "test");

        assertThat(exception.getCause().getClass()).isEqualTo(InvalidParameterException.class);
        verify(customerMapper, times(2)).add(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo(NAME);
        assertThat(captor.getValue().getEmail()).isEqualTo(EMAIL);
    }

    @Test
    void addCustomerWhenThrowRuntimeException() {
        doAnswer((invocation) -> {
            throw new RuntimeException("test");
        }).when(customerMapper).add(any(Customer.class));

        var exception = assertThrows(ExhaustedRetryException.class, () -> customerService.addCustomer(NAME, EMAIL), "Cannot locate recovery method");
        assertThat(exception.getCause().getClass()).isEqualTo(RuntimeException.class);
        verify(customerMapper, times(1)).add(captor.capture());
    }

    @Test
    void sendEmail() {
        String emailAddress = "1@2.com";
        customerService.sendEmail(emailAddress);
        verify(emailService, times(1)).send(emailAddress, "Hello", "Hello <strong> World</strong>！");
    }
}