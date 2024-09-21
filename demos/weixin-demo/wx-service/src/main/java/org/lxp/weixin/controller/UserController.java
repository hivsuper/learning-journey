package org.lxp.weixin.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    @Operation(summary = "Query profile of current login user")
    @GetMapping(value = "/my-profile")
    public ResponseEntity<String> myProfile(Authentication authentication) {
        return ResponseEntity.ok(authentication.getName());
    }
}
