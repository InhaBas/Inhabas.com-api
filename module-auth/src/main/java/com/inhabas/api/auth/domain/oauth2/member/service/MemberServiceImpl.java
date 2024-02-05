package com.inhabas.api.auth.domain.oauth2.member.service;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.NOT_APPROVED;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.UpdateNameRequest;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.dto.MyProfileDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ProfileDetailDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ProfileIntroDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ProfileNameDto;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.member.repository.UpdateNameRequestRepository;
import com.inhabas.api.auth.domain.oauth2.socialAccount.domain.valueObject.UID;
import com.inhabas.api.auth.domain.oauth2.userInfo.OAuth2UserInfo;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private static final Role DEFAULT_ROLE_AFTER_FINISH_SIGNUP = NOT_APPROVED;
  private final MemberRepository memberRepository;
  private final UpdateNameRequestRepository updateNameRequestRepository;
  private final MemberDuplicationChecker duplicationChecker;

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
  @Transactional
  public void updateSocialAccountInfo(OAuth2UserInfo oAuth2UserInfo) {

    Member member =
        memberRepository
            .findByProviderAndUid(oAuth2UserInfo.getProvider(), new UID(oAuth2UserInfo.getId()))
            .orElse(new Member(oAuth2UserInfo))
            .setLastLoginTime(LocalDateTime.now());

    memberRepository.save(member);
  }

  @Override
  @Transactional(readOnly = true)
  public MyProfileDto getMyProfile(Long memberId) {

    Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

    return MyProfileDto.builder()
        .name(member.getName())
        .studentId(member.getStudentId())
        .major(member.getSchoolInformation().getMajor())
        .grade(member.getSchoolInformation().getGrade())
        .email(member.getEmail())
        .picture(member.getPicture())
        .phoneNumber(member.getPhone())
        .role(member.getRole())
        .type(member.getSchoolInformation().getMemberType())
        .introduce(member.getIbasInformation().getIntroduce())
        .build();
  }

  @Override
  @Transactional
  public void updateMyProfileDetail(Long memberId, ProfileDetailDto profileDetailDto) {

    Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

    if (profileDetailDto.getMajor() != null)
      member.getSchoolInformation().setMajor(profileDetailDto.getMajor());
    if (profileDetailDto.getPhoneNumber() != null)
      member.setPhone(profileDetailDto.getPhoneNumber());
    if (profileDetailDto.getGrade() != null)
      member.getSchoolInformation().setGrade(profileDetailDto.getGrade());
  }

  @Override
  @Transactional
  public void updateMyProfileIntro(Long memberId, ProfileIntroDto profileIntroDto) {

    Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

    if (profileIntroDto.getIsHOF() != null)
      member.getIbasInformation().setIsHOF(profileIntroDto.getIsHOF());

    member.getIbasInformation().setIntroduce(profileIntroDto.getIntroduce());
  }

  @Override
  @Transactional
  public void requestMyProfileName(Long memberId, ProfileNameDto profileNameDto) {

    Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

    UpdateNameRequest updateNameRequest =
        UpdateNameRequest.builder().member(member).name(profileNameDto.getName()).build();

    updateNameRequestRepository.save(updateNameRequest);
  }
}
