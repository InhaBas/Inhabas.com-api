package com.inhabas.api.auth.domain.oauth2.socialAccount.repository;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Email;
import com.inhabas.api.auth.domain.oauth2.socialAccount.domain.entity.MemberSocialAccount;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberSocialAccountRepository
    extends JpaRepository<MemberSocialAccount, Long>, MemberSocialAccountRepositoryCustom {

  Optional<MemberSocialAccount> findMemberSocialAccountByEmailAndProvider(
      Email email, OAuth2Provider provider);

  boolean existsByMember_Id(Long memberId);
}
