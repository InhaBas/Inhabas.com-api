package com.inhabas.security.annotataion;

import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.type.IbasInformation;
import com.inhabas.api.domain.member.type.SchoolInformation;
import com.inhabas.api.security.utils.jwtUtils.JwtAuthenticationToken;
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

        String role = principalInfo.memberRole().toString(); // 기본은 BASIC_MEMBER.
        if (principalInfo.memberId() != 0) { // default 값이 아니면, 회원 프로필이 저장되어 있다고 간주.

            Member profile = Member.builder()
                    .id(principalInfo.memberId())
                    .picture("")
                    .name(principalInfo.memberName())
                    .email(principalInfo.email())
                    .phone(principalInfo.memberPhone())
                    .schoolInformation(new SchoolInformation(principalInfo.memberMajor(), principalInfo.memberGeneration(), principalInfo.memberType()))
                    .ibasInformation(new IbasInformation(principalInfo.memberRole()))
                    .build();
        }

        JwtAuthenticationToken token
                = new JwtAuthenticationToken(principalInfo.memberId(), Collections.singleton(new SimpleGrantedAuthority(role)));
        token.setAuthenticated(true);

        context.setAuthentication(token);
        return context;
    }
}
