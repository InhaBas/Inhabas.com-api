package com.inhabas.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.swagger.v3.core.jackson.ModelResolver;

@Configuration
public class WebConfig {

  @Bean
  public ModelResolver modelResolver(ObjectMapper objectMapper) {
    return new ModelResolver(
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE));
  }
}
