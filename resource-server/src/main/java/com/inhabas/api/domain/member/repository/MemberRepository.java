package com.inhabas.api.domain.member.repository;

import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, MemberId>, MemberRepositoryCustom {
    boolean existsByPhone(Phone phone);

    boolean existsByPhoneOrId(Phone phone, MemberId id);
}
