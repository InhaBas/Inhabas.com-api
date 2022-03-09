package com.inhabas.api.security.oauth2;
import com.inhabas.api.security.domain.AuthUserDetail;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User extends DefaultOAuth2User {

    private final AuthUserDetail authUserDetail;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, AuthUserDetail authUserDetail) {
        super(authorities, attributes, nameAttributeKey);
        this.authUserDetail = authUserDetail;
    }

    public AuthUserDetail getAuthUserDetail() {
        return authUserDetail;
    }
}
