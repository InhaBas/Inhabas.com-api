package com.inhabas.api.auth.domain.oauth2.member.security.socialAccount;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;

import java.util.Optional;

public interface MemberSocialAccountRepositoryCustom {

    Optional<Long> findMemberIdByUidAndProvider(UID uid, OAuth2Provider provider);
}
