package com.inhabas.api.auth.domain.oauth2.member.repository;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findAllById(Iterable<Long> memberIdList);
    Member getByStudentId(StudentId studentId);
    Member findByIbasInformation_Role(Role role);

    // OAuth
    boolean existsByProviderAndUid(OAuth2Provider provider, UID uid);
    Optional<Member> findByProviderAndUid(OAuth2Provider provider, UID uid);

}
