package com.inhabas.api.config;

import com.inhabas.api.controller.interceptor.SignUpControllerInterceptor;
import com.inhabas.api.domain.signup.SignUpAvailabilityChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final SignUpAvailabilityChecker signUpAvailabilityChecker;

    @Bean
    SignUpControllerInterceptor signUpControllerInterceptor() {
        return new SignUpControllerInterceptor(signUpAvailabilityChecker);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(signUpControllerInterceptor())
                .addPathPatterns("/signUp/**").excludePathPatterns("/signUp/schedule");

        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
