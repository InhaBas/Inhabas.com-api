package com.inhabas.api.web;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.auth.domain.oauth2.member.dto.MyProfileDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ProfileDetailDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ProfileIntroDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ProfileNameDto;
import com.inhabas.api.auth.domain.oauth2.member.service.MemberService;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "내 정보 관리", description = "마이 페이지 기능")
@RequiredArgsConstructor
public class MyProfileController {

  private final MemberService memberService;

  @Operation(summary = "내 정보 조회", description = "사용자 자신의 정보만 조회 가능")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = MyProfileDto.class))}),
      })
  @GetMapping("/myInfo")
  public ResponseEntity<MyProfileDto> getMyProfile(@Authenticated Long memberId) {
    return ResponseEntity.ok(memberService.getMyProfile(memberId));
  }

  @Operation(summary = "내 [학과, 학년, 전화번호] 수정", description = "학과, 학년, 전화번호 수정. ")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400 ",
            description = "입력값이 없거나, 타입이 유효하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"))),
      })
  @PutMapping("/myInfo/detail")
  public ResponseEntity<Void> updateMyProfileDetail(
      @Authenticated Long memberId, @Valid @RequestBody ProfileDetailDto profileDetailDto) {
    memberService.updateMyProfileDetail(memberId, profileDetailDto);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "내 프로필 자기소개 수정", description = "자기소개, 공개여부 수정")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400 ",
            description = "입력값이 없거나, 타입이 유효하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"))),
      })
  @PutMapping("/myInfo/intro")
  public ResponseEntity<Void> updateMyProfileIntro(
      @Authenticated Long memberId, @Valid @RequestBody ProfileIntroDto profileIntroDto) {
    memberService.updateMyProfileIntro(memberId, profileIntroDto);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "내 프로필 사진 수정", description = "프로필 사진 수정")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400 ",
            description = "입력값이 없거나, 타입이 유효하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"))),
      })
  @PutMapping("/myInfo/image")
  public ResponseEntity<Void> updateMyProfileImage(@Authenticated Long memberId) {

    // 첨부파일 문제로 보류
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "내 정보 이름 수정", description = "이름 수정, 회장의 승인 필요")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204"),
        @ApiResponse(
            responseCode = "400 ",
            description = "입력값이 없거나, 타입이 유효하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"))),
      })
  @PutMapping("/myInfo/name")
  public ResponseEntity<Void> requestMyProfileName(
      @Authenticated Long memberId, @Valid @RequestBody ProfileNameDto profileNameDto) {
    memberService.requestMyProfileName(memberId, profileNameDto);
    return ResponseEntity.noContent().build();
  }

  // [모임, 글, 댓글, 예산신청 조회] 추후 개발 예정

}
