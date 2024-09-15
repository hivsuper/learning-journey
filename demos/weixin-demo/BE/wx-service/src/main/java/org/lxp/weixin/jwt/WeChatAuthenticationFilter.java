package org.lxp.weixin.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * https://blog.csdn.net/hjg719/article/details/134802533
 */
@Slf4j
@Component
public class WeChatAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String PATH = "/wx/login";
    private static final String JS_CODE = "jsCode";
    private static final AntPathRequestMatcher MATCHER = new AntPathRequestMatcher(PATH);

    public WeChatAuthenticationFilter(AuthenticationManager authenticationManager,
                                      WeChatAuthenticationFailureHandler weChatAuthenticationFailureHandler,
                                      WeChatAuthenticationSuccessHandler weChatAuthenticationSuccessHandler) {
        super(MATCHER, authenticationManager);
        setAuthenticationFailureHandler(weChatAuthenticationFailureHandler);
        setAuthenticationSuccessHandler(weChatAuthenticationSuccessHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response
    ) throws AuthenticationException {
        if (!"POST".equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported:" + request.getMethod());
        }
        final var code = request.getParameter(JS_CODE);
        if (Objects.isNull(code)) {
            throw new AuthenticationServiceException("jsCode can't be null");
        }
        final var token = WeChatAuthenticationToken.unauthentication(code);
        token.setDetails(this.authenticationDetailsSource.buildDetails(request));
        return this.getAuthenticationManager().authenticate(token);
    }
}
