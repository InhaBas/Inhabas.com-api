package com.inhabas.api.auth.domain.oauth2.member.security.masking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomObjectMapper {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();

    SimpleModule module = new SimpleModule();
    module.addSerializer(String.class, new StringPropertyMasker());
    objectMapper.registerModule(module);

    objectMapper.registerModule(new JavaTimeModule());

    return objectMapper;
  }
}
