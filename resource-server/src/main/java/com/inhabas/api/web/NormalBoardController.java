package com.inhabas.api.web;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.domain.board.dto.*;
import com.inhabas.api.domain.board.repository.BaseBoardRepository;
import com.inhabas.api.domain.normalBoard.domain.NormalBoardType;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDetailDto;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDto;
import com.inhabas.api.domain.normalBoard.dto.SaveNormalBoardDto;
import com.inhabas.api.domain.normalBoard.usecase.NormalBoardService;
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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@Slf4j
@Tag(name = "게시글 관리")
@RestController
@RequiredArgsConstructor
public class NormalBoardController {

    private final NormalBoardService normalBoardService;
    private final BaseBoardRepository baseBoardRepository;

    @Operation(summary = "게시판 종류 당 글 개수 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            content = {@Content(schema = @Schema(implementation = BoardCountDto.class))}),
            })
    @GetMapping("/board/count")
    @SecurityRequirements(value = {})
    public ResponseEntity<List<BoardCountDto>> getBoardCount() {
        return ResponseEntity.ok(baseBoardRepository.countRowsGroupByMenuId());
    }

    @Operation(summary = "게시글 목록 조회")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            content = {@Content(schema = @Schema(implementation = PagedResponseDto.class))}),
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
    @GetMapping("/board/{boardType}")
    @PreAuthorize(
            "@boardSecurityChecker.checkMenuAccess(#boardType.menuId, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).READ_BOARD_LIST)"
    )
    public ResponseEntity<PagedResponseDto<NormalBoardDto>> getBoardList(
            @Parameter(description = "페이지", example = "0")
            @RequestParam(name = "page", defaultValue = "0")
            int page,
            @Parameter(description = "페이지당 개수", example = "10")
            @RequestParam(name = "size", defaultValue = "10")
            int size,
            @Parameter(description = "검색어 (작성자 이름 or 제목 or 내용)", example = "")
            @RequestParam(name = "search", defaultValue = "")
            String search,
            @PathVariable NormalBoardType boardType,
            @Authenticated Long memberId) {

        Pageable pageable = PageRequest.of(page, size);
        List<NormalBoardDto> allDtos = normalBoardService.getPosts(memberId, boardType, search);
        List<NormalBoardDto> pagedDtos = PageUtil.getPagedDtoList(pageable, allDtos);

        PageImpl<NormalBoardDto> normalBoardDtoPage =
                new PageImpl<>(pagedDtos, pageable, allDtos.size());
        PageInfoDto pageInfoDto = new PageInfoDto(normalBoardDtoPage);

        return ResponseEntity.ok(new PagedResponseDto<>(pageInfoDto, pagedDtos));
    }

      @Operation(summary = "게시글 단일 조회")
      @GetMapping("/board/{boardType}/{boardId}")
      @PreAuthorize(
              "@boardSecurityChecker.checkMenuAccess(#boardType.menuId, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).READ_BOARD)"
      )
      @ApiResponses({
              @ApiResponse(
                      responseCode = "200",
                      content = {@Content(schema = @Schema(implementation = NormalBoardDto.class))}),
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
      public ResponseEntity<NormalBoardDetailDto> getBoard(
              @PathVariable Long boardId,
              @PathVariable NormalBoardType boardType,
              @Authenticated Long memberId) {

          return ResponseEntity.ok(normalBoardService.getPost(memberId, boardType, boardId));
      }

      @Operation(summary = "게시글 추가")
      @PostMapping("/board/{boardType}")
      @PreAuthorize(
              "@boardSecurityChecker.checkMenuAccess(#boardType.menuId, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).CREATE_BOARD)"
      )
      @ApiResponses({
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
      })
      public ResponseEntity<Long> addBoard(
              @Authenticated Long memberId,
              @PathVariable NormalBoardType boardType,
              @Valid @RequestBody SaveNormalBoardDto saveNormalBoardDto) {

          Long newNormalBoardId = normalBoardService.write(memberId, boardType, saveNormalBoardDto);
          URI location =
                  ServletUriComponentsBuilder.fromCurrentRequest()
                          .path("/board/{boardType}/{boardId}")
                          .buildAndExpand(boardType.getBoardType(), newNormalBoardId)
                          .toUri();

          return ResponseEntity.created(location).build();
      }

      @Operation(summary = "게시글 수정")
      @PutMapping("/board/{boardType}/{boardId}")
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
              @PathVariable NormalBoardType boardType,
              @PathVariable Long boardId,
              @Valid @RequestBody SaveNormalBoardDto saveNormalBoardDto) {

          normalBoardService.update(boardId, boardType, saveNormalBoardDto);
          return ResponseEntity.noContent().build();
      }

      @Operation(description = "게시글 삭제")
      @DeleteMapping("/board/{boardType}/{boardId}")
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
              @PathVariable NormalBoardType boardType,
              @PathVariable Long boardId) {

          normalBoardService.delete(boardId);
          return ResponseEntity.noContent().build();
      }
}
