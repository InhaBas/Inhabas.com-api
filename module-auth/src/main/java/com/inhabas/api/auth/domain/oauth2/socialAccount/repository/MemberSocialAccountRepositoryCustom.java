package com.inhabas.api.auth.domain.oauth2.socialAccount.repository;

import java.util.Optional;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.socialAccount.domain.valueObject.UID;

public interface MemberSocialAccountRepositoryCustom {

  Optional<Long> findMemberIdByUidAndProvider(UID uid, OAuth2Provider provider);
}
