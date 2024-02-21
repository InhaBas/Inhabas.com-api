package com.inhabas.api.domain.member.usecase;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.*;

import java.util.*;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.dto.ApprovedMemberManagementDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ContactDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ExecutiveMemberDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.HallOfFameDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.NotApprovedMemberManagementDto;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.member.service.SMTPService;
import com.inhabas.api.auth.domain.oauth2.socialAccount.repository.MemberSocialAccountRepository;
import com.inhabas.api.domain.signUp.repository.AnswerRepository;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberManageServiceImpl implements MemberManageService {

  private static final Role DEFAULT_ROLE_AFTER_FINISH_SIGNUP = NOT_APPROVED;
  private static final Set<Role> OLD_ROLES =
      Set.of(ADMIN, CHIEF, VICE_CHIEF, EXECUTIVES, SECRETARY, BASIC, DEACTIVATED);
  private static final Set<Role> CHIEF_CHANGEABLE_ROLES =
      Set.of(ADMIN, CHIEF, VICE_CHIEF, EXECUTIVES, SECRETARY, BASIC, DEACTIVATED);
  private static final Set<Role> SECRETARY_CHANGEABLE_ROLES = Set.of(BASIC, DEACTIVATED);
  private static final Set<Role> EXECUTIVE_ROLES =
      Set.of(ADMIN, CHIEF, VICE_CHIEF, EXECUTIVES, SECRETARY);
  private static final String PASS_STATE = "pass";
  private static final String FAIL_STATE = "fail";
  private static final String PASS_EMAIL_SUBJECT = "[IBAS] 축하합니다. 동아리에 입부되셨습니다.";
  private static final String FAIL_EMAIL_SUBJECT = "[IBAS] 입부 신청 결과를 알립니다.";
  private static final String ROLE_PREFIX = "ROLE_";
  private static final String BLANK = "";
  private final MemberRepository memberRepository;
  private final SMTPService amazonSMTPService;
  private final AnswerRepository answerRepository;
  private final MemberSocialAccountRepository memberSocialAccountRepository;

  @Override
  @Transactional(readOnly = true)
  public List<NotApprovedMemberManagementDto> getNotApprovedMembersBySearchAndRole(String search) {
    final List<Member> members =
        StringUtils.isNumeric(search)
            ? memberRepository.findAllByRoleAndStudentIdLike(
                DEFAULT_ROLE_AFTER_FINISH_SIGNUP, search)
            : memberRepository.findAllByRoleAndNameLike(DEFAULT_ROLE_AFTER_FINISH_SIGNUP, search);

    return members.stream()
        .map(
            member ->
                new NotApprovedMemberManagementDto(
                    member.getName(),
                    member.getId(),
                    member.getStudentId(),
                    member.getPhone(),
                    member.getEmail(),
                    member.getSchoolInformation().getMemberType(),
                    member.getSchoolInformation().getGrade(),
                    member.getSchoolInformation().getMajor()))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<ApprovedMemberManagementDto> getApprovedMembersBySearchAndRole(String search) {
    final List<Member> members =
        StringUtils.isNumeric(search)
            ? memberRepository.findAllByRolesInAndStudentIdLike(OLD_ROLES, search)
            : memberRepository.findAllByRolesInAndNameLike(OLD_ROLES, search);

    return members.stream()
        .map(
            member ->
                new ApprovedMemberManagementDto(
                    member.getName(),
                    member.getId(),
                    member.getStudentId(),
                    member.getPhone(),
                    member.getSchoolInformation().getMemberType(),
                    member.getRole(),
                    member.getSchoolInformation().getGeneration(),
                    member.getSchoolInformation().getMajor()))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<ApprovedMemberManagementDto> getGraduatedMembersBySearch(String search) {
    final List<Member> members =
        StringUtils.isNumeric(search)
            ? memberRepository.findAllGraduatedByRolesInAndStudentLike(OLD_ROLES, search)
            : memberRepository.findAllGraduatedByRolesInAndNameLike(OLD_ROLES, search);

    return members.stream()
        .map(
            member ->
                new ApprovedMemberManagementDto(
                    member.getName(),
                    member.getId(),
                    member.getStudentId(),
                    member.getPhone(),
                    member.getSchoolInformation().getMemberType(),
                    member.getRole(),
                    member.getSchoolInformation().getGeneration(),
                    member.getSchoolInformation().getMajor()))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<ExecutiveMemberDto> getExecutiveMembers() {
    final List<Member> members =
        memberRepository.findAllByRolesInAndNameLike(EXECUTIVE_ROLES, BLANK);
    return members.stream()
        .map(
            member ->
                new ExecutiveMemberDto(
                    member.getName(),
                    member.getId(),
                    member.getStudentId(),
                    member.getRole(),
                    member.getSchoolInformation().getGeneration(),
                    member.getSchoolInformation().getMajor(),
                    member.getPicture()))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<HallOfFameDto> getHallOfFame() {
    final List<Member> members = memberRepository.findAllByIbasInformation_IsHOF(true);
    return members.stream()
        .map(
            member ->
                new HallOfFameDto(
                    member.getName(),
                    member.getId(),
                    member.getStudentId(),
                    member.getSchoolInformation().getGeneration(),
                    member.getSchoolInformation().getMajor(),
                    member.getPicture(),
                    member.getIbasInformation().getIntroduce(),
                    member.getEmail()))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void updateUnapprovedMembers(List<Long> memberIdList, String state) {

    List<Member> members = memberRepository.findAllById(memberIdList);
    boolean allNewMembers =
        members.stream()
            .allMatch(member -> DEFAULT_ROLE_AFTER_FINISH_SIGNUP.equals(member.getRole()));

    if (!allNewMembers || !(state.equals(PASS_STATE) || state.equals(FAIL_STATE))) {
      throw new InvalidInputException();
    }

    if (state.equals(PASS_STATE)) {
      memberRepository.saveAll(members);

      for (Member member : members) {
        member.setRole(DEACTIVATED);
        Map<String, Object> variables = Map.of("memberName", member.getName());
        String to = member.getEmail();
        amazonSMTPService.sendPassMail(PASS_EMAIL_SUBJECT, variables, to);
      }
    } else {
      answerRepository.deleteByMember_IdIn(memberIdList);
      memberSocialAccountRepository.deleteByMember_IdIn(memberIdList);
      memberRepository.deleteAll(members);

      for (Member member : members) {
        Map<String, Object> variables = Map.of("memberName", member.getName());
        String to = member.getEmail();
        amazonSMTPService.sendRejectMail(FAIL_EMAIL_SUBJECT, variables, to);
      }
    }
  }

  @Override
  @Transactional
  public void updateApprovedMembersRole(List<Long> memberIdList, Role role) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    validateMemberRolesToUpdate(authentication, memberIdList, role);

    List<Member> members = memberRepository.findAllById(memberIdList);
    members.forEach(member -> member.setRole(role));

    memberRepository.saveAll(members);
  }

  @Override
  @Transactional
  public void updateApprovedMembersType(List<Long> memberIdList, MemberType type) {

    List<Member> members = memberRepository.findAllById(memberIdList);
    boolean allApprovedMembers =
        members.stream().allMatch(member -> OLD_ROLES.contains(member.getRole()));

    if (!allApprovedMembers) {
      throw new InvalidInputException();
    }

    for (Member member : members) {
      member.setMemberType(type);
    }

    memberRepository.saveAll(members);
  }

  @Override
  @Transactional(readOnly = true)
  public ContactDto getChiefContact() {

    Member chief = memberRepository.findByIbasInformation_Role(CHIEF);

    return new ContactDto(chief.getName(), chief.getPhone(), chief.getEmail());
  }

  private void validateMemberRolesToUpdate(
      Authentication authentication, List<Long> memberIdList, Role role) {
    List<Member> members = memberRepository.findAllById(memberIdList);
    boolean allApprovedMembers;

    if (hasRoleToChange(authentication, CHIEF) || hasRoleToChange(authentication, VICE_CHIEF)) {
      if (!CHIEF_CHANGEABLE_ROLES.contains(role)) {
        throw new InvalidInputException();
      }
      allApprovedMembers = checkAllMembersHaveAllowedRoles(members, CHIEF_CHANGEABLE_ROLES);

    } else {
      if (!SECRETARY_CHANGEABLE_ROLES.contains(role)) {
        throw new InvalidInputException();
      }
      allApprovedMembers = checkAllMembersHaveAllowedRoles(members, SECRETARY_CHANGEABLE_ROLES);
    }

    if (!allApprovedMembers) {
      throw new InvalidInputException();
    }
  }

  private boolean hasRoleToChange(Authentication authentication, Role role) {
    return authentication.getAuthorities().stream()
        .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(ROLE_PREFIX + role));
  }

  private boolean checkAllMembersHaveAllowedRoles(List<Member> members, Set<Role> allowedRoles) {
    return members.stream().allMatch(member -> allowedRoles.contains(member.getRole()));
  }
}
