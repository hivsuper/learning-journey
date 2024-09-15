package org.lxp.weixin.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.weixin.jwt.JWTAccessDeniedHandler;
import org.lxp.weixin.jwt.JWTAuthenticationEntryPoint;
import org.lxp.weixin.jwt.JWTFilter;
import org.lxp.weixin.jwt.WeChatAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.lxp.weixin.enumeration.RoleEnum.EMPLOYEE;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    public static final String[] WX_LOGIN_PATHS = {"/wx/login"};

    private final JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final JWTAccessDeniedHandler jwtAccessDeniedHandler;

    private final WeChatAuthenticationFilter weChatAuthenticationFilter;

    private final JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/user/**").hasAuthority(EMPLOYEE.name())
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptions -> exceptions
                        // 未登录
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        // 权限不足
                        .accessDeniedHandler(jwtAccessDeniedHandler)
                )
                .addFilterBefore(weChatAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer ignoringCustomizer() {
        return (web -> web.ignoring().requestMatchers(
                "/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/actuator/health"
        ));
    }
}
