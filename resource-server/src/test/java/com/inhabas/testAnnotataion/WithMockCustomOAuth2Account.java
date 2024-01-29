package com.inhabas.testAnnotataion;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

/**
 * OAuth2 인증 결과를 securityContext 에 담아두기 위한 test 용 annotation.<br>
 * OAuth2 인증 후의 어떤 특정한 상황을 Mocking 하고 싶을 때 사용한다.
 *
 * @see WithMockCustomOAuth2AccountSecurityContextFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomOAuth2AccountSecurityContextFactory.class)
public @interface WithMockCustomOAuth2Account {

  String name() default "username";

  String role() default "USER";

  String email() default "my@default.email";

  String picture() default "https://get_my_picture.com";

  String provider() default "google";

  int authUserId() default 1;

  boolean alreadyJoined() default true;

  boolean isActive() default true;

  int profileId() default 0;
}
