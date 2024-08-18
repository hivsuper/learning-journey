package org.lxp.weixin.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.weixin.entity.User;
import org.lxp.weixin.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Query user list",
            description = "List users by page")
    @GetMapping(value = "/list")
    public ResponseEntity<Page<User>> findUsers(@RequestParam int pageNumber,
                                                @RequestParam int pageNo) {
        return ResponseEntity.ok(userService.findAll(pageNumber, pageNo));
    }
}
