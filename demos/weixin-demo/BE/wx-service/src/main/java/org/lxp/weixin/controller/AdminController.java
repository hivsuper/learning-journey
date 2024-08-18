package org.lxp.weixin.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.weixin.dto.AdminDto;
import org.lxp.weixin.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @Operation(
            summary = "Admin login",
            description = "Admin login")
    @GetMapping(value = "/login")
    public ResponseEntity login(@RequestBody AdminDto adminDto) {
        adminService.login(adminDto.getUsername(), adminDto.getPassword());
        return ResponseEntity.ok().build();
    }
}
