package com.inhabas.api.domain.member.security;

import com.inhabas.api.auth.domain.exception.InvalidUserInfoException;
import com.inhabas.api.auth.domain.exception.UserNotFoundException;
import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.repository.MemberRepository;
import com.inhabas.api.domain.member.domain.valueObject.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberAuthorityProviderTest {

    @InjectMocks
    private MemberAuthorityProvider memberAuthorityProvider;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberPrincipalService memberPrincipalService;

    @Mock
    private OAuth2UserInfo oAuth2UserInfo;

    @Mock
    private Member member;

    @BeforeEach
    public void setUp() {
        given(oAuth2UserInfo.getEmail()).willReturn("my@gmail.com");
        given(oAuth2UserInfo.getId()).willReturn("12345");
        given(oAuth2UserInfo.getProvider()).willReturn(OAuth2Provider.GOOGLE);
    }

    @Test
    @DisplayName("회원가입하지 않은 사용자가 로그인 시도하면 UserNotFoundException 을 발생시킨다.")
    public void NonMemberLoginRaisesExceptionTest() {

        given(memberPrincipalService.loadUserPrincipal(any())).willReturn(null);

        //when
        Assertions.assertThrows(UserNotFoundException.class,
                ()->memberAuthorityProvider.determineAuthorities(oAuth2UserInfo));
    }

    @Test
    @DisplayName("기존회원의 권한을 들고온다.")
    public void memberLoginTest() {

        given(memberPrincipalService.loadUserPrincipal(any())).willReturn(new MemberId(12171652));
        MemberAuthorityProvider.RoleDto roleDto =
                new MemberAuthorityProvider.RoleDto(Role.BASIC);
        given(memberRepository.fetchRoleByMemberId(any())).willReturn(roleDto);

        //when
        Collection<SimpleGrantedAuthority> simpleGrantedAuthorities =
                memberAuthorityProvider.determineAuthorities(oAuth2UserInfo);

        //then
        assertThat(simpleGrantedAuthorities)
                .hasSize(3)
                .extracting("role")
                .contains("ROLE_BASIC", "TEAM_회계", "TEAM_운영");
    }

    @Test
    @DisplayName("회원의 소셜계정 정보는 있지만, 회원프로필이 존재하지 않으면 오류발생")
    public void cannotFindProfileMappedFromSocialAccount() {
        given(memberPrincipalService.loadUserPrincipal(any())).willReturn(new MemberId(12171652));
        given(memberRepository.fetchRoleByMemberId(any()))
                .willReturn(new MemberAuthorityProvider.RoleDto(null));

        //then
        Assertions.assertThrows(InvalidUserInfoException.class,
                () -> memberAuthorityProvider.determineAuthorities(oAuth2UserInfo));
    }
}
