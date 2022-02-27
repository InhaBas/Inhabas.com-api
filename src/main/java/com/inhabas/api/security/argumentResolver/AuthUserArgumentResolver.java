package com.inhabas.api.security.argumentResolver;

import com.inhabas.api.security.domain.AuthUserDetail;
import com.inhabas.api.security.jwtUtils.JwtAuthenticationToken;
import com.inhabas.api.security.oauth2.CustomOAuth2User;
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
import java.util.Optional;


@Component
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authenticated.class);
    }

    /**
     * SecurityContextHolder에서 받아온 authentication을 기준으로 AuthUserDetail을 찾아 반환한다.
     * @param parameter @Authenticated 어노테이션에 의해 받아온 파라미터.
     * @return Integer / AuthUserDetail - 파라미터의 타입에 따라 다른 형태로 반환.
     * memberId에 의해 조회한 profileId가 null일 경우 null을 반환할 수 있음.
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        AuthUserDetail authenticatedUser = null;
        authenticatedUser = getAuthUserDetail(authentication);

        if (parameter.getParameterType().equals(Integer.class)) {
            if(authenticatedUser == null) return null;
            Integer profileId = authenticatedUser.getProfileId();
            if(profileId == null) return null;
            return profileId;
        } else if (parameter.getParameterType().equals(AuthUserDetail.class)) {
            return authenticatedUser;
        }

        return authenticatedUser;
    }

    private AuthUserDetail getAuthUserDetail(Authentication authentication) {
        AuthUserDetail authenticatedUser = null;
        if (Objects.nonNull(authentication)) {
            if (authentication instanceof JwtAuthenticationToken) { // jwt 토큰 인증 이후
                authenticatedUser = (AuthUserDetail) authentication.getPrincipal();

            } else if (authentication instanceof OAuth2AuthenticationToken) { // 소셜 로그인 인증 이후
                authenticatedUser = ((CustomOAuth2User) authentication.getPrincipal()).getAuthUserDetail();
                authenticatedUser.setProfileImageUrl(
                        ((CustomOAuth2User) authentication.getPrincipal()).getAttribute("picture"));
            }
        }
        return authenticatedUser;
    }
}
