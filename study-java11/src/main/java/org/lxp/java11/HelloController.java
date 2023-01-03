package org.lxp.java11;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class HelloController {

    @GetMapping("/")
    public String get(@RequestParam(required = false) String param) {
        var input = "default";
        return String.format("get %s", Optional.ofNullable(param).orElse(input));
    }

    @PostMapping("/")
    public String post(@RequestBody String body) {
        return String.format("post %s", body);
    }
}
