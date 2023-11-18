package com.inhabas.api.auth.domain.oauth2.member.domain.service;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.DuplicatedMemberFieldException;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.member.dto.NewMemberManagementDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.OldMemberManagementDto;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private static final Role DEFAULT_ROLE_AFTER_FINISH_SIGNUP = NOT_APPROVED;
    private static final List<Role> OLD_ROLES = Arrays.asList(ADMIN, CHIEF, VICE_CHIEF, EXECUTIVES, SECRETARY, BASIC, DEACTIVATED);
    private static final String PASS_STATE = "pass";
    private static final String FAIL_STATE = "fail";
    private final MemberRepository memberRepository;
    private final MemberDuplicationChecker duplicationChecker;


    @Override
    @Transactional
    public void save(Member member) {

        if (duplicationChecker.isDuplicatedMember(member)) {
            throw new DuplicatedMemberFieldException("provider 와 uid");
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
    public List<NewMemberManagementDto> getNewMembersBySearchAndRole(String search) {
        final List<Member> members = StringUtils.isNumeric(search)
                ? memberRepository.findByRoleAndStudentIdLike(DEFAULT_ROLE_AFTER_FINISH_SIGNUP, search)
                : memberRepository.findByRoleAndNameLike(DEFAULT_ROLE_AFTER_FINISH_SIGNUP, search);

        return members.stream()
                .map(member -> new NewMemberManagementDto(
                member.getName(),
                member.getId(),
                member.getStudentId().getValue(),
                member.getPhone(),
                member.getEmail(),
                member.getSchoolInformation().getMajor()))
                .collect(Collectors.toList());

    }

    @Override
    @Transactional(readOnly = true)
    public List<OldMemberManagementDto> getOldMembersBySearchAndRole(String search) {
        final List<Member> members = StringUtils.isNumeric(search)
                ? memberRepository.findByRolesInAndStudentIdLike(OLD_ROLES, search)
                : memberRepository.findByRolesInAndNameLike(OLD_ROLES, search);

        return members.stream()
                .map(member -> new OldMemberManagementDto(
                        member.getName(),
                        member.getId(),
                        member.getStudentId().getValue(),
                        member.getPhone(),
                        member.getSchoolInformation().getGeneration(),
                        member.getSchoolInformation().getMajor()))
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public void updateUnapprovedMembers(List<Integer> memberIdList, String state) {

        List<Long> memberLongList = memberIdList.stream()
                .map(Long::valueOf)
                .collect(Collectors.toList());

        List<Member> members = memberRepository.findAllById(memberLongList);
        boolean allNewMembers = members.stream().allMatch(
                member -> DEFAULT_ROLE_AFTER_FINISH_SIGNUP.equals(member.getRole()));

        if (!allNewMembers || !(state.equals(PASS_STATE) || state.equals(FAIL_STATE))) {
            throw new IllegalArgumentException();
        }

        if (state.equals("pass")) {

            for (Member member : members)
                member.setRole(DEACTIVATED);

            memberRepository.saveAll(members);

        } else {
            // 이메일 전송 추가 예정
            memberRepository.deleteAll(members);
        }

    }

    @Override
    public void updateApprovedMembers(List<Long> memberIdList, Role role) {

        // 변경 가능한 ROLE 인지 확인
        if (!OLD_ROLES.contains(role)) {
            throw new IllegalArgumentException();
        }

        List<Member> members = memberRepository.findAllById(memberIdList);
        boolean allApprovedMembers = members.stream().allMatch(
                member -> OLD_ROLES.contains(member.getRole()));

        if (!allApprovedMembers) {
            throw new IllegalArgumentException();
        }

        for (Member member : members)
            member.setRole(role);

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

}
