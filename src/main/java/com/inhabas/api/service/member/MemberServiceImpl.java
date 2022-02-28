package com.inhabas.api.service.member;

import com.inhabas.api.domain.member.IbasInformation;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.MemberRepository;
import com.inhabas.api.domain.member.SchoolInformation;
import com.inhabas.api.domain.member.type.wrapper.Phone;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.dto.signUp.DetailSignUpDto;
import com.inhabas.api.dto.signUp.ProfessorSignUpDto;
import com.inhabas.api.dto.signUp.StudentSignUpDto;
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

    @Override
    @Transactional
    public Member saveSignUpForm(StudentSignUpDto signUpForm) {
        IbasInformation ibasInformation = new IbasInformation(Role.ANONYMOUS, "", 0);
        SchoolInformation schoolInformation = SchoolInformation.ofStudent(signUpForm.getMajor(), signUpForm.getGrade(), signUpForm.getSemester());

        checkDuplicatedMemberId(signUpForm.getMemberId());
        checkDuplicatedMemberPhoneNumber(signUpForm.getPhoneNumber());

        Member member = createMember(ibasInformation, schoolInformation, signUpForm.getMemberId(), signUpForm.getName(), signUpForm.getPhoneNumber());

        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member saveSignUpForm(ProfessorSignUpDto signUpForm) {
        IbasInformation ibasInformation = new IbasInformation(Role.ANONYMOUS, "", 0);
        SchoolInformation schoolInformation = SchoolInformation.ofProfessor(signUpForm.getMajor());

        checkDuplicatedMemberId(signUpForm.getMemberId());
        checkDuplicatedMemberPhoneNumber(signUpForm.getPhoneNumber());

        Member member = createMember(ibasInformation, schoolInformation, signUpForm.getMemberId(), signUpForm.getName(), signUpForm.getPhoneNumber());

        return memberRepository.save(member);
    }

    private void checkDuplicatedMemberId(Integer memberId) {
        if (memberRepository.existsById(memberId)) {
            throw new DuplicatedMemberFieldException("학번");
        }
    }

    private void checkDuplicatedMemberPhoneNumber(String phoneNumber) {
        if (memberRepository.existsByPhone(new Phone(phoneNumber))) {
            throw new DuplicatedMemberFieldException("전화번호");
        }
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
    public DetailSignUpDto loadSignUpForm(Integer memberId, String email) {

        if (Objects.isNull(memberId)) {
            return DetailSignUpDto.builder()
                    .memberId(null)
                    .phoneNumber(null)
                    .email(email)
                    .name(null)
                    .major(null)
                    .grade(null)
                    .semester(null)
                    .build();
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(MemberNotExistException::new);

        return DetailSignUpDto.builder()
                        .memberId(memberId)
                        .phoneNumber(member.getPhone())
                        .email(email)
                        .name(member.getName())
                        .major(member.getSchoolInformation().getMajor())
                        .grade(member.getSchoolInformation().getGrade())
                        .semester(member.getSchoolInformation().getGen())
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

    @Override
    @Transactional(readOnly = true)
    public boolean isDuplicatedId(Integer memberId) {

        return memberRepository.existsById(memberId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isDuplicatedPhoneNumber(Phone phoneNumber) {
        return memberRepository.existsByPhone(phoneNumber);
    }
}
