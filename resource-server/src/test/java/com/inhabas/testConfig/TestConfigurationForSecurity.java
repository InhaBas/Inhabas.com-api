package com.inhabas.testConfig;

import com.inhabas.api.auth.domain.token.jwtUtils.JwtTokenProvider;
import com.inhabas.api.auth.domain.token.securityFilter.UserPrincipalService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class TestConfigurationForSecurity {
    @MockBean
    JwtTokenProvider tokenProvider;
    @MockBean
    UserPrincipalService userPrincipalService;
}
