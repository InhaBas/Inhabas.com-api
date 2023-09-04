package com.inhabas.testAnnotataion;

import com.inhabas.api.auth.domain.token.TokenAuthenticationResult;
import com.inhabas.api.auth.domain.token.jwtUtils.JwtAuthenticationResult;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;

/**
 * WithMockJwtAuthenticationToken 어노테이션 정보를 기반으로 SecurityContext 를 설정한다. <br>
 * - memberId 가 default 이면 AuthUser 와 매핑되는 Member 는 null 이다.
 * @see WithMockJwtAuthenticationToken
 */
public class WithMockJwtAuthenticationTokenSecurityContextFactory
        implements WithSecurityContextFactory<WithMockJwtAuthenticationToken> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwtAuthenticationToken principalInfo) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        String role = principalInfo.memberRole().toString(); // 기본은 BASIC.
        TokenAuthenticationResult token
                = new JwtAuthenticationResult(principalInfo.uid(), principalInfo.provider(), principalInfo.email(), Collections.singleton(new SimpleGrantedAuthority(role)));
        token.setAuthenticated(true);

        if (principalInfo.memberId() != 0) { // default 값이 아니면, 회원 프로필이 저장되어 있다고 간주.
            MemberId memberId = new MemberId(principalInfo.memberId());
            token.setPrincipal(memberId);
        }

        context.setAuthentication(token);
        return context;
    }
}
