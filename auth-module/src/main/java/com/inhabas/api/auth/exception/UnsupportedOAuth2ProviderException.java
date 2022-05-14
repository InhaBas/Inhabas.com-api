package com.inhabas.api.auth.exception;

public class UnsupportedOAuth2ProviderException extends CustomAuthException {

    public UnsupportedOAuth2ProviderException() {
        super(AuthExceptionCodes.UNSUPPORTED_OAUTH2_PROVIDER);
    }
}
