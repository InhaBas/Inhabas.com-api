package com.inhabas.testConfig;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.inhabas.api.web.interceptor.InterceptorConfig;

@TestConfiguration
public class InterceptorConfigMockBean {

  @MockBean public InterceptorConfig interceptorConfig;
}
