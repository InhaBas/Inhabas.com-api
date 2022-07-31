package com.inhabas.api.auth.domain.token.securityFilter;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class InvalidJwtTokenHandler extends TokenAuthenticationFailureHandler {

    /**
     * Performs the redirect or forward to the {@code defaultFailureUrl} if set, otherwise
     * returns a 401 error code.
     * <p>
     * If redirecting or forwarding, {@code saveException} will be called to cache the
     * exception for use in the target view.
     * @see SimpleUrlAuthenticationFailureHandler
     */
    public InvalidJwtTokenHandler() {
        super();
        setAllowSessionCreation(false);
    }
}
