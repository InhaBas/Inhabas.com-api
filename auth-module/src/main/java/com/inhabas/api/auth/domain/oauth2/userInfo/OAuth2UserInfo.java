package com.inhabas.api.auth.domain.oauth2.userInfo;

import org.springframework.util.StringUtils;

import java.util.Map;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;
    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    public Map<String, Object> getAttributes() {
        return attributes;
    }
    public abstract String getId();
    public abstract String getName();
    public abstract String getEmail();
    public abstract String getImageUrl();
    public abstract Map<String, Object>  getExtraData();

    public boolean validateNecessaryFields() {
        return StringUtils.hasText(this.getEmail())
                && StringUtils.hasText(this.getId())
                && StringUtils.hasText(this.getName())
                && StringUtils.hasText(this.getImageUrl());
    }
}
