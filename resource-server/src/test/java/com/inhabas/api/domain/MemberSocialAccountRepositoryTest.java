package com.inhabas.api.domain;

import com.inhabas.api.auth.domain.socialAccount.NoSuchSocialAccountException;
import com.inhabas.api.auth.domain.socialAccount.SocialAccount;
import com.inhabas.api.auth.domain.socialAccount.SocialAccountRepository;
import com.inhabas.api.auth.domain.socialAccount.type.Provider;
import com.inhabas.api.auth.domain.socialAccount.type.UID;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.MemberSocialAccount;
import com.inhabas.api.domain.member.MemberSocialAccountRepository;
import com.inhabas.testConfig.DefaultDataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DefaultDataJpaTest
public class MemberSocialAccountRepositoryTest {

    @Autowired
    private MemberSocialAccountRepository memberSocialAccountRepository;
    @Autowired
    private SocialAccountRepository socialAccountRepository;
    @Autowired
    private TestEntityManager em;

    private Member member;
    private SocialAccount socialaccount;
    private MemberSocialAccount mappingObj;

    @BeforeEach
    public void setUp() {
        member = em.persist(MEMBER1);
        socialaccount = socialAccountRepository.save(new SocialAccount(Provider.GOOGLE, new UID("1234"), LocalDateTime.now(), LocalDateTime.now(),""));
        mappingObj = memberSocialAccountRepository.save(new MemberSocialAccount(member, socialaccount));
    }

    @Disabled
    @DisplayName("소셜계정을 찾으면 매핑객체도 join 되서 반환된다.")
    @Test
    public void returnWithMappingObject() {

        SocialAccount socialAccount = socialAccountRepository.findWithMemberIdByUidAndProvider(new UID("1234"), Provider.GOOGLE)
                .orElseThrow(NoSuchSocialAccountException::new);

        //assertTrue(Hibernate.isInitialized(socialAccount.getMemberSocialAccount()));
    }

    @Disabled
    @DisplayName("소셜계정을 삭제하면 매핑객체도 삭제된다.")
    @Test
    public void deleteWithMappingObject() {
        //when
        socialAccountRepository.deleteById(socialaccount.getId());
        em.flush();
        em.clear();

        //then
        assertTrue(memberSocialAccountRepository.findById(mappingObj.getId()).isEmpty());
    }

}
