package com.inhabas.api.security.oauth2;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    public CustomAuthenticationFailureHandler(String failureUrl) {
        setDefaultFailureUrl(failureUrl);
    }
}
