package com.inhabas.api.auth.domain.oauth2.socialAccount.repository;

import static com.inhabas.api.auth.domain.oauth2.OAuth2Provider.GOOGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.MemberTest;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Email;
import com.inhabas.api.auth.domain.oauth2.socialAccount.domain.entity.MemberSocialAccount;
import com.inhabas.api.auth.domain.oauth2.socialAccount.domain.valueObject.UID;
import com.inhabas.api.auth.testAnnotation.DefaultDataJpaTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DefaultDataJpaTest
public class MemberSocialAccountRepositoryTest {

  @Autowired private MemberSocialAccountRepository memberSocialAccountRepository;

  @Autowired private TestEntityManager em;

  @DisplayName("소셜계정으로 회원의 학번을 가져온다.")
  @Test
  public void getStudentIdBySocialAccount() {
    // given
    Member member = em.persist(MemberTest.signingUpMember1());
    memberSocialAccountRepository.save(
        new MemberSocialAccount(member, "my@gmail.com", "1234", GOOGLE));

    // when
    Optional<Long> id =
        memberSocialAccountRepository.findMemberIdByUidAndProvider(new UID("1234"), GOOGLE);

    // then
    assertTrue(id.isPresent());
    assertThat(id.get()).isEqualTo(member.getId());
  }

  @DisplayName("memberId로 회원가입이 되어있는지 확인한다.")
  @Test
  public void checkSignUp() {
    // given
    Member member = em.persist(MemberTest.basicMember());
    memberSocialAccountRepository.save(
        new MemberSocialAccount(member, "my@gmail.com", "1234", GOOGLE));
    Optional<MemberSocialAccount> memberSocialAccount =
        memberSocialAccountRepository.findMemberSocialAccountByEmailAndProvider(
            new Email("my@gmail.com"), GOOGLE);

    // when
    boolean check =
        memberSocialAccountRepository.existsByMember_Id(
            memberSocialAccount.get().getMember().getId());

    // then
    assertThat(check).isEqualTo(true);
  }
}
