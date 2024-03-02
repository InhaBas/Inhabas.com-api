package com.inhabas.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.inhabas.api.web.converter.ContestOrderByConverter;
import com.inhabas.api.web.converter.ContestTypeConverter;
import com.inhabas.api.web.converter.MenuIdConverter;
import com.inhabas.api.web.converter.NormalBoardTypeConverter;
import io.swagger.v3.core.jackson.ModelResolver;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  // 커스텀 컨버터를 Spring 변환 서비스에 등록
  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addConverter(new ContestOrderByConverter.StringToOrderByConverter());
    registry.addConverter(new ContestOrderByConverter.OrderByToStringConverter());
    registry.addConverter(new ContestTypeConverter.StringToContestTypeConverter());
    registry.addConverter(new ContestTypeConverter.ContestTypeToStringConverter());
    registry.addConverter(new NormalBoardTypeConverter.NormalTypeToStringConverter());
    registry.addConverter(new NormalBoardTypeConverter.StringToNormalTypeConverter());
    registry.addConverter(new MenuIdConverter.StringToMenuIdConverter());
    registry.addConverter(new MenuIdConverter.MenuIdToStringConverter());
  }

  @Bean
  public ModelResolver modelResolver(ObjectMapper objectMapper) {
    return new ModelResolver(
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE));
  }
}
