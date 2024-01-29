package com.inhabas.testConfig;

import com.inhabas.api.web.interceptor.InterceptorConfig;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class InterceptorConfigMockBean {

  @MockBean public InterceptorConfig interceptorConfig;
}
