package com.inhabas.annotataion;

import com.inhabas.api.domain.member.type.wrapper.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Jwt 인증 결과를 securityContext 에 담아두기 위한 test 용 annotation.
 * Jwt 인증 후의 어떤 특정한 상황을 Mocking 하고 싶을 때 사용한다.<br>
 * - memberId 가 default 이면 AuthUser 와 매핑되는 Member 는 null 이다.
 * @see WithMockJwtAuthenticationTokenSecurityContextFactory
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtAuthenticationTokenSecurityContextFactory.class)
public @interface WithMockJwtAuthenticationToken {

    String uid() default "1234";

    String email() default "my@email.com";

    String provider() default "google";

    boolean joined() default false;

    int memberId() default 0; // 다른값으로 설정되지 않으면, authUser 의 member profile 을 null 로 간주.

    Role memberRole() default Role.BASIC_MEMBER;
}
