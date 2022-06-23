package com.inhabas.api.domain.member;

import com.inhabas.api.domain.member.type.wrapper.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, MemberId>, MemberRepositoryCustom {
    boolean existsByPhone(Phone phone);

    boolean existsByPhoneOrId(Phone phone, MemberId id);
}
