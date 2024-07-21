package org.lxp.jpa.controller;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Inject
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/add")
    public ResponseEntity<Void> set(String key, String value) throws Exception {
        stringRedisTemplate.opsForValue().set(key, value);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get")
    public ResponseEntity<String> get(String key) throws Exception {
        return ResponseEntity.ok(stringRedisTemplate.opsForValue().get(key));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> delete(String key) throws Exception {
        return ResponseEntity.ok(stringRedisTemplate.delete(key));
    }
}