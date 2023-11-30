package com.inhabas.api.web.argumentResolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
        return (Long) authentication.getPrincipal();

    }

}
