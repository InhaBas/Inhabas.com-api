package com.inhabas.api.security.data;

import com.inhabas.api.security.domain.socialAccount.SocialAccount;
import com.inhabas.api.security.domain.socialAccount.SocialAccountRepository;
import com.inhabas.api.security.domain.socialAccount.type.Provider;
import com.inhabas.api.security.domain.socialAccount.type.UID;
import com.inhabas.testConfig.DefaultDataJpaTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@DefaultDataJpaTest
public class SocialAccountRepositoryTest {

    @Autowired
    private SocialAccountRepository socialAccountRepository;

    @Test
    @DisplayName("소셜 계정을 uid 와 provider 로 조회한다.")
    public void findBySocialAccountByUidAndProvider() {
        //given
        SocialAccount socialAccount =
                new SocialAccount(Provider.GOOGLE, new UID("1234"), LocalDateTime.now(), LocalDateTime.now(), "");
        socialAccountRepository.save(socialAccount);

        //when
        SocialAccount find = socialAccountRepository.findWithAuthUserByUidAndProvider(new UID("1234"), Provider.GOOGLE)
                .orElseThrow(EntityNotFoundException::new);

        //then
        assertThat(find.getUid()).isEqualTo("1234");
        assertThat(find.getProvider()).isEqualTo(Provider.GOOGLE);
    }
}
