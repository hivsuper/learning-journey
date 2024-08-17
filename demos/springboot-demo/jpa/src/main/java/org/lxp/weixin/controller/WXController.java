package org.lxp.weixin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.weixin.response.SessionResponse;
import org.lxp.weixin.response.UserInfoResponse;
import org.lxp.weixin.service.WXService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<SessionResponse> sessionId(@RequestParam String jsCode) throws JsonProcessingException {
        return ResponseEntity.ok(wxService.get(jsCode).transform());
    }

    @Operation(
            summary = "Query WeiXin userinfo",
            description = "Query WeiXin userinfo.")
    @PostMapping(value = "/oauth")
    public ResponseEntity<UserInfoResponse> oauth(@RequestParam String encryptedData,
                                                  @RequestParam String iv,
                                                  @RequestParam String jsCode) throws JsonProcessingException {
        return ResponseEntity.ok(wxService.getUserInfo(encryptedData, iv, jsCode));
    }
}
