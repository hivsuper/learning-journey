package org.lxp.weixin.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lxp.weixin.service.WXService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class WeChatAuthenticationProvider implements AuthenticationProvider {
    private final WXService wxService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final var token = (WeChatAuthenticationToken) authentication;
        final var code = token.getOpenId();
        final var userDetails = wxService.loadUserByUsername(code);

        final var weChatAuthenticationToken = WeChatAuthenticationToken.authentication(
                userDetails.getUsername(),
                userDetails,
                userDetails.getAuthorities()
        );
        weChatAuthenticationToken.setDetails(token.getDetails());
        return weChatAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WeChatAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
