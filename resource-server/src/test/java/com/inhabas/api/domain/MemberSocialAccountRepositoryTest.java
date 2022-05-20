package com.inhabas.api.domain;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.socialAccount.NoSuchSocialAccountException;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.inhabas.api.domain.member.*;
import com.inhabas.testConfig.DefaultDataJpaTest;
import org.hibernate.Hibernate;
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


    @DisplayName("소셜계정으로 회원정보를 가져온다. (Team 정보까지)")
    @Test
    public void returnWithMappingObject() {
        //given
        Team IT = em.persist(new Team("IT 부서"));
        Team EXEC = em.persist(new Team("운영"));
        Member member = em.persist(MEMBER1);
        em.persist(new MemberTeam(member, IT));
        em.persist(new MemberTeam(member, EXEC));
        memberSocialAccountRepository.save(new MemberSocialAccount(member, "my@gmail.com", "1234", OAuth2Provider.GOOGLE));

        //when
        MemberSocialAccount memberSocialAccount = memberSocialAccountRepository.findByUidAndProviderWithRoleAndTeam(new UID("1234"), OAuth2Provider.GOOGLE)
                .orElseThrow(NoSuchSocialAccountException::new);

        //then
        assertTrue(Hibernate.isInitialized(memberSocialAccount.getMember()));
        assertTrue(Hibernate.isInitialized(memberSocialAccount.getMember().getTeamList()));
        memberSocialAccount.getMember().getTeamList()
                .forEach(team -> Hibernate.isInitialized(team.getName()));

    }

    @DisplayName("소셜계정으로 회원의 학번을 가져온다.")
    @Test
    public void getMemberIdBySocialAccount() {
        //given
        Member member = em.persist(MEMBER1);
        memberSocialAccountRepository.save(new MemberSocialAccount(member, "my@gmail.com", "1234", OAuth2Provider.GOOGLE));

        //when
        Optional<Integer> id = memberSocialAccountRepository.findMemberIdByUidAndProvider(new UID("1234"), OAuth2Provider.GOOGLE);

        //then
        assertTrue(id.isPresent());
        assertThat(id.get()).isEqualTo(MEMBER1.getId());
    }
}
