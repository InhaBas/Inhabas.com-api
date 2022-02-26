package com.inhabas.api.security.argumentResolver;

import com.inhabas.api.security.domain.AuthUserDetail;
import com.inhabas.api.security.jwtUtils.JwtAuthenticationToken;
import com.inhabas.api.security.oauth2.CustomOAuth2User;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {

    AuthUserDetail authenticatedUser = null;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Authenticated.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        authenticatedUser = getAuthUserDetail(authentication);


        if (parameter.getParameterType().equals(Integer.class)) {
            try{
                Integer profileId = authenticatedUser.getProfileId();
                if(profileId.toString().isEmpty())
                    throw new InvalidAuthUserException();
                return profileId;
            } catch(InvalidAuthUserException e){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else if(parameter.getParameterType().equals(AuthUserDetail.class)) {
            return authenticatedUser;
        }

        return authenticatedUser;
    }

    private AuthUserDetail getAuthUserDetail(Authentication authentication) {
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
