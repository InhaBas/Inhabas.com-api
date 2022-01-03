package com.inhabas.api.service;

import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.MemberRepository;
import com.inhabas.api.service.member.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import java.util.Optional;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @DisplayName("회원가입을 성공한다.")
    @Test
    public void 회원가입() {
        //when
        Optional<Member> joinedMember = memberService.join(MEMBER1);

        //then
        joinedMember.ifPresentOrElse(
                newMember -> assertAll(
                        () -> assertThat(newMember.getIbasInformation().getJoined()).isNotNull(),
                        () -> assertThat(newMember.getName()).isEqualTo(MEMBER1.getName()),
                        () -> assertThat(newMember.getPhone()).isEqualTo(MEMBER1.getPhone()),
                        () -> assertThat(newMember.getSchoolInformation())
                                .usingRecursiveComparison()
                                .isEqualTo(MEMBER1.getSchoolInformation()),
                        () -> assertThat(newMember.getIbasInformation())
                                .usingRecursiveComparison()
                                .ignoringFields("joined")
                                .isEqualTo(MEMBER1.getIbasInformation())
                ),
                () -> { throw new EntityNotFoundException(); }
        );
    }

    @DisplayName("이미 가입된 회원이다.")
    @Test
    public void 중복_회원_예외() {
        //when
        memberService.join(MEMBER1);

        //then에
        assertThrows(EntityExistsException.class,
                () -> memberService.join(MEMBER1));
    }

    @DisplayName("개인정보 수정 성공한다.")
    @Test
    public void 회원_정보_수정() {
        //given
        Member save = memberService.join(MEMBER1).orElse(null);

        //when - 전화번호 수정
        Member param = new Member(
                save.getId(), save.getName(), "010-2222-2222", save.getPicture(),
                save.getSchoolInformation(), save.getIbasInformation());
        Optional<Member> updateMember = memberService.updateMember(param);

        //then
        assertThat(updateMember).hasValue(param);
    }
}
