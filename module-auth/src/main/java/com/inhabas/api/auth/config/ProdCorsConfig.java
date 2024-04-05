package com.inhabas.api.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Profile("production")
public class ProdCorsConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping("/**")
        .allowedOrigins("https://inhabas.com", "https://www.inhabas.com")
        .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE")
        .allowedHeaders("*")
        .maxAge(3600);
  }
}
