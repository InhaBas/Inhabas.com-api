package com.inhabas.api.domain;

import com.inhabas.api.domain.member.type.IbasInformation;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.type.SchoolInformation;
import com.inhabas.api.domain.member.MemberRepository;
import com.inhabas.api.domain.member.type.wrapper.Phone;
import com.inhabas.api.domain.member.type.wrapper.Role;

import com.inhabas.api.dto.signUp.MemberDuplicationQueryCondition;
import com.inhabas.api.service.signup.NoQueryParameterException;
import com.inhabas.testConfig.DefaultDataJpaTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.List;
import java.util.Optional;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static com.inhabas.api.domain.MemberTest.MEMBER2;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        Member param = new Member(MEMBER1.getId(), "유동현", "010-1111-2222", "", SchoolInformation.ofUnderGraduate("건축공학과", 2), member.getIbasInformation());
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
                .ibasInformation(new IbasInformation(Role.BASIC_MEMBER))
                .schoolInformation(SchoolInformation.ofUnderGraduate("전자공학과", 3))
                .build();

        //then
        assertThrows(DataIntegrityViolationException.class,
                () -> memberRepository.saveAndFlush(samePhoneMember));
    }

    @DisplayName("전화번호 중복검사 시 true 를 반환")
    @Test
    public void 전화번호가_존재한다() {
        //given
        Member member = Member.builder()
                .id(12171652)
                .phone("010-0000-0000")
                .name("유동현")
                .picture("")
                .schoolInformation(SchoolInformation.ofUnderGraduate("공간정보공학과", 1))
                .ibasInformation(new IbasInformation(Role.ANONYMOUS))
                .build();
        memberRepository.save(member);

        //when
        boolean isExist = memberRepository.existsByPhone(new Phone("010-0000-0000"));

        //then
        assertTrue(isExist);
    }

    @DisplayName("전화번호 중복검사 시 false 를 반환")
    @Test
    public void 전화번호가_존재하지_않는다() {
        //when
        boolean isExist = memberRepository.existsByPhone(new Phone("010-0000-0000"));

        //then
        assertFalse(isExist);
    }

    @DisplayName("전화번호 중복검사")
    @Test
    public void validatePhoneNumber() {
        //when
        boolean result = memberRepository.isDuplicated(new MemberDuplicationQueryCondition(null, "010-1111-1111"));

        //then
        assertFalse(result);
    }

    @DisplayName("회원 id 중복검사")
    @Test
    public void validateMemberId() {
        //when
        boolean result = memberRepository.isDuplicated(new MemberDuplicationQueryCondition(12171652, null));

        //then
        assertFalse(result);
    }

    @DisplayName("중복 검사 쿼리 아무것도 없는 경우")
    @Test
    public void validateNoneFields() {
        //given
        memberRepository.save(MEMBER1);

        //then
        InvalidDataAccessApiUsageException e = assertThrows(InvalidDataAccessApiUsageException.class,
                () -> memberRepository.isDuplicated(new MemberDuplicationQueryCondition(null, null)));

        assertThat(e.getCause().getClass()).isEqualTo(NoQueryParameterException.class);
    }

    @DisplayName("모든 필드 중 학번이 중복되는 경우")
    @Test
    public void validateAllFieldsOnlyDuplicatedId() {
        //given
        memberRepository.save(MEMBER1);

        //when
        boolean result = memberRepository.isDuplicated(new MemberDuplicationQueryCondition(12171234, "010-1111-1234"));

        //then
        assertTrue(result);
    }

    @DisplayName("모든 필드 중 전화번호가 중복되는 경우")
    @Test
    public void validateAllFieldsOnlyDuplicatedPhoneNumber() {
        //given
        memberRepository.save(MEMBER1);

        //when
        boolean result = memberRepository.isDuplicated(new MemberDuplicationQueryCondition(12171111, "010-1111-1111"));

        //then
        assertTrue(result);
    }

    @DisplayName("모든 필드 중복되는 경우")
    @Test
    public void validateAllFields() {
        //given
        memberRepository.save(MEMBER1);

        //when
        boolean result = memberRepository.isDuplicated(new MemberDuplicationQueryCondition(12171234, "010-1111-1111"));

        //then
        assertTrue(result);
    }






}
