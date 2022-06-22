package com.inhabas.api.domain;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.security.socialAccount.MemberSocialAccount;
import com.inhabas.api.domain.member.security.socialAccount.MemberSocialAccountRepository;
import com.inhabas.testConfig.DefaultDataJpaTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DefaultDataJpaTest
public class MemberSocialAccountRepositoryTest {

    @Autowired
    private MemberSocialAccountRepository memberSocialAccountRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("소셜계정으로 회원의 학번을 가져온다.")
    @Test
    public void getMemberIdBySocialAccount() {
        //given
        Member member = em.persist(MEMBER1());
        memberSocialAccountRepository.save(new MemberSocialAccount(member, "my@gmail.com", "1234", OAuth2Provider.GOOGLE));

        //when
        Optional<Integer> id = memberSocialAccountRepository.findMemberIdByUidAndProvider(new UID("1234"), OAuth2Provider.GOOGLE);

        //then
        assertTrue(id.isPresent());
        assertThat(id.get()).isEqualTo(member.getId());
    }
}
