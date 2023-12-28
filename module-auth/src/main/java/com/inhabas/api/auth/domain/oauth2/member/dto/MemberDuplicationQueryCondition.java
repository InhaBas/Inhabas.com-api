package com.inhabas.api.auth.domain.oauth2.member.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.socialAccount.domain.valueObject.UID;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberDuplicationQueryCondition {

    private OAuth2Provider provider;

    private UID uid;

    public MemberDuplicationQueryCondition(OAuth2Provider provider, String uid) {
        this.provider = provider;
        setUid(uid);
    }

    public void verifyTwoParameters() {
        if (Objects.isNull(provider) || Objects.isNull(uid)) {
            throw new InvalidInputException();
        }
    }

    public OAuth2Provider getProvider() {
        return provider;
    }

    /**
     * do not delete this method. this getter's return type is used for get parameter of SignUpController
     */

    public String getUidNumber() {
        return uid.getValue();
    }

    public void setProvider(OAuth2Provider provider) {
        this.provider = provider;
    }

    public void setUid(String uid) {
        this.uid = new UID(uid);
    }

    public UID getUid() {
        return uid;
    }
}
