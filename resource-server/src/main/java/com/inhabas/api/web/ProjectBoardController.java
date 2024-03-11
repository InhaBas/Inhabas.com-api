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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.domain.board.dto.BoardCountDto;
import com.inhabas.api.domain.board.repository.BaseBoardRepository;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDetailDto;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDto;
import com.inhabas.api.domain.normalBoard.dto.SaveNormalBoardDto;
import com.inhabas.api.domain.project.ProjectBoardType;
import com.inhabas.api.domain.project.usecase.ProjectBoardService;
import com.inhabas.api.global.dto.PageInfoDto;
import com.inhabas.api.global.dto.PagedPinnedResponseDto;
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
@Tag(name = "게시글 관리")
@RestController
@RequiredArgsConstructor
public class ProjectBoardController {

  private final ProjectBoardService projectBoardService;
  private final BaseBoardRepository baseBoardRepository;

  @Operation(summary = "게시판 종류 당 글 개수 조회")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = BoardCountDto.class))}),
      })
  @GetMapping("/project/count")
  @SecurityRequirements(value = {})
  public ResponseEntity<List<BoardCountDto>> getBoardCount() {
    return ResponseEntity.ok(baseBoardRepository.countRowsGroupByMenuName(5));
  }

  @Operation(summary = "게시글 목록 조회")
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
  @GetMapping("/project/{projectBoardType}")
  @PreAuthorize(
      "@boardSecurityChecker.checkMenuAccess(#projectBoardType.menuId, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).READ_BOARD_LIST)")
  public ResponseEntity<PagedPinnedResponseDto<NormalBoardDto>> getBoardList(
      @Parameter(description = "페이지", example = "0")
          @RequestParam(name = "page", defaultValue = "0")
          int page,
      @Parameter(description = "페이지당 개수", example = "10")
          @RequestParam(name = "size", defaultValue = "10")
          int size,
      @Parameter(description = "검색어 (작성자 이름 or 제목 or 내용)", example = "")
          @RequestParam(name = "search", defaultValue = "")
          String search,
      @PathVariable ProjectBoardType projectBoardType) {

    Pageable pageable = PageRequest.of(page, size);
    List<NormalBoardDto> allDtoList = projectBoardService.getPosts(projectBoardType, search);
    List<NormalBoardDto> pinnedDtoList = projectBoardService.getPinned(projectBoardType);
    List<NormalBoardDto> pagedDtoList = PageUtil.getPagedDtoList(pageable, allDtoList);

    PageImpl<NormalBoardDto> normalBoardDtoPage =
        new PageImpl<>(pagedDtoList, pageable, allDtoList.size());
    PageInfoDto pageInfoDto = new PageInfoDto(normalBoardDtoPage);

    return ResponseEntity.ok(
        new PagedPinnedResponseDto<>(pageInfoDto, pinnedDtoList, pagedDtoList));
  }

  @Operation(summary = "게시글 단일 조회")
  @GetMapping("/project/{projectBoardType}/{boardId}")
  @PreAuthorize(
      "@boardSecurityChecker.checkMenuAccess(#projectBoardType.menuId, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).READ_BOARD)")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        content = {@Content(schema = @Schema(implementation = NormalBoardDto.class))}),
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
  public ResponseEntity<NormalBoardDetailDto> getBoard(
      @PathVariable Long boardId,
      @PathVariable ProjectBoardType projectBoardType,
      @Authenticated Long memberId) {

    return ResponseEntity.ok(projectBoardService.getPost(memberId, projectBoardType, boardId));
  }

  @Operation(summary = "게시글 추가")
  @PostMapping(path = "/project/{projectBoardType}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize(
      "@boardSecurityChecker.checkMenuAccess(#projectBoardType.menuId, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).CREATE_BOARD)")
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
      @PathVariable ProjectBoardType projectBoardType,
      @Valid @RequestPart("form") SaveNormalBoardDto form,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {
    SaveNormalBoardDto saveNormalBoardDto =
        SaveNormalBoardDto.builder()
            .title(form.getTitle())
            .content(form.getContent())
            .pinOption(form.getPinOption())
            .files(files)
            .build();
    Long newNormalBoardId =
        projectBoardService.write(memberId, projectBoardType, saveNormalBoardDto);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{boardId}")
            .buildAndExpand(newNormalBoardId)
            .toUri();

    return ResponseEntity.created(location).build();
  }

  @Operation(summary = "게시글 수정")
  @PostMapping(
      value = "/project/{projectBoardType}/{boardId}",
      consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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
      @PathVariable ProjectBoardType projectBoardType,
      @PathVariable Long boardId,
      @Valid @RequestPart("form") SaveNormalBoardDto form,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {
    SaveNormalBoardDto saveNormalBoardDto =
        SaveNormalBoardDto.builder()
            .title(form.getTitle())
            .content(form.getContent())
            .pinOption(form.getPinOption())
            .files(files)
            .build();
    projectBoardService.update(boardId, projectBoardType, saveNormalBoardDto);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "게시글 삭제")
  @DeleteMapping("/project/{projectBoardType}/{boardId}")
  @ApiResponses({
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
  @PreAuthorize("@boardSecurityChecker.boardWriterOnly(#boardId) or hasRole('VICE_CHIEF')")
  public ResponseEntity<?> deleteBoard(
      @Authenticated Long memberId,
      @PathVariable ProjectBoardType projectBoardType,
      @PathVariable Long boardId) {

    projectBoardService.delete(boardId);
    return ResponseEntity.noContent().build();
  }
}
