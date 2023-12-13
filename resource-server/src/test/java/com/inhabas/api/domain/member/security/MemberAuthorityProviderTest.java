package com.inhabas.api.domain.member.security;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.member.security.MemberAuthorityProvider;
import com.inhabas.api.auth.domain.oauth2.member.security.MemberPrincipalService;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Optional;

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


    @BeforeEach
    public void setUp() {
        given(oAuth2UserInfo.getEmail()).willReturn("my@gmail.com");
        given(oAuth2UserInfo.getId()).willReturn("12345");
        given(oAuth2UserInfo.getProvider()).willReturn(OAuth2Provider.GOOGLE);
    }


    @Test
    @DisplayName("기존회원의 권한을 들고온다.")
    public void memberLoginTest() {

        given(memberPrincipalService.loadUserPrincipal(any())).willReturn(1L);
        MemberAuthorityProvider.RoleDto roleDto =
                new MemberAuthorityProvider.RoleDto(Role.BASIC);
        given(memberRepository.fetchRoleByStudentId(any())).willReturn(roleDto);

        //when
        Collection<SimpleGrantedAuthority> simpleGrantedAuthorities =
                memberAuthorityProvider.determineAuthorities(oAuth2UserInfo);

        //then
        assertThat(simpleGrantedAuthorities).containsExactly(new SimpleGrantedAuthority("ROLE_BASIC"));

    }

    @Test
    @DisplayName("OAuth 인증을 했지만 회원가입을 완료하지 않았다면 SIGNING_UP 권한 부여")
    public void cannotFindProfileMappedFromSocialAccount() {
        given(memberPrincipalService.loadUserPrincipal(any())).willReturn(null);
        given(memberRepository.findByProviderAndUid(any(), any()))
                .willReturn(Optional.of(MemberTest.signingUpMember1()));

        Collection<SimpleGrantedAuthority> simpleGrantedAuthorities =
                memberAuthorityProvider.determineAuthorities(oAuth2UserInfo);

        //then
        assertThat(simpleGrantedAuthorities).containsExactly(new SimpleGrantedAuthority("ROLE_SIGNING_UP"));
    }
}
