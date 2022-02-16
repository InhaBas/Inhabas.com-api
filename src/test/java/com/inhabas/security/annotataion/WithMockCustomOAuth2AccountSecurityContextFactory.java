package com.inhabas.security.annotataion;

import com.inhabas.api.security.oauth2.CustomOAuth2User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashMap;
import java.util.List;

public class WithMockCustomOAuth2AccountSecurityContextFactory
    implements WithSecurityContextFactory<WithMockCustomOAuth2Account> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomOAuth2Account customOAuth2Account) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();

        HashMap<String, Object> attributes = new HashMap<>();
        attributes.put("username", customOAuth2Account.name());
        attributes.put("email", customOAuth2Account.email());
        attributes.put("picture", customOAuth2Account.picture());
        attributes.put("role", customOAuth2Account.role());

        CustomOAuth2User principal = new CustomOAuth2User(
                List.of(new SimpleGrantedAuthority(customOAuth2Account.role())),
                attributes,
                "email",
                customOAuth2Account.authUserId(),
                customOAuth2Account.alreadyJoined());

        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(
                principal,
                principal.getAuthorities(),
                customOAuth2Account.registrationId());

        context.setAuthentication(token);
        return context;
    }
}
