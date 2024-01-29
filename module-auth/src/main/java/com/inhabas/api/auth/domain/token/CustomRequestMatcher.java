package com.inhabas.api.auth.domain.token;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/** 기존 matches와 달리 request와 일치하지 않으면 True 반환 */
public class CustomRequestMatcher implements RequestMatcher {

  private final AntPathRequestMatcher antPathRequestMatcher;
  private final String httpMethod;

  public CustomRequestMatcher(String path, String httpMethod) {

    this.httpMethod = httpMethod;
    this.antPathRequestMatcher = new AntPathRequestMatcher(path, httpMethod);
  }

  @Override
  public boolean matches(HttpServletRequest request) {

    String requestMethod = request.getMethod();
    return !(httpMethod.equalsIgnoreCase(requestMethod) && antPathRequestMatcher.matches(request));
  }
}
