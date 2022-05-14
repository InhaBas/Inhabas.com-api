package com.inhabas.api.auth;

import com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import com.inhabas.api.auth.domain.oauth2.handler.Oauth2AuthenticationFailureHandler;
import com.inhabas.api.auth.domain.oauth2.handler.Oauth2AuthenticationSuccessHandler;
import com.inhabas.api.auth.domain.token.TokenProvider;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AuthBeansConfig {

    private final AuthProperties authProperties;

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public TokenProvider tokenProvider() {
        return new JwtTokenProvider();
    }

    @Bean
    public Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler() {
        return new Oauth2AuthenticationSuccessHandler(
                this.tokenProvider(), this.authProperties, this.httpCookieOAuth2AuthorizationRequestRepository());
    }

    @Bean
    public Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler() {
        return new Oauth2AuthenticationFailureHandler(
                this.authProperties, this.httpCookieOAuth2AuthorizationRequestRepository());
    }
}
