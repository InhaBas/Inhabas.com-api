package com.inhabas.api.service.member;

import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.type.wrapper.Phone;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.dto.signUp.SignUpDto;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    Member saveSignUpForm(SignUpDto signUpForm);

    SignUpDto loadSignUpForm(Integer memberId, String email);

    List<Member> findMembers();

    Member findById(Integer memberId);

    Optional<Member> updateMember(Member member);

    void changeRole(Integer memberId, Role role);

    boolean isDuplicatedId(Integer memberId);

    boolean isDuplicatedPhoneNumber(Phone phoneNumber);
}
