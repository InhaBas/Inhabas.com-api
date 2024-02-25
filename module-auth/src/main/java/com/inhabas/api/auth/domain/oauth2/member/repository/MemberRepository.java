package com.inhabas.api.auth.domain.oauth2.member.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.socialAccount.domain.valueObject.UID;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

  List<Member> findAllById(Iterable<Long> memberIdList);

  Member findByIbasInformation_Role(Role role);

  List<Member> findAllByIbasInformation_IsHOF(boolean IsHOF);

  // OAuth
  boolean existsByProviderAndUid(OAuth2Provider provider, UID uid);

  Optional<Member> findByProviderAndUid(OAuth2Provider provider, UID uid);
}
