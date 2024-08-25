package org.lxp.jpa.service;

import org.junit.jupiter.api.Test;
import org.lxp.jpa.entity.Customer;
import org.lxp.jpa.entity.Password;
import org.lxp.jpa.repository.CustomerRepository;
import org.lxp.jpa.repository.PasswordRepository;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.slf4j.MDC;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import javax.inject.Inject;
import java.security.InvalidParameterException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
@SpringBootTest(classes = CustomerService.class)
public class CustomerServiceTest {
    private final String NAME = "name";
    private final String EMAIL = "email";
    private final String PASSWORD = "password";
    @MockBean
    private CustomerRepository customerRepository;
    @MockBean
    private PasswordRepository passwordRepository;
    @Inject
    private CustomerService customerService;
    @Captor
    private ArgumentCaptor<Customer> captor;
    @Captor
    private ArgumentCaptor<Password> passwordCaptor;

    @Test
    public void addCustomer() {
        MDC.put("key", "addCustomer");
        customerService.addCustomer(NAME, EMAIL, PASSWORD);

        verify(customerRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo(NAME);
        assertThat(captor.getValue().getEmail()).isEqualTo(EMAIL);
        verify(passwordRepository, times(1)).save(passwordCaptor.capture());
        assertThat(passwordCaptor.getValue().getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    public void addCustomerWhenThrowInvalidParameterException() {
        doAnswer((invocation) -> {
            throw new InvalidParameterException("test");
        }).when(customerRepository).save(any(Customer.class));

        var exception = assertThrows(InvalidParameterException.class, () -> customerService.addCustomer(NAME, EMAIL, PASSWORD), "test");

        assertThat(exception.getClass()).isEqualTo(InvalidParameterException.class);
        verify(customerRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getName()).isEqualTo(NAME);
        assertThat(captor.getValue().getEmail()).isEqualTo(EMAIL);
    }
}