package com.inhabas.api.auth.domain.oauth2.member.security.socialAccount;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;

import java.util.Optional;

public interface MemberSocialAccountRepositoryCustom {

    Optional<StudentId> findMemberIdByUidAndProvider(UID uid, OAuth2Provider provider);
}
