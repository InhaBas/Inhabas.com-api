package com.inhabas.api.domain;

import com.inhabas.api.domain.member.IbasInformation;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.SchoolInformation;
import com.inhabas.api.domain.member.MemberRepository;
import com.inhabas.api.domain.member.type.wrapper.Role;

import com.inhabas.api.testConfig.DefaultDataJpaTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static com.inhabas.api.domain.MemberTest.MEMBER2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DefaultDataJpaTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TestEntityManager em;

    @DisplayName("저장 후 반환 값은 처음과 같다.")
    @Test
    public void save() {
        //when
        Member saveMember = memberRepository.save(MEMBER1);

        //then
        assertThat(saveMember)
                .usingRecursiveComparison()
                .ignoringFields("ibasInformation.joined")
                .isEqualTo(MEMBER1);
    }

    @DisplayName("학번으로 사용자를 찾을 수 있다.")
    @Test
    public void find_by_id() {
        //given
        Member save1 = memberRepository.save(MEMBER1);
        Member save2 = memberRepository.save(MEMBER2);

        //when
        Optional<Member> find1 = memberRepository.findById(save1.getId());
        Optional<Member> find2 = memberRepository.findById(save2.getId());

        //then
        assertThat(find1).hasValue(save1);
        assertThat(find2).hasValue(save2);
    }

    @DisplayName("모든 데이터를 조회한다.")
    @Test
    public void findAll() {
        //given
        Member save1 = memberRepository.save(MEMBER1);
        Member save2 = memberRepository.save(MEMBER2);

        //when
        List<Member> members = memberRepository.findAll();

        //then
        assertThat(members).contains(save1, save2);
        assertThat(members.size()).isEqualTo(2);
    }

    @DisplayName("사용자의 정보를 갱신할 수 있다.")
    @Test
    public void update() {
        //given
        Member member = memberRepository.save(MEMBER1);

        //when
        Member param = new Member(MEMBER1.getId(), "유동현", "010-1111-2222", "", new SchoolInformation("건축공학과", 2, 2), member.getIbasInformation());
        Member updated = memberRepository.save(param);

        //then
        Member findMember = memberRepository.findById(MEMBER1.getId()).orElse(null);
        assertThat(findMember).isEqualTo(updated);
    }

    @DisplayName("같은 전화번호 저장 시 DataIntegrityViolationException 예외")
    @Test
    public void 같은_전화번호_저장_예외() {
        //given
        memberRepository.save(MEMBER1);

        //when
        Member samePhoneMember = Member.builder()
                .id(99999999)
                .name("홍길동")
                .phone(MEMBER1.getPhone()) // 같은 전화번호
                .picture("")
                .ibasInformation(new IbasInformation(Role.BASIC_MEMBER, "", 0))
                .schoolInformation(new SchoolInformation("전자공학과", 3, 1))
                .build();

        //then
        assertThrows(DataIntegrityViolationException.class,
                () -> memberRepository.saveAndFlush(samePhoneMember));
    }

}
