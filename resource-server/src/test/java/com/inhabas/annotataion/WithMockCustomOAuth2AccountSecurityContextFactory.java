package com.inhabas.annotataion;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashMap;


/**
 * WithMockCustomOAuth2Account 어노테이션 정보를 기반으로 SecurityContext 를 설정한다.
 * @see WithMockCustomOAuth2Account
 */
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

        throw new NotImplementedException("로그인 다시 구현해야함");
//        CustomOAuth2User principal = new CustomOAuth2User(
//                List.of(new SimpleGrantedAuthority(customOAuth2Account.role())),
//                attributes,
//                "email",
//                AuthUserDetail.builder()
//                        .id(customOAuth2Account.authUserId())
//                        .email(customOAuth2Account.email())
//                        .provider(customOAuth2Account.provider())
//                        .profileId(customOAuth2Account.profileId())
//                        .hasJoined(customOAuth2Account.alreadyJoined())
//                        .isActive(customOAuth2Account.isActive())
//                        .build());
//
//        OAuth2AuthenticationToken token = new OAuth2AuthenticationToken(
//                principal,
//                principal.getAuthorities(),
//                customOAuth2Account.provider());
//
//        context.setAuthentication(token);
//        return context;
    }
}
