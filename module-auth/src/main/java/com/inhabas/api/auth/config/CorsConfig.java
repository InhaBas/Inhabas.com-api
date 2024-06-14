package com.inhabas.api.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** 개발 서버에 한해서만 cors 를 적용함. 대신 개발 전용 api 에 대해 높은 수준의 access control 이 보장되어야 함. */
@Configuration
@Profile({"dev1", "dev2"})
public class CorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedOrigins("*")
        .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE")
        .allowedHeaders("*")
        .maxAge(3600);
  }
}
