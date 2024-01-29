package com.inhabas.api.web.interceptor;

import com.inhabas.api.domain.signUpSchedule.usecase.SignUpAvailabilityChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

  private final SignUpAvailabilityChecker signUpAvailabilityChecker;
  private static final String[] WHITELIST_SIGNUP_SCHEDULE = {
    "/signUp/schedule", "/signUp/check", "/signUp/questionnaires", "/signUp/majorInfo"
  };

  @Bean
  SignUpControllerInterceptor signUpControllerInterceptor() {
    return new SignUpControllerInterceptor(signUpAvailabilityChecker);
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry
        .addInterceptor(signUpControllerInterceptor())
        .addPathPatterns("/signUp/**")
        .excludePathPatterns(WHITELIST_SIGNUP_SCHEDULE);

    WebMvcConfigurer.super.addInterceptors(registry);
  }
}
