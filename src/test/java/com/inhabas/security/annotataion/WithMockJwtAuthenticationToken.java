package com.inhabas.security.annotataion;

import com.inhabas.api.domain.member.type.wrapper.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockJwtAuthenticationTokenSecurityContextFactory.class)
public @interface WithMockJwtAuthenticationToken {

    int authUserId() default 1;

    String Email() default "my@email.com";

    String provider() default "google";

    boolean joined() default false;

    int memberId() default 0; // 다른값으로 설정되지 않으면, authUser 의 member profile 을 null 로 간주.

    String memberName() default "홍길동";

    int memberGrade() default 1;

    int memberSemester() default 1;

    String memberMajor() default "의예과";

    String memberPhone() default "010-1234-5678";

    Role memberRole() default Role.ANONYMOUS;


}
