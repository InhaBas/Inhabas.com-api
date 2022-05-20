package com.inhabas.api.auth;

import com.inhabas.api.auth.domain.oauth2.userAuthorityProvider.DefaultUserAuthorityProvider;
import com.inhabas.api.auth.domain.oauth2.userAuthorityProvider.UserAuthorityProvider;
import com.inhabas.api.auth.domain.oauth2.cookie.HttpCookieOAuth2AuthorizationRequestRepository;
import com.inhabas.api.auth.domain.oauth2.handler.Oauth2AuthenticationFailureHandler;
import com.inhabas.api.auth.domain.oauth2.handler.Oauth2AuthenticationSuccessHandler;
import com.inhabas.api.auth.domain.token.TokenProvider;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenProvider;
import com.inhabas.api.auth.domain.token.jwtUtils.refreshToken.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;

@Configuration
@RequiredArgsConstructor
public class AuthBeansConfig {

    private final AuthProperties authProperties;

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    @Bean
    public TokenProvider tokenProvider(RefreshTokenRepository refreshTokenRepository) {
        return new JwtTokenProvider(refreshTokenRepository);
    }

    @Bean
    public Oauth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler(RefreshTokenRepository refreshTokenRepository) {
        return new Oauth2AuthenticationSuccessHandler(
                this.tokenProvider(refreshTokenRepository), this.authProperties, this.httpCookieOAuth2AuthorizationRequestRepository());
    }

    @Bean
    public Oauth2AuthenticationFailureHandler oauth2AuthenticationFailureHandler() {
        return new Oauth2AuthenticationFailureHandler(
                this.authProperties, this.httpCookieOAuth2AuthorizationRequestRepository());
    }

    @Bean
    @ConditionalOnMissingBean
    public UserAuthorityProvider userAuthorityService() {
        return new DefaultUserAuthorityProvider();
    }

    @Bean
    public OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository() {
        return new HttpSessionOAuth2AuthorizedClientRepository();
    }
}
