package com.inhabas.api.auth.utils.argumentResolver;

import com.inhabas.api.auth.domain.token.jwtUtils.JwtAuthenticationToken;
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
        if (Objects.isNull(authentication))
            return null;  // login not processed, anonymous user!

        ResolvedAuthenticationResult authenticatedMember = resolveAuthentication(authentication);

        if (parameter.getParameterType().equals(Integer.class)) {

            return authenticatedMember.getMemberId();
        }
        else if (ResolvedAuthenticationResult.class.isAssignableFrom(parameter.getParameterType())) {

            return authenticatedMember;
        }
        else {
            throw new IllegalArgumentException("지원하지 않는 타입입니다");
        }
    }

    private ResolvedAuthenticationResult resolveAuthentication(Authentication authentication) {
        ResolvedAuthenticationResult authenticatedMember = null;

        if (authentication instanceof JwtAuthenticationToken) { // jwt 토큰 인증 이후
            //Integer memberId = (Integer) authentication.getPrincipal();
            throw new NotImplementedException("jwt 인증 수정 후 작업해야함.");

        } else if (authentication instanceof OAuth2AuthenticationToken) { // 소셜 로그인 인증 이후
            throw new NotImplementedException("소셜로그인 구현 완료 후에 작업해야됨!");
        }
        else {
            throw new RuntimeException("cannot resolve login member!");
        }

        //return authenticatedMember;
    }
}
