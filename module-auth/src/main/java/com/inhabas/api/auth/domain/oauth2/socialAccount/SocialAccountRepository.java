package com.inhabas.api.auth.domain.oauth2.socialAccount;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Integer> {

    Optional<SocialAccount> findByUidAndProvider(UID uid, OAuth2Provider provider);
}
