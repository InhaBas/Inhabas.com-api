package com.inhabas.api.member;

import com.inhabas.api.domain.member.IbasInformation;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.Role;
import com.inhabas.api.domain.member.SchoolInformation;
import com.inhabas.api.domain.member.MemberRepository;
import com.inhabas.api.service.member.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityExistsException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() {
        //given
        Member member = new Member(12171652, "name", "010-0000-0000", null, new SchoolInformation(), new IbasInformation());

        //when
        Member joinedMember = memberService.join(member).get();

        //then
        Member findMember = memberService.findOne(joinedMember.getId()).get();
        assertThat(joinedMember).isEqualTo(findMember);
    }

    @Test
    public void 중복_회원_예외() {
        //given
        Member member1 = new Member(12171652, "name", "010-0000-0000", null, new SchoolInformation(), new IbasInformation());

        Member member2 = new Member(12171652, "name", "010-0000-0000", null, new SchoolInformation(), new IbasInformation());

        //when
        memberService.join(member1);

        //then
        assertThrows(EntityExistsException.class,
                () -> memberService.join(member2));
    }

    @Test
    public void 회원_정보_수정() {
        //given
        Member member = new Member(
                12171652, "유동현", "010-1111-1111", null, null,
                new IbasInformation(Role.values()[0], "hello", 0));
        memberService.join(member);


        //when
        Member param = new Member(
                12171652, "유동현", "010-2222-2222", null, null,
                new IbasInformation(Role.values()[1], "not hello", 1));
        Member updateMember = memberService.updateMember(param).get();

        //then
        assertThat(updateMember)
                .usingRecursiveComparison()
                .isEqualTo(param);
    }
}
