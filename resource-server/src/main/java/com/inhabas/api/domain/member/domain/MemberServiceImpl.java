package com.inhabas.api.domain.member.domain;

import com.inhabas.api.domain.member.*;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.domain.valueObject.Role;
import com.inhabas.api.domain.member.repository.MemberRepository;
import com.inhabas.api.domain.member.dto.ContactDto;
import com.inhabas.api.domain.member.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.inhabas.api.domain.member.domain.valueObject.Role.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private static final Role DEFAULT_ROLE_AFTER_FINISH_SIGNUP = NOT_APPROVED;

    private final MemberRepository memberRepository;
    private final MemberDuplicationChecker duplicationChecker;

    @Override
    @Transactional
    public void save(Member member) {

        if (duplicationChecker.isDuplicatedMember(member)) {
            throw new DuplicatedMemberFieldException("학번 또는 전화번호");
        }

        memberRepository.save(member);
    }


    @Override
    @Transactional(readOnly = true)
    public Member findById(MemberId id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Override
    @Transactional
    public Optional<Member> updateMember(Member member) {
        return DoesExistMember(member) ?
                Optional.of(memberRepository.save(member)) : Optional.empty();
    }

    private boolean DoesExistMember(Member member) {
        return memberRepository.findById(member.getId()).isPresent();
    }

    @Transactional
    public void changeRole(Member member, Role role) {
        member.setRole(role);
        memberRepository.save(member);
    }

    @Override
    @Transactional(readOnly = true)
    public ContactDto getChiefContact() {
        try {
            Member chief = memberRepository.searchByRoleLimit(CHIEF, 1).get(0);
            return new ContactDto(chief.getName(), chief.getPhone(), chief.getEmail());
        } catch (IndexOutOfBoundsException e) {
            return new ContactDto("", "", "");
        }
    }

    @Override
    @Transactional
    public void finishSignUp(Member member) {
        member.finishSignUp();
        this.changeRole(member, DEFAULT_ROLE_AFTER_FINISH_SIGNUP);
    }
}
