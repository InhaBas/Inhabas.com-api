package com.inhabas.api.domain.member.security;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.member.security.MemberPrincipalService;
import com.inhabas.api.auth.domain.oauth2.member.security.socialAccount.MemberSocialAccount;
import com.inhabas.api.auth.domain.oauth2.member.security.socialAccount.MemberSocialAccountRepository;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfoAuthentication;
import com.inhabas.api.auth.domain.token.securityFilter.UserPrincipalNotFoundException;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class MemberPrincipalServiceTest {

    @InjectMocks
    private MemberPrincipalService memberPrincipalService;

    @Mock
    private MemberSocialAccountRepository memberSocialAccountRepository;

//    @Test
//    @DisplayName("기존회원을 uid 와 provider 로 찾는다.")
//    public void findMemberByUidAndProvider() {
//        //given
//        Member member = MemberTest.basicMember1();
//        given(memberSocialAccountRepository.findMemberIdByUidAndProvider(any(), any()))
//                .willReturn(Optional.of(member.getId()));
//
//        //when
//        StudentId StudentId
//                = (StudentId) memberPrincipalService.loadUserPrincipal(new OAuth2UserInfoAuthentication("1234579123", "KAKAO", "my@gmail.com"));
//
//        //then
//        assertThat(StudentId).isEqualTo(member.getId());
//        then(memberSocialAccountRepository).should(times(1)).findMemberIdByUidAndProvider(any(), any());
//        then(memberSocialAccountRepository).should(times(0)).findMemberSocialAccountByEmailAndProvider(any(), any());
//    }

//    @Test
//    @DisplayName("기존회원의 uid 가 없어서 이메일과 provider 로 찾는다.")
//    public void findMemberByEmailAndProvider() {
//        //given
//        Member member = MemberTest.basicMember1();
//        MemberSocialAccount memberSocialAccount =
//                new MemberSocialAccount(member, "my@gmail.com", "1234579123", OAuth2Provider.KAKAO);
//        ReflectionTestUtils.setField(memberSocialAccount, "id", 1);
//        given(memberSocialAccountRepository.findMemberIdByUidAndProvider(any(), any()))
//                .willReturn(Optional.empty());
//        given(memberSocialAccountRepository.findMemberSocialAccountByEmailAndProvider(any(), any()))
//                .willReturn(Optional.of(memberSocialAccount));
//        given(memberSocialAccountRepository.save(any())).willReturn(null);
//
//        //when
//        StudentId StudentId
//                = (StudentId) memberPrincipalService.loadUserPrincipal(new OAuth2UserInfoAuthentication("1234579123", "KAKAO", "my@gmail.com"));
//
//        //then
//        assertThat(StudentId).isEqualTo(member.getId());
//        then(memberSocialAccountRepository).should(times(1)).findMemberIdByUidAndProvider(any(), any());
//        then(memberSocialAccountRepository).should(times(1)).findMemberSocialAccountByEmailAndProvider(any(), any());
//    }
//
//    @Test
//    @DisplayName("기존회원이 아니라서 검색되지 않는다.")
//    public void cannotFindMemberPrincipalTest() {
//        //given
//        given(memberSocialAccountRepository.findMemberIdByUidAndProvider(any(), any()))
//                .willReturn(Optional.empty());
//        given(memberSocialAccountRepository.findMemberSocialAccountByEmailAndProvider(any(), any()))
//                .willReturn(Optional.empty());
//
//        //then
//        Assertions.assertThrows(UserPrincipalNotFoundException.class,
//                () ->memberPrincipalService.loadUserPrincipal(new OAuth2UserInfoAuthentication("1234579123", "KAKAO", "my@gmail.com")));
//    }

}
