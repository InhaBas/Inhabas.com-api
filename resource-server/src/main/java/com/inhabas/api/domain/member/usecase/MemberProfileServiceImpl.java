package com.inhabas.api.domain.member.usecase;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.SdkClientException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.UpdateNameRequest;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.dto.*;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.member.repository.UpdateNameRequestRepository;
import com.inhabas.api.domain.board.exception.InvalidFileExtensionException;
import com.inhabas.api.domain.board.exception.S3UploadFailedException;
import com.inhabas.api.domain.file.usecase.S3Service;
import com.inhabas.api.global.util.FileUtil;

@Service
@RequiredArgsConstructor
public class MemberProfileServiceImpl implements MemberProfileService {

  private final MemberRepository memberRepository;
  private final UpdateNameRequestRepository updateNameRequestRepository;
  private final S3Service s3Service;
  private static final String DIR_NAME = "myInfo/";
  protected static final String DEFAULT_PROFILE_URL =
      "https://inhabas-bucket.s3.ap-northeast-2.amazonaws.com/public/default-profile.png";

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
        .isHOF(member.getIbasInformation().getIsHOF())
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
    if (profileDetailDto.getType() != null) member.setMemberType(profileDetailDto.getType());
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
  public void updateMyProfileImage(Long memberId, MultipartFile file) {

    Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

    // file이 null이거나 비어있을 경우 기본 프로필 이미지 URL을 사용
    if (file == null || file.isEmpty()) {
      member.setPicture(DEFAULT_PROFILE_URL);
    } else {
      String path = FileUtil.generateFileName(file, DIR_NAME);
      String url = null;
      try {
        url = s3Service.uploadS3Image(file, path);
      } catch (IOException e) {
        throw new InvalidFileExtensionException();
      } catch (SdkClientException e) {
        throw new S3UploadFailedException();
      }
      member.setPicture(url);
    }

    memberRepository.save(member);
  }

  @Override
  @Transactional
  public void requestMyProfileName(Long memberId, ProfileNameDto profileNameDto) {

    Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

    UpdateNameRequest updateNameRequest =
        UpdateNameRequest.builder().member(member).name(profileNameDto.getName()).build();

    updateNameRequestRepository.save(updateNameRequest);
  }

  // (회장이 이름 변경 요청을 승인한 경우 <- controller에 구현) 이름 수정
  @Override
  @Transactional
  public void updateMyProfileName(Long memberId, ProfileNameDto profileNameDto) {
    Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    member.setName(profileNameDto.getName());
    memberRepository.save(member);
  }

  @Override
  public List<UpdateNameRequestDto> getMyInfoMyRequests(Long memberId) {
    List<UpdateNameRequest> updateNameRequestList =
        updateNameRequestRepository.findAllByMemberIdOrderByStatusAndDateRequested(memberId);

    return updateNameRequestList.stream()
        .map(
            request ->
                UpdateNameRequestDto.builder()
                    .id(request.getId())
                    .studentId(request.getMember().getStudentId())
                    .major(request.getMember().getSchoolInformation().getMajor())
                    .role(request.getMember().getRole())
                    .beforeName(request.getMember().getName())
                    .afterName(request.getName().getValue())
                    .dateRequested(request.getDateRequested())
                    .status(request.getRequestStatus())
                    .rejectReason(request.getRejectReason())
                    .build())
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<UpdateNameRequestDto> getMyInfoRequests() {

    List<UpdateNameRequest> updateNameRequestList =
        updateNameRequestRepository.findAllOrderByStatusAndDateRequested();

    return updateNameRequestList.stream()
        .map(
            request ->
                UpdateNameRequestDto.builder()
                    .id(request.getId())
                    .studentId(request.getMember().getStudentId())
                    .major(request.getMember().getSchoolInformation().getMajor())
                    .role(request.getMember().getRole())
                    .beforeName(request.getMember().getName())
                    .afterName(request.getName().getValue())
                    .dateRequested(request.getDateRequested())
                    .status(request.getRequestStatus())
                    .rejectReason(request.getRejectReason())
                    .build())
        .collect(Collectors.toList());
  }

  @Override
  public void handleMyInfoRequest(HandleNameRequestDto handleNameRequestDto) {

    UpdateNameRequest updateNameRequest =
        updateNameRequestRepository
            .findById(handleNameRequestDto.getId())
            .orElseThrow(NotFoundException::new);

    updateNameRequest.handleRequest(
        handleNameRequestDto.getStatus(), handleNameRequestDto.getRejectReason());

    updateNameRequestRepository.save(updateNameRequest);
  }
}
