package com.inhabas.api.web;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
import com.inhabas.api.domain.board.dto.BoardCountDto;
import com.inhabas.api.domain.board.repository.BaseBoardRepository;
import com.inhabas.api.domain.contest.domain.valueObject.ContestType;
import com.inhabas.api.domain.contest.dto.ContestBoardDetailDto;
import com.inhabas.api.domain.contest.dto.ContestBoardDto;
import com.inhabas.api.domain.contest.dto.SaveContestBoardDto;
import com.inhabas.api.domain.contest.usecase.ContestBoardService;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@Tag(name = "공모전 게시글 관리")
@RestController
@RequiredArgsConstructor
public class ContestBoardController {

  private final ContestBoardService contestBoardService;
  private final BaseBoardRepository baseBoardRepository;

  @Operation(summary = "게시판 종류 당 글 개수 조회")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = BoardCountDto.class))}),
      })
  @GetMapping("/contest/count")
  @SecurityRequirements(value = {})
  public ResponseEntity<List<BoardCountDto>> getBoardCount() {
    return ResponseEntity.ok(baseBoardRepository.countRowsGroupByMenuName(2));
  }

  @Operation(summary = "공모전 게시글 목록 조회")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = PagedResponseDto.class))}),
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
  @GetMapping("/contest/{contestType}")
  @PreAuthorize(
      "@boardSecurityChecker.checkMenuAccess(#contestType.menuId, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).READ_BOARD_LIST)")
  public ResponseEntity<PagedResponseDto<ContestBoardDto>> getContestBoard(
      @PathVariable ContestType contestType,
      @Parameter(description = "공모전 분야", example = "1")
          @RequestParam(name = "contestFieldId", required = false)
          Long contestFieldId,
      @Parameter(description = "검색어 (작성자 이름 or 제목 or 내용)", example = "")
          @RequestParam(name = "search", defaultValue = "")
          String search,
      @Parameter(description = "페이지", example = "1")
          @RequestParam(name = "page", defaultValue = "1")
          int page,
      @Parameter(description = "페이지당 개수", example = "4")
          @RequestParam(name = "size", defaultValue = "4")
          int size,
      @Parameter(description = "'boardId' 또는 'dateContestEnd' 기준 내림차순 정렬", example = "boardId")
          @RequestParam(name = "sortBy", defaultValue = "dateContestEnd")
          String sortBy) {

    Pageable pageable = PageRequest.of(page, size);
    List<ContestBoardDto> allDtoList =
        contestBoardService.getContestBoards(contestType, contestFieldId, search, sortBy);
    List<ContestBoardDto> pagedDtoList = PageUtil.getPagedDtoList(pageable, allDtoList);

    PageImpl<ContestBoardDto> ContestBoardDtoPage =
        new PageImpl<>(pagedDtoList, pageable, allDtoList.size());
    PageInfoDto pageInfoDto = new PageInfoDto(ContestBoardDtoPage);

    return ResponseEntity.ok(new PagedResponseDto<>(pageInfoDto, pagedDtoList));
  }

  @Operation(summary = "공모전 게시글 단일 조회")
  @GetMapping("/contest/{contestType}/{boardId}")
  @PreAuthorize(
      "@boardSecurityChecker.checkMenuAccess(#contestType.menuId, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).READ_BOARD)")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200"),
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
  public ResponseEntity<ContestBoardDetailDto> getContestBoard(
      @PathVariable Long boardId, @PathVariable ContestType contestType) {

    return ResponseEntity.ok(contestBoardService.getContestBoard(contestType, boardId));
  }

  @Operation(summary = "공모전 게시글 추가")
  @PostMapping(path = "/contest/{contestType}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize(
      "@boardSecurityChecker.checkMenuAccess(#contestType.menuId, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).CREATE_BOARD)")
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
  public ResponseEntity<Void> writeContestBoard(
      @Authenticated Long memberId,
      @PathVariable ContestType contestType,
      @Valid @RequestPart("form") SaveContestBoardDto form,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {

    SaveContestBoardDto saveContestBoardDto =
        SaveContestBoardDto.builder()
            .contestFieldId(form.getContestFieldId())
            .title(form.getTitle())
            .content(form.getContent())
            .association(form.getAssociation())
            .topic(form.getTopic())
            .dateContestStart(form.getDateContestStart())
            .dateContestEnd(form.getDateContestEnd())
            .files(files)
            .build();
    Long newContestBoardId =
        contestBoardService.writeContestBoard(memberId, contestType, saveContestBoardDto);

    // 뒤에 추가 URI 생성
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{boardId}")
            .buildAndExpand(newContestBoardId)
            .toUri();

    return ResponseEntity.created(location).build();
  }

  @Operation(summary = "공모전 게시글 수정")
  @PostMapping(
      path = "/contest/{contestType}/{boardId}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("@boardSecurityChecker.boardWriterOnly(#boardId) or hasRole('VICE_CHIEF')")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200"),
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
  public ResponseEntity<Long> updateContestBoard(
      @Authenticated Long memberId,
      @PathVariable ContestType contestType,
      @PathVariable Long boardId,
      @Valid @RequestPart SaveContestBoardDto form,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {

    SaveContestBoardDto saveContestBoardDto =
        SaveContestBoardDto.builder()
            .contestFieldId(form.getContestFieldId())
            .title(form.getTitle())
            .content(form.getContent())
            .association(form.getAssociation())
            .topic(form.getTopic())
            .dateContestStart(form.getDateContestStart())
            .dateContestEnd(form.getDateContestEnd())
            .files(files)
            .build();

    contestBoardService.updateContestBoard(boardId, contestType, saveContestBoardDto);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "공모전 게시글 삭제")
  @DeleteMapping("contest/{contestType}/{boardId}")
  @PreAuthorize("@boardSecurityChecker.boardWriterOnly(#boardId) or hasRole('VICE_CHIEF')")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "204"),
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
  public ResponseEntity<ContestBoardDto> deleteContestBoard(
      @Authenticated Long memberId,
      @PathVariable ContestType contestType,
      @PathVariable Long boardId) {

    contestBoardService.deleteContestBoard(boardId);

    return ResponseEntity.noContent().build();
  }
}
