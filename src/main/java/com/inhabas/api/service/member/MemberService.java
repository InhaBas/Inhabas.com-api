package com.inhabas.api.service.member;

import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.dto.signUp.DetailSignUpDto;
import com.inhabas.api.dto.signUp.StudentSignUpDto;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    Member saveSignUpForm(StudentSignUpDto signUpForm);

    DetailSignUpDto loadSignUpForm(Integer memberId, String email);

    List<Member> findMembers();

    Optional<Member> findById(Integer memberId);

    Optional<Member> updateMember(Member member);

    void changeRole(Integer memberId, Role role);
}
