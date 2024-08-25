package org.lxp.jpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {
    private final StringRedisTemplate stringRedisTemplate;

    @PostMapping("/add")
    public ResponseEntity<Void> set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get")
    public ResponseEntity<String> get(String key) {
        return ResponseEntity.ok(stringRedisTemplate.opsForValue().get(key));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> delete(String key) {
        return ResponseEntity.ok(stringRedisTemplate.delete(key));
    }
}