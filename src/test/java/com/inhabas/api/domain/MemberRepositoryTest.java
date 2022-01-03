package com.inhabas.api.domain;


import com.inhabas.api.domain.member.IbasInformation;
import com.inhabas.api.domain.member.Major;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.SchoolInformation;
import com.inhabas.api.domain.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static com.inhabas.api.domain.MemberTest.MEMBER2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository MemberRepository;

    @DisplayName("저장 후 반환 값은 처음과 같다.")
    @Test
    public void save() {
        //when
        Member saveMember = MemberRepository.save(MEMBER1);

        //then
        assertAll(
                () -> assertThat(saveMember.getId()).isNotNull(),
                () -> assertThat(saveMember.getName()).isEqualTo(MEMBER1.getName()),
                () -> assertThat(saveMember.getPhone()).isEqualTo(MEMBER1.getPhone()),
                () -> assertThat(saveMember.getPicture()).isEqualTo(MEMBER1.getPicture()),
                () -> assertThat(saveMember.getIbasInformation().getJoined()).isNotNull(),
                () -> assertThat(saveMember.getIbasInformation())
                        .usingRecursiveComparison()
                        .ignoringFields("joined")
                        .isEqualTo(MEMBER1.getIbasInformation()),
                () -> assertThat(saveMember.getSchoolInformation())
                        .usingRecursiveComparison()
                        .isEqualTo(MEMBER1.getSchoolInformation())
        );
    }

    @DisplayName("학번으로 사용자를 찾을 수 있다.")
    @Test
    public void find_by_id() {
        //given
        Member save1 = MemberRepository.save(MEMBER1);
        Member save2 = MemberRepository.save(MEMBER2);

        //when
        Optional<Member> find1 = MemberRepository.findById(MEMBER1.getId());
        Optional<Member> find2 = MemberRepository.findById(MEMBER2.getId());

        //then
        assertThat(find1).hasValue(save1);
        assertThat(find2).hasValue(save2);
    }

    @DisplayName("모든 데이터를 조회한다.")
    @Test
    public void findAll() {
        //given
        Member save1 = MemberRepository.save(MEMBER1);
        Member save2 = MemberRepository.save(MEMBER2);

        //when
        List<Member> members = MemberRepository.findAll();

        //then
        assertThat(members).contains(save1, save2);
        assertThat(members.size()).isEqualTo(2);
    }

    @DisplayName("사용자의 정보를 갱신할 수 있다.")
    @Test
    public void update() {
        //given
        Member member = MemberRepository.save(MEMBER1);

        //when
        Member param = new Member(MEMBER1.getId(), "유동현", "010-1111-2222", "", new SchoolInformation(Major.건축공학과, 2, 2), member.getIbasInformation());
        MemberRepository.save(param);

        //then
        Member findMember = MemberRepository.findById(MEMBER1.getId()).orElse(null);
        assertThat(findMember).isEqualTo(param);
    }

}
