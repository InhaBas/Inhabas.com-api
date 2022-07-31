package com.inhabas.api.auth.domain.oauth2.socialaccount;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.socialAccount.SocialAccount;
import com.inhabas.api.auth.domain.oauth2.socialAccount.SocialAccountRepository;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@DataJpaTest
public class SocialAccountRepositoryTest {

    @Autowired
    private SocialAccountRepository socialAccountRepository;

    @Test
    @DisplayName("소셜 계정을 uid 와 provider 로 조회한다.")
    public void findBySocialAccountByUidAndProvider() {
        //given
        SocialAccount socialAccount =
                new SocialAccount(OAuth2Provider.GOOGLE, "1234", LocalDateTime.now(), LocalDateTime.now(), "");
        socialAccountRepository.save(socialAccount);

        //when
        SocialAccount find = socialAccountRepository.findByUidAndProvider(new UID("1234"), OAuth2Provider.GOOGLE)
                .orElseThrow(EntityNotFoundException::new);

        //then
        assertThat(find.getUid()).isEqualTo("1234");
        assertThat(find.getProvider()).isEqualTo(OAuth2Provider.GOOGLE);
    }

    @Test
    @DisplayName("소셜 계정은 uid와 provider 가 unique 한 조합이어야 한다.")
    public void failToSaveTheSameSocialAccount() {
        //given
        SocialAccount socialAccount =
                new SocialAccount(OAuth2Provider.GOOGLE, "1234", LocalDateTime.now(), LocalDateTime.now(), "");
        socialAccountRepository.save(socialAccount);

        //when
        assertThrows(DataIntegrityViolationException.class,
                () -> socialAccountRepository.save(new SocialAccount(OAuth2Provider.GOOGLE, "1234", LocalDateTime.now(), LocalDateTime.now(), "")));
    }
}
