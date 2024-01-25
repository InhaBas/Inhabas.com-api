package com.inhabas.api.web.argumentResolver;

import com.inhabas.api.auth.domain.error.authException.InvalidAuthorityException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authenticated.class);
    }

    /**
     * SecurityContextHolder에서 받아온 authentication을 기준으로 memberId를 찾아 반환한다.
     * @param parameter @Authenticated 어노테이션에 의해 받아온 파라미터.
     * @return Long memberId / null일 경우 null을 반환할 수 있음.
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getPrincipal());
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ANONYMOUS")))
            throw new InvalidAuthorityException();  // login not processed, anonymous user!
        return (Long) authentication.getPrincipal();

    }

}
