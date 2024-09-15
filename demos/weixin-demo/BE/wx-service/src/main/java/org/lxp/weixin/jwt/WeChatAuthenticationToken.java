package org.lxp.weixin.jwt;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class WeChatAuthenticationToken extends AbstractAuthenticationToken {
    private final String openId;
    private final Object principal;

    public WeChatAuthenticationToken(String openId) {
        super(null);
        this.openId = openId;
        this.principal = openId;
        setAuthenticated(false);
    }

    public WeChatAuthenticationToken(String openId, Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.openId = openId;
        this.principal = principal;
        setAuthenticated(false);
    }

    public static WeChatAuthenticationToken unauthentication(String openId) {
        return new WeChatAuthenticationToken(openId);
    }

    public static WeChatAuthenticationToken authentication(String openId, Object principal, Collection<? extends GrantedAuthority> authorities) {
        return new WeChatAuthenticationToken(openId, principal, authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }
}
