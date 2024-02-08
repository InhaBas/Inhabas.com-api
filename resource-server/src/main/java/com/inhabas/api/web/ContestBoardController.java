package com.inhabas.api.web;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.domain.contest.domain.valueObject.ContestType;
import com.inhabas.api.domain.contest.dto.ContestBoardDetailDto;
import com.inhabas.api.domain.contest.dto.ContestBoardDto;
import com.inhabas.api.domain.contest.dto.SaveContestBoardDto;
import com.inhabas.api.domain.contest.usecase.ContestBoardService;
import com.inhabas.api.global.dto.PageInfoDto;
import com.inhabas.api.global.dto.PagedMemberResponseDto;
import com.inhabas.api.global.util.PageUtil;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "공모전 게시판")
@RestController
@RequiredArgsConstructor
public class ContestBoardController {

  private final ContestBoardService contestBoardService;

  @Operation(summary = "공모전 게시판 목록 조회", description = "공모전 게시판 목록 조회 (썸네일은 첫 사진 첨부파일)")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = PagedMemberResponseDto.class))}),
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
  @SecurityRequirements(value = {})
  @GetMapping("/contest/{contestTypeString}")
  @PreAuthorize(
      // 공모전 게시판 MenuId : 18
      "@boardSecurityChecker.checkMenuAccess(18, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).READ_BOARD_LIST)")
  public ResponseEntity<PagedMemberResponseDto<ContestBoardDto>> getContestBoard(
      @PathVariable("contestType") ContestType contestType,
      @Parameter(description = "페이지", example = "1")
          @RequestParam(name = "page", defaultValue = "1")
          int page,
      @Parameter(description = "페이지당 개수", example = "4")
          @RequestParam(name = "size", defaultValue = "4")
          int size,
      @Parameter(description = "검색어", example = "")
          @RequestParam(name = "search", defaultValue = "")
          String search) {

    Pageable pageable = PageRequest.of(page, size);
    List<ContestBoardDto> allDtos =
        contestBoardService.getContestBoardsBySearch(contestType, search);
    List<ContestBoardDto> pagedDtos = PageUtil.getPagedDtoList(pageable, allDtos);

    PageImpl<ContestBoardDto> ContestBoardDtoPage =
        new PageImpl<>(pagedDtos, pageable, allDtos.size());
    PageInfoDto pageInfoDto = new PageInfoDto(ContestBoardDtoPage);

    return ResponseEntity.ok(new PagedMemberResponseDto<>(pageInfoDto, pagedDtos));
  }

  @Operation(summary = "공모전 게시판 글 생성", description = "공모전 게시판 글 생성 (활동회원 이상)")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "'Location' 헤더에 생성된 리소스의 URI 가 포함됩니다."),
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
  @PostMapping(path = "/contest/{contestType}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize(
      "@boardSecurityChecker.checkMenuAccess(18, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).CREATE_BOARD)")
  public ResponseEntity<Void> writeContestBoard(
      @Authenticated Long memberId,
      @PathVariable("contestType") ContestType contestType,
      @RequestPart("title") String title,
      @RequestPart("content") String content,
      @RequestPart("association") String association,
      @RequestPart("topic") String topic,
      @RequestPart("dateContestStart") LocalDate dateContestStart,
      @RequestPart("dateContestEnd") LocalDate dateContestEnd,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {

    SaveContestBoardDto saveContestBoardDto =
        new SaveContestBoardDto(
            title, content, association, topic, dateContestStart, dateContestEnd, files);

    // PathVariable인 {contestType}에 따라 공모전 글 작성 페이지가 CONTEST와 EXTERNAL_ACTIVITY로 분류됨
    Long newContestBoardId =
        contestBoardService.writeContestBoard(memberId, saveContestBoardDto, contestType);

    // 뒤에 추가 URI 생성
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{boardId}")
            .buildAndExpand(newContestBoardId)
            .toUri();

    return ResponseEntity.created(location).build();
  }

  @Operation(summary = "공모전 게시판 글 단일 조회", description = "공모전 게시판 글 단일 조회")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200"),
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
  @SecurityRequirements(value = {})
  @GetMapping("/contest/{contestType}/{boardId}")
  @PreAuthorize(
      "@boardSecurityChecker.checkMenuAccess(18, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).READ_BOARD)")
  public ResponseEntity<ContestBoardDetailDto> findContestBoard(
      @PathVariable("contestType") ContestType contestType, @PathVariable Long boardId) {

    ContestBoardDetailDto contestBoardDetailDto = contestBoardService.getContestBoard(boardId);

    return ResponseEntity.ok(contestBoardDetailDto);
  }

  @Operation(summary = "공모전 게시판 글 수정", description = "공모전 게시판 글 수정 (작성자, 회장만)")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200"),
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
  @PostMapping(
      path = "/contest/{contestType}/{boardId}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("@boardSecurityChecker.boardWriterOnly(#boardId) or hasRole('VICE_CHIEF')")
  public ResponseEntity<ContestBoardDto> updateContestBoard(
      @Authenticated Long memberId,
      @PathVariable("contestType") ContestType contestType,
      @PathVariable Long boardId,
      @RequestPart("title") String title,
      @RequestPart("content") String content,
      @RequestPart("topic") String topic,
      @RequestPart("association") String association,
      @RequestPart("dateContestStart") LocalDate dateContestStart,
      @RequestPart("dateContestEnd") LocalDate dateContestEnd,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {

    SaveContestBoardDto saveContestBoardDto =
        new SaveContestBoardDto(
            title, content, topic, association, dateContestStart, dateContestEnd, files);
    contestBoardService.updateContestBoard(boardId, saveContestBoardDto);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "공모전 게시판 글 삭제", description = "공모전 게시판 글 삭제 (작성자, 회장만)")
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
  @DeleteMapping("contest/{contestType}/{boardId}")
  @PreAuthorize("@boardSecurityChecker.boardWriterOnly(#boardId) or hasRole('VICE_CHIEF')")
  public ResponseEntity<ContestBoardDto> deleteContestBoard(
      @Authenticated Long memberId,
      @PathVariable ContestType contestType,
      @PathVariable Long boardId) {

    contestBoardService.deleteContestBoard(boardId);

    return ResponseEntity.noContent().build();
  }
}
