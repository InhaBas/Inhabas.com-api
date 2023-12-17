package com.inhabas.testAnnotataion;

import com.inhabas.api.auth.domain.token.jwtUtils.JwtAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

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

        Long memberId = principalInfo.memberId();
        String role = principalInfo.memberRole().toString(); // 기본은 BASIC.
        List<? extends GrantedAuthority> grantedAuthorities = List.of(new SimpleGrantedAuthority(role));

        JwtAuthenticationToken authentication = JwtAuthenticationToken.of(memberId, "TEST", grantedAuthorities);

        context.setAuthentication(authentication);
        return context;

    }
}
