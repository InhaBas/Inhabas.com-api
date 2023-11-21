package com.inhabas.api.domain.member.repository;

import static com.inhabas.api.domain.member.domain.entity.MemberTest.basicMember1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.member.security.socialAccount.MemberSocialAccount;
import com.inhabas.api.auth.domain.oauth2.member.security.socialAccount.MemberSocialAccountRepository;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DefaultDataJpaTest
public class MemberSocialAccountRepositoryTest {

    @Autowired
    private MemberSocialAccountRepository memberSocialAccountRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("소셜계정으로 회원의 학번을 가져온다.")
    @Test
    public void getStudentIdBySocialAccount() {
        //given
        Member member = em.persist(basicMember1());
        memberSocialAccountRepository.save(new MemberSocialAccount(member, "my@gmail.com", "1234", OAuth2Provider.GOOGLE));

        //when
        Optional<StudentId> id =
                memberSocialAccountRepository.findMemberIdByUidAndProvider(new UID("1234"), OAuth2Provider.GOOGLE);

        //then
        assertTrue(id.isPresent());
        assertThat(id.get()).isEqualTo(member.getId());
    }
}
