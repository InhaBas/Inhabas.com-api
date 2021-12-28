package com.inhabas.api.member;

import com.inhabas.api.domain.member.IbasInformation;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.Role;
import com.inhabas.api.repository.member.JpaMemberRepository;
import com.inhabas.api.repository.member.MemberRepository;
import com.inhabas.api.service.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired JpaMemberRepository memberRepository;

    @Test
    public void 회원가입() {
        //given
        Member member = new Member();
        member.setId(12171652);

        //when
        Member joinedMember = memberService.join(member);

        //then
        Member findMember = memberService.findOne(joinedMember.getId());
        assertThat(member).isEqualTo(findMember);
    }

    @Test
    public void 중복_회원_예외() {
        //given
        Member member1 = new Member();
        member1.setId(12171652);

        Member member2 = new Member();
        member2.setId(12171652);

        //when
        memberService.join(member1);

        //then
        assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));
    }

    @Test
    public void 회원_정보_수정() {
        //given
        Member member = new Member();
        member.setId(12171652);
        member.setName("유동현");
        member.setPhone("010-1111-1111");
        member.setIbasInformation(new IbasInformation(Role.values()[0], "hello", 0));
        memberService.join(member);
        memberRepository.detach(member);

        //when
        IbasInformation param = new IbasInformation(Role.values()[1], "not hello", 1);
        member.setIbasInformation(param);     // no dirty check
        member.setPhone("010-2222-2222");
        Member updateMember = memberService.updateMember(member);

        //then
        assertThat(updateMember.getIbasInformation().getRole()).isEqualTo(param.getRole());
        assertThat(updateMember.getIbasInformation().getIntroduce()).isEqualTo(param.getIntroduce());
        assertThat(updateMember.getIbasInformation().getApplyPublish()).isEqualTo(param.getApplyPublish());
        assertThat(updateMember.getPhone()).isEqualTo("010-2222-2222");
    }
}
