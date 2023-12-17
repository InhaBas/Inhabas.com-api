package com.inhabas.testAnnotataion;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
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

    long memberId() default 1L;
    Role memberRole() default Role.BASIC;

}
