package com.inhabas.api.auth.domain.oauth2.member.repository;

import com.inhabas.api.auth.domain.oauth2.OAuth2Provider;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Name;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    boolean existsByPhone(Phone phone);

    boolean existsByPhoneOrId(Phone phone, StudentId id);

    List<Member> findByStudentIdIdIn(List<Integer> memberIdList);

    Member getByStudentId(StudentId studentId);
    Optional<Member> findByStudentId(StudentId studentId);
    Optional<Member> findByNameValueAndEmailValue(String name, String email);
    List<Member> findByNameValueContaining(Name name);

    Optional<Member> findByUidAndProvider(UID uid, OAuth2Provider provider);

}
