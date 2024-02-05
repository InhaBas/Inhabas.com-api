package com.inhabas.api.web;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.domain.club.dto.SaveClubActivityDto;
import com.inhabas.api.domain.contest.dto.ContestBoardDto;
import com.inhabas.api.domain.contest.dto.SaveContestBoardDto;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.inhabas.api.domain.contest.usecase.ContestBoardService;
import com.inhabas.api.global.dto.PageInfoDto;
import com.inhabas.api.global.dto.PagedMemberResponseDto;
import com.inhabas.api.global.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
      })
  @SecurityRequirements(value = {})
  @GetMapping("/contest/{contestType}")
  @PreAuthorize(
      // 공모전 게시판 MenuId : 18
      "@boardSecurityChecker.checkMenuAccess(18, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).READ_BOARD_LIST)")
  public ResponseEntity<PagedMemberResponseDto<ContestBoardDto>> getContestBoard(
      @Parameter(description = "페이지", example = "0")
          @RequestParam(name = "page", defaultValue = "0")
          int page,
      @Parameter(description = "페이지당 개수", example = "4")
          @RequestParam(name = "size", defaultValue = "4")
          int size) {
    // search 파라미터 구현 필요 -> Member 컨트롤러 확인

    Pageable pageable = PageRequest.of(page, size);
    List<ContestBoardDto> allDtos = contestBoardService.getContestBoard();
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
                          "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}")))
      })
  // 주소 전반적으로 수정해야함.
  @PostMapping(path = "/club/activity", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize(
      "@boardSecurityChecker.checkMenuAccess(18, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).CREATE_BOARD)")
  public ResponseEntity<Void> writeContestBoard(
      @Authenticated Long memberId,
      @RequestPart("title") String title,
      @RequestPart("content") String content,
      @RequestPart("association") String association,
      @RequestPart("topic") String topic,
      @RequestPart("dateContestStart")LocalDate dateContestStart,
      @RequestPart("dateContestEnd")LocalDate dateContestEnd,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {

    SaveContestBoardDto saveContestBoardDto = new SaveContestBoardDto(title, content, association, topic, dateContestStart, dateContestEnd, files);

    Long newContestBoardId = contestBoardService.writeContestBoard(memberId, saveContestBoardDto);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/club/activity/{boardId}")
            .buildAndExpand(newContestBoardId)
            .toUri();

    return ResponseEntity.created(location).build();
  }

  //
  //    @Operation(description = "공모전 게시판의 게시글 단일 조회")
  //    @GetMapping("/contest/{id}")
  //    @ApiResponses({
  //            @ApiResponse(responseCode = "200"),
  //            @ApiResponse(responseCode = "400", description = "잘못된 게시글 조회 URL 요청"),
  //            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
  //    })
  //    public ResponseEntity<DetailContestBoardDto> getBoard(@PathVariable Integer id) {
  //
  //        return new ResponseEntity<>(boardService.getBoard(id), HttpStatus.OK);
  //    }
  //
  //    @Operation(description = "공모전 게시판의 모든 게시글 조회")
  //    @GetMapping("/contests")
  //    @ApiResponses({
  //            @ApiResponse(responseCode = "200"),
  //            @ApiResponse(responseCode = "400", description = "잘못된 게시글 목록 조회 URL 요청"),
  //            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
  //    })
  //    public ResponseEntity<Page<ListContestBoardDto>> getBoardList(
  //            @RequestParam("menu_id") MenuId menuId,
  //            @PageableDefault(size = 8, direction = Direction.DESC, sort = "created") Pageable
  // pageable) {
  //
  //        return new ResponseEntity<>(boardService.getBoardList(menuId, pageable), HttpStatus.OK);
  //    }
  //
  //    @Operation(description = "공모전 게시판 게시글 추가")
  //    @PostMapping("/contest")
  //    @ApiResponses({
  //            @ApiResponse(responseCode = "201"),
  //            @ApiResponse(responseCode = "400", description = "잘못된 게시글 폼 데이터 요청"),
  //            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
  //    })
  //    public ResponseEntity<Integer> addBoard(@Authenticated StudentId studentId, @Valid
  // @RequestBody SaveContestBoardDto dto) {
  //        return new ResponseEntity<>(boardService.write(studentId, dto), HttpStatus.CREATED);
  //    }
  //
  //    @Operation(description = "공모전 게시판의 게시글 수정")
  //    @PutMapping("/contest")
  //    @ApiResponses({
  //            @ApiResponse(responseCode = "200"),
  //            @ApiResponse(responseCode = "400", description = "잘못된 게시글 폼 데이터 요청"),
  //            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
  //    })
  //    public ResponseEntity<Integer> updateBoard(
  //            @Authenticated StudentId studentId, @Valid @RequestBody UpdateContestBoardDto dto) {
  //
  //        return new ResponseEntity<>(boardService.update(studentId, dto), HttpStatus.OK);
  //    }
  //
  //    @Operation(description = "공모전 게시판의 게시글 삭제")
  //    @DeleteMapping("/contest/{id}")
  //    @ApiResponses({
  //            @ApiResponse(responseCode = "204"),
  //            @ApiResponse(responseCode = "400", description = "잘못된 게시글 삭제 요청"),
  //            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
  //    })
  //    public ResponseEntity<?> deleteBoard(
  //            @Authenticated StudentId studentId, @PathVariable Integer id) {
  //
  //        boardService.delete(studentId, id);
  //        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  //    }

}
