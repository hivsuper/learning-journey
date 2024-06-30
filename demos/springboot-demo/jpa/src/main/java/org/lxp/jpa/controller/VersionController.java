package org.lxp.jpa.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
@Configuration
@PropertySource(value = "classpath:git.properties", ignoreResourceNotFound = true)
@RestController
public class VersionController {
    @Value("${git.commit.id.abbrev:na}")
    private String abbrev;

    @Value("${git.commit.id.full:na}")
    private String full;

    @Value("${git.commit.message.short:na}")
    private String messageShort;

    @Value("${git.commit.time:na}")
    private String time;

    @Operation(summary = "查看版本信息")
    @GetMapping(value = "/version")
    public ResponseEntity<Map<String, String>> version() {
        log.info("version接口被调用！");
        Map<String, String> map = new HashMap<>();
        map.put("abbrev", abbrev);
        map.put("full", full);
        map.put("messageShort", messageShort);
        map.put("time", time);
        return ResponseEntity.ok(map);
    }
}
