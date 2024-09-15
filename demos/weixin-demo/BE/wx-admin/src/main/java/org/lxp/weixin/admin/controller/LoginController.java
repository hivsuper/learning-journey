package org.lxp.weixin.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginController {
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    @PostMapping("/login")
    public void login(HttpServletRequest request,
                      HttpServletResponse response,
                      @RequestParam("username") String username,
                      @RequestParam("password") String password
    ) {
        final var token = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
        // 通过前端发来的 username、password 进行认证，这里会用到CustomUserDetailsService.loadUserByUsername
        final var authentication = authenticationManager.authenticate(token);
        // 设置空的上下文
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // 设置认证信息
        context.setAuthentication(authentication);
        // 这句保证了随后的请求都会有这个上下文，通过回话保持，在前端清理 cookie 之后也就失效了
        securityContextRepository.saveContext(context, request, response);
    }
}
