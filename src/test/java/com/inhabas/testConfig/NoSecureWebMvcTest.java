package com.inhabas.testConfig;

import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class) 추가, default security filter 를 사용하지 않음.
 *
 * @see DefaultWebMvcTest
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("test") // for disable cloud config & security filter chain
@WebMvcTest(excludeAutoConfiguration = SecurityAutoConfiguration.class) // disable default spring-security configuration
public @interface NoSecureWebMvcTest {

    @AliasFor(annotation = WebMvcTest.class, attribute = "value")
    Class<?>[] value() default {};

    @AliasFor(annotation = WebMvcTest.class, attribute = "controllers")
    Class<?>[] controllers() default {};
}
