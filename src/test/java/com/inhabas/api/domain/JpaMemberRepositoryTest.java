package com.inhabas.api.domain;


import com.inhabas.api.domain.member.IbasInformation;
import com.inhabas.api.domain.member.Major;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.SchoolInformation;
import com.inhabas.api.domain.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class JpaMemberRepositoryTest {

    @Autowired
    MemberRepository MemberRepository;

    @Test
    public void save_findById() {
        //given
        Member member = new Member(12171123, "유동현", "010-1111-1111", null, null, new IbasInformation());

        //when
        Member saveMember = MemberRepository.save(member);

        //then
        Member findMember = MemberRepository.findById(12171123).get();
        assertThat(findMember).isEqualTo(saveMember);
    }

    @Test
    public void findAll() {
        //given
        Member member1 = new Member(12171123, "유동현", "010-1111-1111", null, null, new IbasInformation());
        Member member2 = new Member(12161234, "유동동", "010-1234-1234", null, null, new IbasInformation());

        //when
        Member save1 = MemberRepository.save(member1);
        Member save2 = MemberRepository.save(member2);

        //then
        List<Member> members = MemberRepository.findAll();
        assertThat(members).contains(save1, save2);
        assertThat(members.size()).isEqualTo(2);
    }

    @Test
    public void update() {
        //given
        Member member = new Member(12171123, "유동현", "010-1111-1111", null, null, new IbasInformation());
        member = MemberRepository.save(member);

        //when
        Member param = new Member(12171123, "유동현", "010-1111-2222", null, new SchoolInformation(Major.건축공학과, 2, 2), member.getIbasInformation());
        MemberRepository.save(param);

        //then
        Member findMember = MemberRepository.findById(12171123).get();
        assertThat(findMember)
                .usingRecursiveComparison()
                .isEqualTo(param);
    }

}
