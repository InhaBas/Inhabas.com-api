package com.inhabas.api.web;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.domain.scholarship.domain.ScholarshipBoardType;
import com.inhabas.api.domain.scholarship.dto.SaveScholarshipBoardDto;
import com.inhabas.api.domain.scholarship.dto.ScholarshipBoardDetailDto;
import com.inhabas.api.domain.scholarship.dto.ScholarshipBoardDto;
import com.inhabas.api.domain.scholarship.usecase.ScholarshipBoardService;
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
@Tag(name = "장학회 게시판 관리")
@RestController
@RequiredArgsConstructor
public class ScholarshipController {

  private final ScholarshipBoardService scholarshipBoardService;

  @Operation(summary = "장학회 게시글 목록 조회")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = PagedResponseDto.class))}),
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
  @GetMapping("/scholarship/{boardType}")
  @PreAuthorize(
      "@boardSecurityChecker.checkMenuAccess(#boardType.menuId, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).READ_BOARD_LIST)")
  public ResponseEntity<PagedResponseDto<ScholarshipBoardDto>> getBoardList(
      @Parameter(description = "페이지", example = "0")
          @RequestParam(name = "page", defaultValue = "0")
          int page,
      @Parameter(description = "페이지당 개수", example = "10")
          @RequestParam(name = "size", defaultValue = "10")
          int size,
      @Parameter(description = "검색어 (작성자 이름 or 제목 or 내용)", example = "")
          @RequestParam(name = "search", defaultValue = "")
          String search,
      @PathVariable ScholarshipBoardType boardType) {

    Pageable pageable = PageRequest.of(page, size);
    List<ScholarshipBoardDto> allDtoList = scholarshipBoardService.getPosts(boardType, search);
    List<ScholarshipBoardDto> pagedDtoList = PageUtil.getPagedDtoList(pageable, allDtoList);

    PageImpl<ScholarshipBoardDto> ScholarshipBoardDtoPage =
        new PageImpl<>(pagedDtoList, pageable, allDtoList.size());
    PageInfoDto pageInfoDto = new PageInfoDto(ScholarshipBoardDtoPage);

    return ResponseEntity.ok(new PagedResponseDto<>(pageInfoDto, pagedDtoList));
  }

  @Operation(summary = "장학회 게시글 단일 조회")
  @GetMapping("/scholarship/{boardType}/{boardId}")
  @PreAuthorize(
      "@boardSecurityChecker.checkMenuAccess(#boardType.menuId, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).READ_BOARD)")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        content = {@Content(schema = @Schema(implementation = ScholarshipBoardDetailDto.class))}),
    @ApiResponse(
        responseCode = "400",
        description = "입력값이 없거나, 타입이 유효하지 않습니다.",
        content =
            @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}")))
  })
  public ResponseEntity<ScholarshipBoardDetailDto> getBoard(
      @PathVariable Long boardId, @PathVariable ScholarshipBoardType boardType) {

    return ResponseEntity.ok(scholarshipBoardService.getPost(boardType, boardId));
  }

  @Operation(summary = "장학회 게시글 추가")
  @PostMapping("/scholarship/{boardType}")
  @PreAuthorize(
      "@boardSecurityChecker.checkMenuAccess(#boardType.menuId, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).CREATE_BOARD)")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "'Location' 헤더에 생성된 리소스의 URI 가 포함됩니다."),
        @ApiResponse(
            responseCode = "400",
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
  public ResponseEntity<Long> addBoard(
      @Authenticated Long memberId,
      @PathVariable ScholarshipBoardType boardType,
      @Valid @RequestBody SaveScholarshipBoardDto form) {
    Long newScholarshipBoardId = scholarshipBoardService.write(boardType, form, memberId);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{boardId}")
            .buildAndExpand(newScholarshipBoardId)
            .toUri();

    return ResponseEntity.created(location).build();
  }

  @Operation(summary = "장학회 게시글 수정")
  @PostMapping("/scholarship/{boardType}/{boardId}")
  @ApiResponses({
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
  @PreAuthorize("@boardSecurityChecker.boardWriterOnly(#boardId) or hasRole('VICE_CHIEF')")
  public ResponseEntity<Long> updateBoard(
      @Authenticated Long memberId,
      @PathVariable ScholarshipBoardType boardType,
      @PathVariable Long boardId,
      @Valid @RequestBody SaveScholarshipBoardDto form) {
    scholarshipBoardService.update(boardId, boardType, form, memberId);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "장학회 게시글 삭제")
  @DeleteMapping("/scholarship/{boardType}/{boardId}")
  @ApiResponses({
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
  @PreAuthorize("@boardSecurityChecker.boardWriterOnly(#boardId) or hasRole('VICE_CHIEF')")
  public ResponseEntity<?> deleteBoard(
      @Authenticated Long memberId,
      @PathVariable ScholarshipBoardType boardType,
      @PathVariable Long boardId) {

    scholarshipBoardService.delete(boardType, boardId);
    return ResponseEntity.noContent().build();
  }
}
