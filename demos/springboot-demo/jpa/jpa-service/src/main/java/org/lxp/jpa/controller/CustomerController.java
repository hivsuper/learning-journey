package org.lxp.jpa.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.jpa.entity.Customer;
import org.lxp.jpa.service.CustomerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerController {
    @Value("${spring.servlet.multipart.location}")
    private String location;
    private final CustomerService customerService;

    @Operation(
            summary = "Add an customer",
            description = "Return the added customer id.")
    @PostMapping(value = "/add.json")
    public ResponseEntity<Integer> add(@RequestParam String name,
                                       @RequestParam String email,
                                       @RequestParam(required = false) String password) {
        return ResponseEntity.ok(customerService.addCustomer(name, email, password));
    }

    @Operation(
            summary = "Query customers by Customer Ids",
            description = "Return the customer list by customer ids.")
    @PostMapping(value = "/listByCustomerIds.json")
    public ResponseEntity<List<Customer>> listByCustomerIds(@RequestBody List<Integer> customerIds) {
        return ResponseEntity.ok(customerService.findCustomerByIds(customerIds));
    }

    @Operation(
            summary = "Query Customer page",
            description = "Return the customer page.")
    @GetMapping(value = "/listByPage.json")
    public ResponseEntity<Page<Customer>> listByPage(@RequestParam int pageNumber, @RequestParam int pageSize) {
        return ResponseEntity.ok(customerService.findAll(pageNumber, pageSize));
    }

    @Operation(
            summary = "Query all customers",
            description = "Return the customer list.")
    @PostMapping(value = "/list.json")
    public ResponseEntity<List<Customer>> list() {
        return ResponseEntity.ok(customerService.findAll());
    }

    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) {
        try {
            File destFile = new File(location + File.separator + file.getOriginalFilename()); // 指定保存的路径
            file.transferTo(destFile);
            return ResponseEntity.ok(destFile.getAbsolutePath());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
