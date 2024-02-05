package com.inhabas.api.domain.member.usecase;

import com.inhabas.api.auth.domain.oauth2.member.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MemberProfileService {

    MyProfileDto getMyProfile(Long memberId);

    void updateMyProfileDetail(Long memberId, ProfileDetailDto profileDetailDto);

    void updateMyProfileIntro(Long memberId, ProfileIntroDto profileIntroDto);

    void updateMyProfileImage(Long memberId, MultipartFile file);

    void requestMyProfileName(Long memberId, ProfileNameDto profileNameDto);

    List<UpdateNameRequestDto> getMyInfoRequests();


}
