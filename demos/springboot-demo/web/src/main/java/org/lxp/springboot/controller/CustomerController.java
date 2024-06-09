package org.lxp.springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.springboot.dto.Customer;
import org.lxp.springboot.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @Operation(
            summary = "Add an customer",
            description = "Return the added customer id.")
    @PostMapping(value = "/add.json")
    public ResponseEntity<Integer> add(@RequestParam String name, @RequestParam String email) {
        return ResponseEntity.ok(customerService.addCustomer(name, email));
    }

    @Operation(
            summary = "Query Customers by Customer Ids",
            description = "Return the account list by customer ids.")
    @PostMapping(value = "/listByCustomerIds.json")
    public ResponseEntity<List<Customer>> list(@RequestBody List<Integer> customerIds) {
        return ResponseEntity.ok(customerService.findCustomerByIds(customerIds));
    }

    @Operation(
            summary = "Query All Customers",
            description = "Return the account list.")
    @PostMapping(value = "/list.json")
    public ResponseEntity<List<Customer>> list() {
        return ResponseEntity.ok(customerService.findAll());
    }
}
