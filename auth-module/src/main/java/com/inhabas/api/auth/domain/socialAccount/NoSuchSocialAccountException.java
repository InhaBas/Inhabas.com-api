package com.inhabas.api.auth.domain.socialAccount;

public class NoSuchSocialAccountException extends RuntimeException {

    public NoSuchSocialAccountException() {
        super("해당 소셜 계정이 존재하지 않습니다,");
    }
}
