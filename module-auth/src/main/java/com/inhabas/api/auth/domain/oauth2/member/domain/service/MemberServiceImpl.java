package com.inhabas.api.auth.domain.oauth2.member.domain.service;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.DuplicatedMemberFieldException;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.dto.ApprovedMemberManagementDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ContactDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.NotApprovedMemberManagementDto;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.socialAccount.type.UID;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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
    public List<NotApprovedMemberManagementDto> getNotApprovedMembersBySearchAndRole(String search) {
        final List<Member> members = StringUtils.isNumeric(search)
                ? memberRepository.findAllByRoleAndStudentIdLike(DEFAULT_ROLE_AFTER_FINISH_SIGNUP, search)
                : memberRepository.findAllByRoleAndNameLike(DEFAULT_ROLE_AFTER_FINISH_SIGNUP, search);

        return members.stream()
                .map(member -> new NotApprovedMemberManagementDto(
                member.getName(),
                member.getId(),
                member.getStudentId(),
                member.getPhone(),
                member.getEmail(),
                member.getSchoolInformation().getGrade(),
                member.getSchoolInformation().getMajor()))
                .collect(Collectors.toList());

    }

    @Override
    @Transactional(readOnly = true)
    public List<ApprovedMemberManagementDto> getApprovedMembersBySearchAndRole(String search) {
        final List<Member> members = StringUtils.isNumeric(search)
                ? memberRepository.findAllByRolesInAndStudentIdLike(OLD_ROLES, search)
                : memberRepository.findAllByRolesInAndNameLike(OLD_ROLES, search);

        return members.stream()
                .map(member -> new ApprovedMemberManagementDto(
                        member.getName(),
                        member.getId(),
                        member.getStudentId(),
                        member.getPhone(),
                        member.getRole(),
                        member.getSchoolInformation().getGeneration(),
                        member.getSchoolInformation().getMajor()))
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public void updateUnapprovedMembers(List<Long> memberIdList, String state) {

        List<Member> members = memberRepository.findAllById(memberIdList);
        boolean allNewMembers = members.stream().allMatch(
                member -> DEFAULT_ROLE_AFTER_FINISH_SIGNUP.equals(member.getRole()));

        if (!allNewMembers || !(state.equals(PASS_STATE) || state.equals(FAIL_STATE))) {
            throw new IllegalArgumentException();
        }

        if (state.equals(PASS_STATE)) {
            for (Member member : members)
                member.setRole(DEACTIVATED);

            memberRepository.saveAll(members);

        } else {
            // 이메일 전송 추가 예정
            memberRepository.deleteAll(members);
        }

    }

    @Override
    @Transactional
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
    @Transactional(readOnly = true)
    public ContactDto getChiefContact() {

        Member chief = memberRepository.findByIbasInformation_Role(CHIEF);

        return new ContactDto(chief.getName(), chief.getPhone(), chief.getEmail());

    }

    @Override
    public List<?> getPagedDtoList(Pageable pageable, List<?> dtoList) {

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), dtoList.size());

        // 시작 인덱스가 리스트 크기보다 크거나 같은 경우, 빈 리스트 반환
        if (start >= dtoList.size()) {
            return Collections.emptyList();
        }

        return dtoList.subList(start, end);

    }


    @Override
    @Transactional
    public void updateSocialAccountInfo(OAuth2UserInfo oAuth2UserInfo) {

        Member member = memberRepository.
                findByProviderAndUid(oAuth2UserInfo.getProvider(), new UID(oAuth2UserInfo.getId()))
                .orElse(new Member(oAuth2UserInfo))
                .setLastLoginTime(LocalDateTime.now());

        memberRepository.save(member);
    }

}
