package com.inhabas.api.annotataion;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static java.lang.Integer.valueOf;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomOAuth2AccountSecurityContextFactory.class)
public @interface WithMockCustomOAuth2Account {

    String username() default "username";

    String role() default "USER";

    String email() default "my@default.email";

    String picture() default "https://get_my_picture.com";

    String registrationId() default "google";

    int authUserId() default 1;

    boolean alreadyJoined() default true;
}
