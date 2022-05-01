package com.inhabas.api.security.domain.socialAccount;

import com.inhabas.api.security.domain.socialAccount.type.Provider;
import com.inhabas.api.security.domain.socialAccount.type.UID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocialAccountRepository extends JpaRepository<SocialAccount, Integer> {

    Optional<SocialAccount> findWithAuthUserByUidAndProvider(UID uid, Provider provider);
}
