package com.inhabas.api.service.member;

import com.inhabas.api.domain.member.*;
import com.inhabas.api.domain.member.type.wrapper.Phone;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.dto.signUp.SignUpDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberDuplicationChecker duplicationChecker;

    @Override
    @Transactional
    public Member saveSignUpForm(SignUpDto signUpForm) {
        IbasInformation ibasInformation = new IbasInformation(Role.ANONYMOUS, "", 0);
        SchoolInformation schoolInformation = SchoolInformation.ofUnderGraduate(signUpForm.getMajor(), 1);

        Member member = createMember(ibasInformation, schoolInformation, signUpForm.getMemberId(), signUpForm.getName(), signUpForm.getPhoneNumber());
        if (duplicationChecker.isDuplicatedMember(member)) {
            throw new DuplicatedMemberFieldException("학번 또는 전화번호");
        }

        return memberRepository.save(member);
    }

    private Member createMember(IbasInformation ibasInformation, SchoolInformation schoolInformation, Integer memberId, String name, String phoneNumber) {
        return Member.builder()
                .id(memberId)
                .name(name)
                .phone(phoneNumber)
                .ibasInformation(ibasInformation)
                .schoolInformation(schoolInformation)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public SignUpDto loadSignUpForm(Integer memberId, String email) {

        if (Objects.isNull(memberId)) {
            return SignUpDto.builder()
                    .memberId(null)
                    .phoneNumber(null)
                    .email(email)
                    .name(null)
                    .major(null)
                    .build();
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotExistException::new);

        return SignUpDto.builder()
                        .memberId(memberId)
                        .phoneNumber(member.getPhone())
                        .email(email)
                        .name(member.getName())
                        .major(member.getSchoolInformation().getMajor())
                        .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Member findById(Integer id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotExistException::new);
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
    public void changeRole(Integer memberId, Role role) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotExistException::new);

        member.setRole(role);
        memberRepository.save(member);
    }
}
