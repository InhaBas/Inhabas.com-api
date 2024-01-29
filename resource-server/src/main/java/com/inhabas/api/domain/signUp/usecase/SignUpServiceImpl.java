package com.inhabas.api.domain.signUp.usecase;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType.PROFESSOR;

import com.inhabas.api.auth.domain.oauth2.majorInfo.dto.MajorInfoDto;
import com.inhabas.api.auth.domain.oauth2.majorInfo.usecase.MajorInfoService;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.SchoolInformation;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.member.service.MemberService;
import com.inhabas.api.auth.domain.oauth2.socialAccount.domain.entity.MemberSocialAccount;
import com.inhabas.api.auth.domain.oauth2.socialAccount.repository.MemberSocialAccountRepository;
import com.inhabas.api.domain.questionnaire.dto.QuestionnaireDto;
import com.inhabas.api.domain.questionnaire.usecase.QuestionnaireService;
import com.inhabas.api.domain.signUp.dto.AnswerDto;
import com.inhabas.api.domain.signUp.dto.SignUpDto;
import com.inhabas.api.domain.signUp.exception.NotWriteAnswersException;
import com.inhabas.api.domain.signUp.exception.NotWriteProfileException;
import com.inhabas.api.domain.signUpSchedule.usecase.SignUpScheduler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SignUpServiceImpl implements SignUpService {

  private final MajorInfoService majorInfoService;
  private final MemberRepository memberRepository;
  private final MemberSocialAccountRepository memberSocialAccountRepository;
  private final MemberService memberService;
  private final SignUpScheduler signUpScheduler;
  private final QuestionnaireService questionnaireService;
  private final AnswerService answerService;

  @Override
  @Transactional
  public void saveSignUpForm(SignUpDto signUpForm, Long memberId) {

    Integer generation = signUpScheduler.getSchedule().getGeneration();
    Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

    member.setName(signUpForm.getName());
    member.setPhone(signUpForm.getPhoneNumber());
    member.setStudentId(signUpForm.getStudentId());

    if (signUpForm.getGrade() != null) {
      member.setSchoolInformation(
          new SchoolInformation(
              signUpForm.getMajor(),
              signUpForm.getGrade(),
              generation,
              signUpForm.getMemberType()));
    } else {
      // 교수
      member.setSchoolInformation(
          new SchoolInformation(signUpForm.getMajor(), generation, signUpForm.getMemberType()));
      completeSignUp(null, memberId);
    }
  }

  @Override
  public void completeSignUp(List<AnswerDto> answerDtoList, Long memberId) {

    Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

    if (notYetWroteProfile(member)) {
      throw new NotWriteProfileException();
    }

    // 교수일 경우 이 과정 스킵
    if (member.getSchoolInformation().getMemberType() != PROFESSOR
        && isContentNullInAnyDto(answerDtoList)) {
      throw new NotWriteAnswersException();
    } else if (member.getSchoolInformation().getMemberType() != PROFESSOR
        && !isContentNullInAnyDto(answerDtoList)) {
      saveAnswers(answerDtoList, memberId);
    }

    memberSocialAccountRepository.save(
        new MemberSocialAccount(
            member, member.getEmail(), member.getUid().getValue(), member.getProvider()));
    memberService.finishSignUp(member);
  }

  public boolean isContentNullInAnyDto(List<AnswerDto> answerDtoList) {
    if (answerDtoList == null || answerDtoList.isEmpty()) {
      return true;
    }
    for (AnswerDto dto : answerDtoList) {
      if (dto.getContent() == null) {
        return true;
      }
    }
    return false;
  }

  private boolean notYetWroteProfile(Member member) {

    return member.getSchoolInformation() == null
        || member.getSchoolInformation().getMemberType() == null
        || member.getSchoolInformation().getMajor() == null
        || member.getStudentId() == null
        || member.getPhone() == null;
  }

  @Override
  public List<QuestionnaireDto> getQuestionnaire() {
    return questionnaireService.getQuestionnaire();
  }

  @Override
  public void saveAnswers(List<AnswerDto> answerDtoList, Long memberId) {
    answerService.saveAnswers(answerDtoList, memberId);
  }

  @Override
  public List<AnswerDto> getAnswers(Long memberId) {
    return answerService.getAnswers(memberId);
  }

  @Override
  public List<MajorInfoDto> getMajorInfo() {
    return majorInfoService.getAllMajorInfo();
  }

  @Override
  public boolean isSignedUp(Long memberId) {
    return memberSocialAccountRepository.existsByMember_Id(memberId);
  }

  @Override
  @Transactional(readOnly = true)
  public SignUpDto loadSignUpForm(Long memberId) {

    Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

    return SignUpDto.builder()
        .studentId(member.getStudentId())
        .phoneNumber(member.getPhone())
        .name(member.getName())
        .major(
            member.getSchoolInformation() == null ? null : member.getSchoolInformation().getMajor())
        .memberType(
            member.getSchoolInformation() == null
                ? null
                : member.getSchoolInformation().getMemberType())
        .grade(
            member.getSchoolInformation() == null ? null : member.getSchoolInformation().getGrade())
        .build();
  }
}
