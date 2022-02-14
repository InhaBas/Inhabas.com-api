package com.inhabas.api.annotataion;

import com.inhabas.api.domain.member.IbasInformation;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.SchoolInformation;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.security.domain.AuthUser;
import com.inhabas.api.security.jwtUtils.JwtAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

public class WithMockJwtAuthenticationTokenSecurityContextFactory
        implements WithSecurityContextFactory<WithMockJwtAuthenticationToken> {

    @Override
    public SecurityContext createSecurityContext(WithMockJwtAuthenticationToken principalInfo) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        Member profile = null;
        String role = principalInfo.memberRole().toString(); // 기본은 익명.
        if (principalInfo.memberId() != 0) { // default 값이 아니면, 회원 프로필이 저장되어 있다고 간주.
            profile = Member.builder()
                    .id(principalInfo.memberId())
                    .picture("")
                    .name(principalInfo.memberName())
                    .phone(principalInfo.memberPhone())
                    .schoolInformation(new SchoolInformation(principalInfo.memberMajor(), principalInfo.memberGrade(), principalInfo.memberSemester()))
                    .ibasInformation(new IbasInformation(principalInfo.memberRole(), "", 0))
                    .build();
        }
        AuthUser authUser = new AuthUser(principalInfo.provider(), principalInfo.Email());

        ReflectionTestUtils.setField(authUser, "id", principalInfo.authUserId());
        ReflectionTestUtils.setField(authUser, "hasJoined", principalInfo.joined());
        ReflectionTestUtils.setField(authUser, "profile", profile);

        JwtAuthenticationToken token = new JwtAuthenticationToken(authUser, Collections.singleton(new SimpleGrantedAuthority(role)));
        token.setAuthenticated(true);

        context.setAuthentication(token);
        return context;
    }
}
