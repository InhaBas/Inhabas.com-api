package com.inhabas.api.security.jwtUtils;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class InvalidJwtTokenHandler extends SimpleUrlAuthenticationFailureHandler {

    public InvalidJwtTokenHandler(String defaultFailureUrl) {
        super(defaultFailureUrl);
        setAllowSessionCreation(false);
    }
}
