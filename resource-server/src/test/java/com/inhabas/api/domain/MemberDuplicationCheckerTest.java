package com.inhabas.api.domain;

import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.MemberDuplicationCheckerImpl;
import com.inhabas.api.domain.member.MemberRepository;
import com.inhabas.api.domain.member.type.wrapper.Phone;
import com.inhabas.api.dto.signUp.MemberDuplicationQueryCondition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
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
        given(memberRepository.existsByPhoneOrId(any(Phone.class), anyInt())).willReturn(false);

        //when
        Assertions.assertFalse(memberDuplicationChecker.isDuplicatedMember(MEMBER1));
        then(memberRepository).should(times(1)).existsByPhoneOrId(any(Phone.class), anyInt());
    }


    @DisplayName("회원 필드 전체를 중복검사한 결과 중복회원이다.")
    @Test
    public void duplicatedMemberTest() {
        //given
        given(memberRepository.existsByPhoneOrId(any(Phone.class), anyInt())).willReturn(true);

        //when
        Assertions.assertTrue(memberDuplicationChecker.isDuplicatedMember(MEMBER1));
        then(memberRepository).should(times(1)).existsByPhoneOrId(any(Phone.class), anyInt());
    }


    @DisplayName("db 에 없는 회원 id 를 중복검사한다.")
    @Test
    public void notDuplicatedIdTest() {
        //given
        given(memberRepository.isDuplicated(any(MemberDuplicationQueryCondition.class))).willReturn(false);

        //when
        Assertions.assertFalse(memberDuplicationChecker.isDuplicatedMember(new MemberDuplicationQueryCondition(12171652, null)));
        then(memberRepository).should(times(1)).isDuplicated(any(MemberDuplicationQueryCondition.class));
    }

    @DisplayName("db 에 존재하는 회원 id 를 중복검사한다.")
    @Test
    public void duplicatedIdTest() {
        given(memberRepository.isDuplicated(any(MemberDuplicationQueryCondition.class))).willReturn(true);

        //when
        Assertions.assertTrue(memberDuplicationChecker.isDuplicatedMember(new MemberDuplicationQueryCondition(12171652, null)));
        then(memberRepository).should(times(1)).isDuplicated(any(MemberDuplicationQueryCondition.class));
    }

    @DisplayName("db 에 존재하지 않는 핸드폰번호를 중복검사한다.")
    @Test
    public void notDuplicatedPhoneNumberTest() {
        given(memberRepository.isDuplicated(any(MemberDuplicationQueryCondition.class))).willReturn(false);

        //when
        Assertions.assertFalse(memberDuplicationChecker.isDuplicatedMember(new MemberDuplicationQueryCondition(null, "010-1111-1111")));
        then(memberRepository).should(times(1)).isDuplicated(any(MemberDuplicationQueryCondition.class));
    }

    @DisplayName("db 에 존재하는 핸드폰번호를 중복검사한다.")
    @Test
    public void duplicatedPhoneNumberTest() {
        given(memberRepository.isDuplicated(any(MemberDuplicationQueryCondition.class))).willReturn(true);

        //when
        Assertions.assertTrue(memberDuplicationChecker.isDuplicatedMember(new MemberDuplicationQueryCondition(null, "010-1111-1111")));
        then(memberRepository).should(times(1)).isDuplicated(any(MemberDuplicationQueryCondition.class));
    }

}