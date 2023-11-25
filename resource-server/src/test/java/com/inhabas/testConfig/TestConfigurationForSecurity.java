package com.inhabas.testConfig;

import com.inhabas.api.auth.domain.token.TokenResolver;
import com.inhabas.api.auth.domain.token.securityFilter.TokenAuthenticationFailureHandler;
import com.inhabas.api.auth.domain.token.securityFilter.UserPrincipalService;
import com.inhabas.api.web.interceptor.InterceptorConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class TestConfigurationForSecurity {

    @MockBean
    InterceptorConfig interceptorConfig;

    @MockBean
    TokenResolver tokenResolver;

    @MockBean
    TokenAuthenticationFailureHandler failureHandler;

    @MockBean
    UserPrincipalService userPrincipalService;

}
