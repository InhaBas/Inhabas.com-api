package com.inhabas.api.domain.member.usecase;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.inhabas.api.auth.domain.oauth2.member.dto.*;

public interface MemberProfileService {

  MyProfileDto getMyProfile(Long memberId);

  void updateMyProfileDetail(Long memberId, ProfileDetailDto profileDetailDto);

  void updateMyProfileIntro(Long memberId, ProfileIntroDto profileIntroDto);

  void updateMyProfileImage(Long memberId, MultipartFile file);

  void requestMyProfileName(Long memberId, ProfileNameDto profileNameDto);

  List<UpdateNameRequestDto> getMyInfoMyRequests(Long memberId);

  List<UpdateNameRequestDto> getMyInfoRequests();

  void handleMyInfoRequest(HandleNameRequestDto handleNameRequestDto);
}
