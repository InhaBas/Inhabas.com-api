package com.inhabas.testAnnotataion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;

import com.inhabas.testConfig.InterceptorConfigMockBean;

/**
 * WebMvcTest(excludeAutoConfiguration = {SecurityAutoConfiguration.class,
 * OAuth2ClientAutoConfiguration.class}) , default security filter 를 사용하지 않음. 테스트 설정 파일에서
 * OAuth2Client 정보를 읽어들이지 않음.
 *
 * @see DefaultWebMvcTest
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ActiveProfiles("no_security_mvc_test") // for disable cloud config & security filter chain
@WebMvcTest(
    excludeAutoConfiguration = {
      SecurityAutoConfiguration.class,
      OAuth2ClientAutoConfiguration.class
    }) // disable default spring-security configuration
@Import(InterceptorConfigMockBean.class)
public @interface NoSecureWebMvcTest {

  @AliasFor(annotation = WebMvcTest.class, attribute = "value")
  Class<?>[] value() default {};

  @AliasFor(annotation = WebMvcTest.class, attribute = "controllers")
  Class<?>[] controllers() default {};
}
