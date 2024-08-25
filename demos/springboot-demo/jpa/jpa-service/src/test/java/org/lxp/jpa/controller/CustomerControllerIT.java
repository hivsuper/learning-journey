package org.lxp.jpa.controller;

import org.junit.jupiter.api.Test;
import org.lxp.jpa.entity.Customer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("IT")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CustomerControllerIT {
    @LocalServerPort
    private int port;

    @Inject
    private TestRestTemplate restTemplate;

    @Test
    @Sql(statements = """
            DELETE FROM customer_password WHERE customer_id IN(SELECT id FROM customer WHERE email='111@yahoo.com');
            DELETE FROM customer WHERE email='111@yahoo.com';
            """, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = """
            DELETE FROM customer_password WHERE customer_id IN(SELECT id FROM customer WHERE email='111@yahoo.com');
            DELETE FROM customer WHERE email='111@yahoo.com';
            """, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void add() {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(restTemplate.getRootUri() + "/add.json")
                .queryParam("name", "111")
                .queryParam("password", "111P")
                .queryParam("email", "111@yahoo.com");

        ResponseEntity<Integer> response = restTemplate.postForEntity(builder.toUriString(), HttpEntity.EMPTY, Integer.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isGreaterThan(0);
    }

    @Test
    @Sql(statements = """
            INSERT INTO customer(id, name,email,created_date) VALUES(-1, '111','111@yahoo.com', '2017-02-11');
            INSERT INTO customer(id, name,email,created_date) VALUES(-2, '222','222@yahoo.com', '2017-02-22');
            """, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM customer WHERE id IN(-1, -2)", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void listByCustomerIds() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<List<Integer>> requestEntity = new HttpEntity<>(List.of(-1), headers);

        ResponseEntity<List<Customer>> response = restTemplate.exchange(
                restTemplate.getRootUri() + "/list.json", HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {
                });

        List<Customer> customers = response.getBody();
        assertThat(customers).isNotNull();
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(customers.size()).isGreaterThanOrEqualTo(2);
        assertThat(customers.stream().map(Customer::getId).collect(Collectors.toSet())).contains(-1, -2);
    }

    @Test
    @Sql(statements = """
            INSERT INTO customer(id, name,email,created_date) VALUES(-1, '111','111@yahoo.com', '2017-02-11');
            INSERT INTO customer(id, name,email,created_date) VALUES(-2, '222','222@yahoo.com', '2017-02-22');
            """, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "DELETE FROM customer WHERE id IN(-1, -2)", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void testList() {
        ResponseEntity<List<Customer>> response = restTemplate.exchange(
                restTemplate.getRootUri() + "/list.json", HttpMethod.POST, null, new ParameterizedTypeReference<>() {
                });

        List<Customer> customers = response.getBody();
        assertThat(customers).isNotNull();
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(customers.size()).isGreaterThan(0);
        assertThat(customers.stream().map(Customer::getId).collect(Collectors.toSet())).contains(-1, -2);
    }
}