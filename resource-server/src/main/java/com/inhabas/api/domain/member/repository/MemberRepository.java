package com.inhabas.api.domain.member.repository;

import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.Name;
import com.inhabas.api.domain.member.domain.valueObject.Phone;
import com.inhabas.api.domain.member.domain.valueObject.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, MemberId>, MemberRepositoryCustom {
    boolean existsByPhone(Phone phone);

    boolean existsByPhoneOrId(Phone phone, MemberId id);

    List<Member> findByIdIdIn(List<Integer> memberIdList);

    List<Member> findByNameValueContaining(Name name);

}
