package com.inhabas.api.auth.domain.oauth2.userInfo;

import java.util.Collections;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;

public abstract class OAuth2UserInfo {

  protected Map<String, Object> attributes;
  protected OAuth2Provider provider;

  public OAuth2UserInfo(OAuth2Provider provider, Map<String, Object> attributes) {
    this.provider = provider;
    this.attributes = attributes;
  }

  public OAuth2Provider getProvider() {
    return provider;
  }

  public Map<String, Object> getAttributes() {
    return Collections.unmodifiableMap(attributes);
  }

  public abstract String getId();

  public abstract String getName();

  public abstract String getEmail();

  public abstract String getImageUrl();

  public abstract Map<String, Object> getExtraData();

  public boolean validateNecessaryFields() {
    return StringUtils.hasText(this.getEmail())
        && StringUtils.hasText(this.getId())
        && StringUtils.hasText(this.getName())
        && StringUtils.hasText(this.getImageUrl());
  }
}
