package com.inhabas.api.domain.member.security.socialAccount;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;

import java.util.Optional;

public interface MemberSocialAccountRepositoryCustom {

    Optional<MemberId> findMemberIdByUidAndProvider(UID uid, OAuth2Provider provider);
}
