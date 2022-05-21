package com.inhabas.api.auth.utils.argumentResolver;

import lombok.Getter;

import java.util.List;

public abstract class AuthenticationResult {

    protected @Getter
    Integer memberId;

    protected String role;

    protected @Getter
    List<String> teams;

    public String getRoleString() {
        return this.role;
    }
}
