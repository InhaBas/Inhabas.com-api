package com.inhabas.api.member;


import com.inhabas.api.domain.member.IbasInformation;
import com.inhabas.api.domain.member.Major;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.SchoolInformation;
import com.inhabas.api.repository.member.JpaMemberRepository;
import org.assertj.core.api.Assertions;
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
    JpaMemberRepository jpaMemberRepository;

    @Test
    public void save_findById() {
        //given
        Member member = new Member(12171123, "유동현", "010-1111-1111", null, null, new IbasInformation());

        //when
        Member saveMember = jpaMemberRepository.save(member);

        //then
        Member findMember = jpaMemberRepository.findById(12171123);
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void findAll() {
        //given
        Member member1 = new Member(12171123, "유동현", "010-1111-1111", null, null, new IbasInformation());
        Member member2 = new Member(12161234, "유동동", "010-1234-1234", null, null, new IbasInformation());

        //when
        Member save1 = jpaMemberRepository.save(member1);
        Member save2 = jpaMemberRepository.save(member2);

        //then
        List<Member> members = jpaMemberRepository.findAll();
        assertThat(members).contains(save1, save2);
        assertThat(members.size()).isEqualTo(2);
    }

    @Test
    public void update() {
        //given
        Member member = new Member(12171123, "유동현", "010-1111-1111", null, null, new IbasInformation());
        member = jpaMemberRepository.save(member);

        //when
        Member param = new Member(12171123, "유동현", "010-1111-2222", null, new SchoolInformation(Major.건축공학과, 2, 2), member.getIbasInformation());
        jpaMemberRepository.update(param);

        //then
        Member findMember = jpaMemberRepository.findById(12171123);
        assertThat(findMember)
                .usingRecursiveComparison()
                .isEqualTo(param);
    }

}
