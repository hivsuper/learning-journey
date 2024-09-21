package org.lxp.weixin.config;

import org.lxp.weixin.jwt.WeChatAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.stereotype.Component;

@Component
public class LoginConfig {
    @Bean
    public AuthenticationManager authenticationManager(WeChatAuthenticationProvider weChatAuthenticationProvider) {
        return new ProviderManager(weChatAuthenticationProvider);
    }
}
