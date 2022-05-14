package com.inhabas.api.auth.domain.socialAccount;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.socialAccount.type.UID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Integer> {

    @EntityGraph(attributePaths = "memberSocialAccount")
    Optional<SocialAccount> findWithMemberIdByUidAndProvider(UID uid, OAuth2Provider provider);

    Optional<SocialAccount> findByUidAndProvider(UID uid, OAuth2Provider provider);
}
