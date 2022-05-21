package com.inhabas.api.domain.member.security.socialAccount;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSocialAccountRepository
        extends JpaRepository<MemberSocialAccount, Integer>, MemberSocialAccountRepositoryCustom {
}
