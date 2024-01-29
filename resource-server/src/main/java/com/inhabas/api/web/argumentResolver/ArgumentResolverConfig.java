package com.inhabas.api.web.argumentResolver;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ArgumentResolverConfig implements WebMvcConfigurer {

  private final LoginMemberArgumentResolver loginMemberArgumentResolver;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(loginMemberArgumentResolver);
    WebMvcConfigurer.super.addArgumentResolvers(resolvers);
  }
}
