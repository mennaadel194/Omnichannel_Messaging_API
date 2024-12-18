package com.omnichannel.messagingplatform.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    private final String token;
    private final UserDetails principal;

    public JwtAuthenticationToken(String token) {
        super(null);  // No authorities at the time of token creation
        this.token = token;
        this.principal = null;  // You can set this later once the token is validated and user is authenticated
        setAuthenticated(false);
    }

    public JwtAuthenticationToken(UserDetails principal, String token) {
        super(principal.getAuthorities());
        this.token = token;
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;  // Return the JWT token as credentials
    }

    @Override
    public Object getPrincipal() {
        return principal;  // Return the authenticated principal (the user)
    }
}

