package com.inhabas.api.web.argumentResolver;

import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoAuthentication;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
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

@Slf4j
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

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

        if (Objects.isNull(authentication)) return null;  // login not processed, anonymous user!

        if (isMemberIdType(parameter))
            return resolveMemberId(authentication);
        else if (isOAuth2UserInfoAuthenticationType(parameter))
            return authentication;
        else
            throw new IllegalArgumentException("지원하지 않는 타입입니다");
    }

    private MemberId resolveMemberId(Authentication authentication) {

        MemberId memberId = null;

        if (isOAuth2UserInfoAuthenticationType(authentication)) { // jwt 토큰 인증 이후
            memberId = (MemberId) authentication.getPrincipal();

        } else if (authentication instanceof OAuth2AuthenticationToken) { // 소셜 로그인 인증 이후
            throw new NotImplementedException("소셜로그인 구현 완료 후에 작업해야됨!");
        }
        else {
            log.warn("{} - cannot resolve authenticated User's Id!", this.getClass());
        }

        return memberId;
    }

    private boolean isMemberIdType(MethodParameter parameter) {

        return parameter.getParameterType().equals(MemberId.class);
    }

    private boolean isOAuth2UserInfoAuthenticationType(MethodParameter parameter) {

        return OAuth2UserInfoAuthentication.class
                .isAssignableFrom(parameter.getParameterType());
    }
    private boolean isOAuth2UserInfoAuthenticationType(Authentication authentication) {

        return OAuth2UserInfoAuthentication.class
                .isAssignableFrom(authentication.getClass());
    }
}
