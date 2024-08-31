package org.lxp.jpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {
    private final StringRedisTemplate stringRedisTemplate;

    @PostMapping("/add")
    public CompletableFuture<ResponseEntity<Void>> set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
        return CompletableFuture.completedFuture(ResponseEntity.ok().build());
    }

    @GetMapping("/get")
    public CompletableFuture<ResponseEntity<String>> get(String key) {
        return CompletableFuture.completedFuture(ResponseEntity.ok(stringRedisTemplate.opsForValue().get(key)));
    }

    @DeleteMapping("/delete")
    public CompletableFuture<ResponseEntity<Boolean>> delete(String key) {
        return CompletableFuture.completedFuture(ResponseEntity.ok(stringRedisTemplate.delete(key)));
    }
}