package com.inhabas.api.auth.domain.oauth2.member.domain.service;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.DuplicatedMemberFieldException;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Name;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.member.dto.NewMemberManagementDto;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;



@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private static final Role DEFAULT_ROLE_AFTER_FINISH_SIGNUP = Role.NOT_APPROVED;

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
    public Member findById(StudentId studentId) {
        return memberRepository.findByStudentId(studentId)
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
    @Transactional
    public void finishSignUp(Member member) {
        member.finishSignUp();
        this.changeRole(member, DEFAULT_ROLE_AFTER_FINISH_SIGNUP);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewMemberManagementDto> getUnapprovedMembers(String search) {
        final List<Member> members = StringUtils.isNumeric(search)
                ? memberRepository.findByRoleAndIdLike(Role.NOT_APPROVED, new StudentId(Integer.parseInt(search)))
                : memberRepository.findByRoleAndNameLike(Role.NOT_APPROVED, new Name(search));

        final List<NewMemberManagementDto> newMemberManagementDtos = members.stream()
                .map(member -> new NewMemberManagementDto(
                member.getName(),
                member.getStudentId().getValue(),
                member.getPhone(),
                member.getEmail(),
                member.getSchoolInformation().getMajor()))
                .collect(Collectors.toList());

        return newMemberManagementDtos;
    }

    @Override
    @Transactional
    public void UpgradeUnapprovedMembers(List<Integer> memberIdList) {
        List<Member> members = memberRepository.findByStudentIdIdIn(memberIdList);
        boolean allNewMembers = members.stream().allMatch(
                member -> DEFAULT_ROLE_AFTER_FINISH_SIGNUP.equals(member.getRole()));

        if (!allNewMembers) {
            throw new IllegalArgumentException();
        }

        for (Member member : members) {
            member.setRole(Role.DEACTIVATED);
        }

        memberRepository.saveAll(members);
    }

    @Override
    public void updateSocialAccountInfo(OAuth2UserInfo oAuth2UserInfo) {

        Member member = memberRepository.
                findByUidAndProvider(new UID(oAuth2UserInfo.getId()), oAuth2UserInfo.getProvider())
                .orElse(new Member(oAuth2UserInfo))
                .setLastLoginTime(LocalDateTime.now());

        memberRepository.save(member);
    }

//    @Override
//    public List<ApprovedMemberManagementDto> getApprovedMembers(String search) {
//        final List<Member> members = isNumeric(search)
//                ? memberRepository.findByRoleAndIdLike(NOT_APPROVED, new StudentId(Integer.parseInt(search)))
//                : memberRepository.findByRoleAndNameLike(NOT_APPROVED, new Name(search));
//
//        final List<NewMemberManagementDto> newMemberManagementDtos = members.stream()
//                .map(member -> new NewMemberManagementDto(
//                        member.getName(),
//                        member.getId().getValue(),
//                        member.getPhone(),
//                        member.getEmail(),
//                        member.getSchoolInformation().getMajor()))
//                .collect(Collectors.toList());
//
//        return newMemberManagementDtos;
//    }
}
