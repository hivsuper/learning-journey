package org.lxp.springboot.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.springboot.dto.Customer;
import org.lxp.springboot.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
            summary = "Add an customer asynchronously",
            description = "Return the added customer id.")
    @PostMapping(value = "/addAsync.json")
    public ResponseEntity<Void> addAsync(@RequestParam String name, @RequestParam String email) {
        customerService.addCustomerAsync(name, email);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Add an customer",
            description = "Return the added customer id.")
    @PostMapping(value = "/add.json")
    public ResponseEntity<Integer> add(@RequestParam String name, @RequestParam String email) {
        return ResponseEntity.ok(customerService.addCustomer(name, email));
    }

    @Operation(
            summary = "Query Customers by Customer Ids",
            description = "Return the customer list by customer ids.")
    @PostMapping(value = "/listByCustomerIds.json")
    public ResponseEntity<List<Customer>> list(@RequestBody List<Integer> customerIds) {
        return ResponseEntity.ok(customerService.findCustomerByIds(customerIds));
    }

    @Operation(
            summary = "Query Customers by Customer Id",
            description = "Return the customer by customer id.")
    @GetMapping(value = "/findCustomerById.json")
    public ResponseEntity<Customer> findCustomerById(@RequestParam Integer customerId) {
        return ResponseEntity.ok(customerService.findCustomerById(customerId));
    }

    @Operation(
            summary = "Query All Customers",
            description = "Return the customer list.")
    @PostMapping(value = "/list.json")
    public ResponseEntity<List<Customer>> list() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @Operation(
            summary = "Notify",
            description = "Send notification")
    @PostMapping(value = "/notify.json")
    public ResponseEntity<Boolean> notify(@RequestParam String toAddress) {
        return ResponseEntity.ok(customerService.sendEmail(toAddress));
    }
}
