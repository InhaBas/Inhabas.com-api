package com.inhabas.api.auth.domain.oauth2.member.repository;

import static com.inhabas.api.auth.domain.oauth2.OAuth2Provider.GOOGLE;
import static com.inhabas.api.auth.domain.oauth2.member.domain.entity.MemberTest.*;
import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.NOT_APPROVED;
import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.SIGNING_UP;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.dto.MemberDuplicationQueryCondition;
import com.inhabas.api.auth.domain.oauth2.socialAccount.domain.valueObject.UID;
import com.inhabas.api.auth.domain.oauth2.userInfo.GoogleOAuth2UserInfo;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import com.inhabas.api.auth.testAnnotation.DefaultDataJpaTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DefaultDataJpaTest
public class MemberRepositoryTest {

  @Autowired MemberRepository memberRepository;
  @Autowired TestEntityManager em;

  @DisplayName("저장 후 반환 값은 처음과 같다.")
  @Test
  @Transactional
  public void save() {

    // when
    Member saveMember = memberRepository.save(signingUpMember1());

    // then
    assertThat(saveMember)
        .usingRecursiveComparison()
        .ignoringFields("ibasInformation.dateJoined", "id", "lastLogin")
        .isEqualTo(signingUpMember1());
  }

  @DisplayName("idList 로 사용자를 찾을 수 있다.")
  @Test
  @Transactional
  public void find_all_by_id() {

    // given
    memberRepository.save(signingUpMember1());
    memberRepository.save(signingUpMember2());
    List<Member> allMember = memberRepository.findAll();
    List<Long> allId = allMember.stream().map(Member::getId).collect(Collectors.toList());

    // when
    List<Member> memberList = memberRepository.findAllById(allId);

    // then
    assertThat(memberList).containsExactlyInAnyOrderElementsOf(allMember);
  }

  @DisplayName("Role로 하나의 사용자를 조회한다.")
  @Test
  @Transactional
  public void findAll() {

    // given
    Member save1 = memberRepository.save(signingUpMember1());

    // when
    Member member = memberRepository.findByIbasInformation_Role(SIGNING_UP);

    // then
    assertThat(member).isEqualTo(save1);
  }

  @DisplayName("같은 provider_uid 저장 시 DataIntegrityViolationException 예외")
  @Test
  @Transactional
  public void 같은_provider_uid_저장_예외() {
    // given
    memberRepository.save(signingUpMember1());

    // then
    assertThrows(
        DataIntegrityViolationException.class,
        () -> memberRepository.saveAndFlush(signingUpMember1()));
  }

  @DisplayName("provider_uid 중복검사 시 true 를 반환")
  @Test
  @Transactional
  public void provider_uid_존재한다() {

    // given
    Map<String, Object> attributes =
        new HashMap<>() {
          {
            put("provider", "GOOGLE");
            put("sub", "1249846925629348");
            put("picture", "/static/image.jpg");
            put("email", "my@gmail.com");
            put("name", "유동현");
            put("locale", "ko");
          }
        };
    OAuth2UserInfo user = new GoogleOAuth2UserInfo(attributes);
    Member save1 = new Member(user);
    memberRepository.save(save1);

    // when
    boolean isExist = memberRepository.existsByProviderAndUid(GOOGLE, new UID("1249846925629348"));

    // then
    assertTrue(isExist);
  }

  @DisplayName("provider_uid 중복검사 시 false 를 반환")
  @Test
  @Transactional
  public void provider_uid_존재하지_않는다() {

    // when
    boolean isExist = memberRepository.existsByProviderAndUid(GOOGLE, new UID("1249846925629348"));

    // then
    assertFalse(isExist);
  }

  @DisplayName("provider_uid 로 사용자를 찾는다")
  @Test
  @Transactional
  public void find_by_provider_uid() {

    // given
    Member save1 = memberRepository.save(signingUpMember1());

    // when
    Optional<Member> member =
        memberRepository.findByProviderAndUid(GOOGLE, new UID("1249846925629348"));

    // then
    assertTrue(member.isPresent());
    assertEquals(save1.getId(), member.get().getId());
  }

  // Custom
  @DisplayName("중복 검사 쿼리 provider 없는 경우")
  @Test
  @Transactional
  public void validateNoneFields() {

    // given
    memberRepository.save(signingUpMember1());

    // then
    assertThrows(
        InvalidInputException.class,
        () ->
            memberRepository.isDuplicated(
                new MemberDuplicationQueryCondition(null, "1249846925629348")));
  }

  @DisplayName("모든 필드 중복되는 경우")
  @Test
  @Transactional
  public void validateAllFields() {

    // given
    memberRepository.save(signingUpMember1());

    // when
    boolean result =
        memberRepository.isDuplicated(
            new MemberDuplicationQueryCondition(GOOGLE, "1249846925629348"));

    // then
    assertTrue(result);
  }

  // 회원가입 이후 구현
  @DisplayName("Role, StudentId 로 회원 검색")
  @Test
  @Transactional
  public void search_by_role_studentId() {
    // given
    memberRepository.save(notapprovedMember());

    // when
    List<Member> members = memberRepository.findAllByRoleAndStudentIdLike(NOT_APPROVED, "12171707");

    // then
    assertThat(members.size()).isEqualTo(1);
  }
}
