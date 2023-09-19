package com.inhabas.api.domain.member.domain;

import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.domain.valueObject.Role;
import com.inhabas.api.domain.member.dto.ContactDto;
import com.inhabas.api.domain.member.dto.NewMemberManagementDto;
import com.inhabas.api.domain.member.dto.PagedResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    void save(Member member);

    Member findById(MemberId memberId);

    Optional<Member> updateMember(Member member);

    void changeRole(Member member, Role role);

    ContactDto getChiefContact();

    void finishSignUp(Member member);

    Page<NewMemberManagementDto> getUnapprovedMembers(Pageable pageable, String search);

    PagedResponseDto getPagedResponse(Page<?> data);
}
