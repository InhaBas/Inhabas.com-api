package com.inhabas.api.domain.member.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.dto.MyProfileDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ProfileDetailDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ProfileIntroDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ProfileNameDto;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.member.repository.UpdateNameRequestRepository;
import com.inhabas.api.domain.file.usecase.S3Service;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class MemberProfileServiceImplTest {

  @InjectMocks MemberProfileServiceImpl memberProfileService;
  @Mock MemberRepository memberRepository;
  @Mock UpdateNameRequestRepository updateNameRequestRepository;
  @Mock S3Service s3Service;
  @Captor private ArgumentCaptor<Member> memberArgumentCaptor;

  @DisplayName("내 정보를 조회한다.")
  @Test
  void getMyProfileTest() {
    // given
    Member member = MemberTest.basicMember1();
    given(memberRepository.findById(any())).willReturn(Optional.of(member));

    // when
    MyProfileDto myProfileDto = memberProfileService.getMyProfile(any());

    // then
    assertThat(myProfileDto.getName()).isEqualTo(member.getName());
    assertThat(myProfileDto.getStudentId()).isEqualTo(member.getStudentId());
    assertThat(myProfileDto.getMajor()).isEqualTo(member.getSchoolInformation().getMajor());
    assertThat(myProfileDto.getGrade()).isEqualTo(member.getSchoolInformation().getGrade());
    assertThat(myProfileDto.getEmail()).isEqualTo(member.getEmail());
    assertThat(myProfileDto.getPhoneNumber()).isEqualTo(member.getPhone());
    assertThat(myProfileDto.getRole()).isEqualTo(member.getRole());
    assertThat(myProfileDto.getType()).isEqualTo(member.getSchoolInformation().getMemberType());
    assertThat(myProfileDto.getIntroduce()).isEqualTo(member.getIbasInformation().getIntroduce());
  }

  @DisplayName("내 정보 [학년, 전공, 전화번호] 를 수정한다.")
  @Test
  void updateMyProfileDetailTest() {
    // given
    Member member = MemberTest.basicMember1();
    given(memberRepository.findById(any())).willReturn(Optional.of(member));
    ProfileDetailDto profileDetailDto = new ProfileDetailDto("경영학과", null, null, null);

    // when
    memberProfileService.updateMyProfileDetail(any(), profileDetailDto);

    // then
    assertThat(member.getSchoolInformation().getMajor()).isEqualTo(profileDetailDto.getMajor());
  }

  @DisplayName("내 정보 [자기소개] 를 수정한다.")
  @Test
  void updateMyProfileIntroTest() {
    // given
    Member member = MemberTest.basicMember1();
    given(memberRepository.findById(any())).willReturn(Optional.of(member));
    ProfileIntroDto profileIntroDto = new ProfileIntroDto("HELLO", true);

    // when
    memberProfileService.updateMyProfileIntro(any(), profileIntroDto);

    // then
    assertThat(member.getIbasInformation().getIntroduce())
        .isEqualTo(profileIntroDto.getIntroduce());
    assertThat(member.getIbasInformation().getIsHOF()).isEqualTo(profileIntroDto.getIsHOF());
  }

  @DisplayName("내 정보 [이름] 을 수정 요청을 한다.")
  @Test
  void requestMyProfileNameTest() {
    // given
    Member member = MemberTest.basicMember1();
    given(memberRepository.findById(any())).willReturn(Optional.of(member));
    ProfileNameDto profileNameDto = new ProfileNameDto("유동현");

    // when
    memberProfileService.requestMyProfileName(any(), profileNameDto);

    // then
    then(updateNameRequestRepository).should(times(1)).save(any());
  }

  @DisplayName("내 정보 [프로필 사진] 수정시 null이 주어지면 기본 프로필 사진으로 업데이트한다.")
  @Test
  void updateMyProfileImageWithNullTest() {
    // given
    Member member = MemberTest.basicMember1();
    given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));

    // when
    memberProfileService.updateMyProfileImage(member.getId(), null);

    // then
    then(memberRepository).should(times(1)).save(memberArgumentCaptor.capture());
    Member updatedMember = memberArgumentCaptor.getValue();

    assertThat(updatedMember.getPicture()).isEqualTo(MemberProfileServiceImpl.DEFAULT_PROFILE_URL);
  }

  @DisplayName("내 정보 [프로필 사진]을 수정한다.")
  @Test
  void updateMyProfileImageTest() throws Exception {
    // given
    Member member = MemberTest.basicMember1();
    MultipartFile file = mock(MultipartFile.class);
    String expectedUrl =
        "https://inhabas-bucket.s3.ap-northeast-2.amazonaws.com/uploaded-image.png";

    given(memberRepository.findById(member.getId())).willReturn(Optional.of(member));
    given(file.isEmpty()).willReturn(false);
    given(s3Service.uploadS3Image(any(), any())).willReturn(expectedUrl);

    // when
    memberProfileService.updateMyProfileImage(member.getId(), file);

    // then
    then(memberRepository).should(times(1)).save(memberArgumentCaptor.capture());
    Member updatedMember = memberArgumentCaptor.getValue();

    assertThat(updatedMember.getPicture()).isEqualTo(expectedUrl);
  }
}
