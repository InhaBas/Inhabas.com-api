package com.inhabas.api.domain.member;

import com.inhabas.api.domain.member.type.wrapper.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    boolean existsByPhone(Phone phone);
}
