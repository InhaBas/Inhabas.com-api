package com.inhabas.api.auth.domain.oauth2.member.service;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.member.dto.MemberDuplicationQueryCondition;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.inhabas.api.auth.domain.oauth2.member.domain.entity.MemberTest.signingUpMember1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class MemberDuplicationCheckerTest {

    @InjectMocks
    MemberDuplicationCheckerImpl memberDuplicationChecker;

    @Mock
    MemberRepository memberRepository;

    @DisplayName("회원 필드 전체를 중복검사한 결과 신규회원이다. ")
    @Test
    public void nonDuplicatedMemberTest() {
        //given
        given(memberRepository.existsByProviderAndUid(any(), any())).willReturn(false);

        //when
        Assertions.assertFalse(memberDuplicationChecker.isDuplicatedMember(signingUpMember1()));
        then(memberRepository).should(times(1)).existsByProviderAndUid(any(), any());
    }


    @DisplayName("회원 필드 전체를 중복검사한 결과 중복회원이다.")
    @Test
    public void duplicatedMemberTest() {
        //given
        given(memberRepository.existsByProviderAndUid(any(), any())).willReturn(true);

        //when
        Assertions.assertTrue(memberDuplicationChecker.isDuplicatedMember(signingUpMember1()));
        then(memberRepository).should(times(1)).existsByProviderAndUid(any(), any());
    }


    @DisplayName("db 에 없는 회원 uid 를 중복검사한다.")
    @Test
    public void notDuplicatedUidTest() {
        //given
        given(memberRepository.isDuplicated(any(MemberDuplicationQueryCondition.class))).willReturn(false);

        //when
        Assertions.assertFalse(memberDuplicationChecker.isDuplicatedMember(
                new MemberDuplicationQueryCondition(OAuth2Provider.GOOGLE, "12312312312312")));
        then(memberRepository).should(times(1)).isDuplicated(any(MemberDuplicationQueryCondition.class));
    }

    @DisplayName("db 에 존재하는 uid 를 중복검사한다.")
    @Test
    public void duplicatedUidTest() {
        given(memberRepository.isDuplicated(any(MemberDuplicationQueryCondition.class))).willReturn(true);

        //when
        Assertions.assertTrue(memberDuplicationChecker.isDuplicatedMember(
                new MemberDuplicationQueryCondition(OAuth2Provider.GOOGLE, "12312312312312")));
        then(memberRepository).should(times(1)).isDuplicated(any(MemberDuplicationQueryCondition.class));
    }

}
