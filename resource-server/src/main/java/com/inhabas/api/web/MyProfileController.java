package com.inhabas.api.web;

import java.util.List;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.auth.domain.oauth2.member.dto.*;
import com.inhabas.api.domain.member.usecase.MemberProfileService;
import com.inhabas.api.domain.myInfo.dto.MyBoardsDto;
import com.inhabas.api.domain.myInfo.dto.MyBudgetSupportApplicationDto;
import com.inhabas.api.domain.myInfo.dto.MyCommentsDto;
import com.inhabas.api.domain.myInfo.usecase.MyInfoService;
import com.inhabas.api.global.dto.PageInfoDto;
import com.inhabas.api.global.dto.PagedResponseDto;
import com.inhabas.api.global.util.PageUtil;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@RestController
@Tag(name = "내 정보 관리", description = "마이 페이지 기능")
@RequiredArgsConstructor
public class MyProfileController {

  private final MemberProfileService memberProfileService;
  private final MyInfoService myInfoService;

  @Operation(summary = "내 정보 조회", description = "사용자 자신의 정보만 조회 가능")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = MyProfileDto.class))}),
      })
  @GetMapping("/myInfo")
  public ResponseEntity<MyProfileDto> getMyProfile(@Authenticated Long memberId) {
    return ResponseEntity.ok(memberProfileService.getMyProfile(memberId));
  }

  @Operation(summary = "내 [학과, 학년, 전화번호, 타입] 수정", description = "학과, 학년, 전화번호 수정. ")
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
    memberProfileService.updateMyProfileDetail(memberId, profileDetailDto);
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
    memberProfileService.updateMyProfileIntro(memberId, profileIntroDto);
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
  @PostMapping(value = "/myInfo/picture", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> updateMyProfileImage(
      @Authenticated Long memberId, @RequestPart(value = "picture") MultipartFile file) {
    memberProfileService.updateMyProfileImage(memberId, file);
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
    memberProfileService.requestMyProfileName(memberId, profileNameDto);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "자신이 보낸 이름 수정 요청 조회", description = "자신이 보낸 이름 수정 요청 조회")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = UpdateNameRequestDto.class))}),
      })
  @GetMapping("/myInfo/myRequests")
  public ResponseEntity<PagedResponseDto<UpdateNameRequestDto>> getMyInfoMyRequests(
      @Parameter(description = "페이지", example = "0")
          @RequestParam(name = "page", defaultValue = "0")
          int page,
      @Parameter(description = "페이지당 개수", example = "10")
          @RequestParam(name = "size", defaultValue = "10")
          int size,
      @Authenticated Long memberId) {
    Pageable pageable = PageRequest.of(page, size);
    List<UpdateNameRequestDto> allDtos = memberProfileService.getMyInfoMyRequests(memberId);
    List<UpdateNameRequestDto> pagedDtos = PageUtil.getPagedDtoList(pageable, allDtos);

    PageImpl<UpdateNameRequestDto> updateNameRequestDtoPage =
        new PageImpl<>(pagedDtos, pageable, allDtos.size());
    PageInfoDto pageInfoDto = new PageInfoDto(updateNameRequestDtoPage);

    return ResponseEntity.ok(new PagedResponseDto<>(pageInfoDto, pagedDtos));
  }

  @Operation(summary = "이름 수정 요청 조회", description = "이름 수정 요청 조회")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = UpdateNameRequestDto.class))}),
      })
  @GetMapping("/myInfo/requests")
  public ResponseEntity<PagedResponseDto<UpdateNameRequestDto>> getMyInfoRequests(
      @Parameter(description = "페이지", example = "0")
          @RequestParam(name = "page", defaultValue = "0")
          int page,
      @Parameter(description = "페이지당 개수", example = "10")
          @RequestParam(name = "size", defaultValue = "10")
          int size,
      @Authenticated Long memberId) {
    Pageable pageable = PageRequest.of(page, size);
    List<UpdateNameRequestDto> allDtos = memberProfileService.getMyInfoRequests();
    List<UpdateNameRequestDto> pagedDtos = PageUtil.getPagedDtoList(pageable, allDtos);

    PageImpl<UpdateNameRequestDto> updateNameRequestDtoPage =
        new PageImpl<>(pagedDtos, pageable, allDtos.size());
    PageInfoDto pageInfoDto = new PageInfoDto(updateNameRequestDtoPage);

    return ResponseEntity.ok(new PagedResponseDto<>(pageInfoDto, pagedDtos));
  }

  @Operation(summary = "이름 수정 요청 처리", description = "이름 수정 요청 처리")
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
        @ApiResponse(
            responseCode = "404",
            description = "데이터가 존재하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 404, \"code\": \"G004\", \"message\": \"데이터가 존재하지 않습니다.\"}")))
      })
  @PutMapping("/myInfo/request")
  public ResponseEntity<Void> handleMyInfoRequest(
      @Authenticated Long memberId, @Valid @RequestBody HandleNameRequestDto handleNameRequestDto) {
    memberProfileService.handleMyInfoRequest(handleNameRequestDto);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "내가 쓴 게시글 목록 조회")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = UpdateNameRequestDto.class))}),
      })
  @GetMapping("/myInfo/boards")
  public ResponseEntity<PagedResponseDto<MyBoardsDto>> getBoardList(
      @Parameter(description = "페이지", example = "0")
          @RequestParam(name = "page", defaultValue = "0")
          int page,
      @Parameter(description = "페이지당 개수", example = "10")
          @RequestParam(name = "size", defaultValue = "10")
          int size,
      @Authenticated Long memberId) {

    Pageable pageable = PageRequest.of(page, size);
    List<MyBoardsDto> allDtoList = myInfoService.getMyBoards();
    List<MyBoardsDto> pagedDtoList = PageUtil.getPagedDtoList(pageable, allDtoList);

    PageImpl<MyBoardsDto> myBoardsDtoPage =
        new PageImpl<>(pagedDtoList, pageable, allDtoList.size());
    PageInfoDto pageInfoDto = new PageInfoDto(myBoardsDtoPage);

    return ResponseEntity.ok(new PagedResponseDto<>(pageInfoDto, pagedDtoList));
  }

  @Operation(summary = "내가 쓴 댓글 목록 조회")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = UpdateNameRequestDto.class))}),
      })
  @GetMapping("/myInfo/comments")
  public ResponseEntity<PagedResponseDto<MyCommentsDto>> getCommentsList(
      @Parameter(description = "페이지", example = "0")
          @RequestParam(name = "page", defaultValue = "0")
          int page,
      @Parameter(description = "페이지당 개수", example = "10")
          @RequestParam(name = "size", defaultValue = "10")
          int size,
      @Authenticated Long memberId) {

    Pageable pageable = PageRequest.of(page, size);
    List<MyCommentsDto> allDtoList = myInfoService.getMyComments();
    List<MyCommentsDto> pagedDtoList = PageUtil.getPagedDtoList(pageable, allDtoList);

    PageImpl<MyCommentsDto> myCommentsDtoPage =
        new PageImpl<>(pagedDtoList, pageable, allDtoList.size());
    PageInfoDto pageInfoDto = new PageInfoDto(myCommentsDtoPage);

    return ResponseEntity.ok(new PagedResponseDto<>(pageInfoDto, pagedDtoList));
  }

  @GetMapping("/myInfo/budgetSupportApplications")
  public ResponseEntity<PagedResponseDto<MyBudgetSupportApplicationDto>>
      getBudgetSupportApplicationsList(
          @Parameter(description = "페이지", example = "0")
              @RequestParam(name = "page", defaultValue = "0")
              int page,
          @Parameter(description = "페이지당 개수", example = "10")
              @RequestParam(name = "size", defaultValue = "10")
              int size,
          @Authenticated Long memberId) {

    Pageable pageable = PageRequest.of(page, size);
    List<MyBudgetSupportApplicationDto> allDtoList = myInfoService.getMyBudgetSupportApplications();
    List<MyBudgetSupportApplicationDto> pagedDtoList =
        PageUtil.getPagedDtoList(pageable, allDtoList);

    PageImpl<MyBudgetSupportApplicationDto> myBudgetSupportApplicationDtoPage =
        new PageImpl<>(pagedDtoList, pageable, allDtoList.size());
    PageInfoDto pageInfoDto = new PageInfoDto(myBudgetSupportApplicationDtoPage);

    return ResponseEntity.ok(new PagedResponseDto<>(pageInfoDto, pagedDtoList));
  }

  // [모임, 강의 조회] 추후 개발 예정
}
