package com.inhabas.testConfig;

import com.inhabas.api.auth.domain.token.TokenProvider;
import com.inhabas.api.auth.domain.token.TokenResolver;
import com.inhabas.api.auth.domain.token.securityFilter.TokenAuthenticationFailureHandler;
import com.inhabas.api.auth.domain.token.securityFilter.TokenAuthenticationProcessingFilter;
import com.inhabas.api.auth.domain.token.securityFilter.UserPrincipalService;
import com.inhabas.api.web.interceptor.InterceptorConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfigurationForSecurity {

    @MockBean
    InterceptorConfig interceptorConfig;

    @MockBean
    TokenProvider tokenProvider;

    @MockBean
    TokenResolver tokenResolver;

    @MockBean
    TokenAuthenticationFailureHandler failureHandler;

    @MockBean
    UserPrincipalService userPrincipalService;

    @Bean
    public TokenAuthenticationProcessingFilter tokenAuthenticationProcessingFilter() {
        return new TokenAuthenticationProcessingFilter(tokenProvider, tokenResolver, failureHandler,
                userPrincipalService);
    }
}
