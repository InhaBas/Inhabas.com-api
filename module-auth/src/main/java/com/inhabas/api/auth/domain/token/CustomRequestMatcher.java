package com.inhabas.api.auth.domain.token;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/** 기존 matches와 달리 request와 일치하지 않으면 True 반환 */
public class CustomRequestMatcher implements RequestMatcher {

  private final RequestMatcher requestMatcher;

  public CustomRequestMatcher(String path, String httpMethod) {
    this.requestMatcher =
        PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.valueOf(httpMethod), path);
  }

  @Override
  public boolean matches(HttpServletRequest request) {
    return !requestMatcher.matches(request);
  }
}
