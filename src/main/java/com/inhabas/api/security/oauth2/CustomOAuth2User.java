package com.inhabas.api.security.oauth2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User extends DefaultOAuth2User {

    private final Integer authUserId;
    private final boolean alreadyJoined;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, Integer authUserId, boolean alreadyJoined) {
        super(authorities, attributes, nameAttributeKey);
        this.authUserId = authUserId;
        this.alreadyJoined = alreadyJoined;
    }

    public Integer getAuthUserId() {
        return authUserId;
    }

    public boolean isAlreadyJoined() {
        return alreadyJoined;
    }
}
