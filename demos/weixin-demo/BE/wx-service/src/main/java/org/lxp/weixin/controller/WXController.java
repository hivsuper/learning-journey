package org.lxp.weixin.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.weixin.service.WXService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/wx")
public class WXController {
    private final WXService wxService;

    @Operation(
            summary = "Query WeiXin sessionId",
            description = "Receive code and return WeiXin sessionId.")
    @GetMapping(value = "/sessionId")
    public ResponseEntity sessionId(@RequestParam String jsCode) {
        wxService.loginOrRegister(jsCode);
        return ResponseEntity.ok().build();
    }
}