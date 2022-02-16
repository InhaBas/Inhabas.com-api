package com.inhabas.api.security.argumentResolver;

import com.inhabas.api.security.domain.AuthUserDetail;
import com.inhabas.api.security.domain.AuthUserService;
import com.inhabas.api.security.jwtUtils.JwtAuthenticationToken;
import com.inhabas.api.security.oauth2.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authenticated.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        AuthUserDetail authenticatedUser = null;

        if (Objects.nonNull(authentication)) {

            if (authentication instanceof JwtAuthenticationToken) { // jwt 토큰 인증 이후
                authenticatedUser = (AuthUserDetail) authentication.getPrincipal();

            } else if (authentication instanceof OAuth2AuthenticationToken) { // 소셜 로그인 인증 이후
                authenticatedUser = ((CustomOAuth2User) authentication.getPrincipal()).getAuthUserDetail();
            }
        }

        return authenticatedUser;
    }
}
