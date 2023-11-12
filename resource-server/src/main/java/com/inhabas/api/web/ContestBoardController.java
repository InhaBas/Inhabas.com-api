package com.inhabas.api.web;

import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.web.argumentResolver.Authenticated;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.contest.dto.DetailContestBoardDto;
import com.inhabas.api.domain.contest.dto.ListContestBoardDto;
import com.inhabas.api.domain.contest.dto.SaveContestBoardDto;
import com.inhabas.api.domain.contest.dto.UpdateContestBoardDto;
import com.inhabas.api.domain.contest.usecase.ContestBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "공모전 관리")
@RestController
@RequiredArgsConstructor
public class ContestBoardController {

    private final ContestBoardService boardService;

    @Operation(description = "공모전 게시판의 게시글 단일 조회")
    @GetMapping("/contest/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "잘못된 게시글 조회 URL 요청"),
            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
    })
    public ResponseEntity<DetailContestBoardDto> getBoard(@PathVariable Integer id) {

        return new ResponseEntity<>(boardService.getBoard(id), HttpStatus.OK);
    }

    @Operation(description = "공모전 게시판의 모든 게시글 조회")
    @GetMapping("/contests")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "잘못된 게시글 목록 조회 URL 요청"),
            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
    })
    public ResponseEntity<Page<ListContestBoardDto>> getBoardList(
            @RequestParam("menu_id") MenuId menuId,
            @PageableDefault(size = 8, direction = Direction.DESC, sort = "created") Pageable pageable) {

        return new ResponseEntity<>(boardService.getBoardList(menuId, pageable), HttpStatus.OK);
    }

    @Operation(description = "공모전 게시판 게시글 추가")
    @PostMapping("/contest")
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", description = "잘못된 게시글 폼 데이터 요청"),
            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
    })
    public ResponseEntity<Integer> addBoard(@Authenticated StudentId studentId, @Valid @RequestBody SaveContestBoardDto dto) {
        return new ResponseEntity<>(boardService.write(studentId, dto), HttpStatus.CREATED);
    }

    @Operation(description = "공모전 게시판의 게시글 수정")
    @PutMapping("/contest")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "잘못된 게시글 폼 데이터 요청"),
            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
    })
    public ResponseEntity<Integer> updateBoard(
            @Authenticated StudentId studentId, @Valid @RequestBody UpdateContestBoardDto dto) {

        return new ResponseEntity<>(boardService.update(studentId, dto), HttpStatus.OK);
    }

    @Operation(description = "공모전 게시판의 게시글 삭제")
    @DeleteMapping("/contest/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 게시글 삭제 요청"),
            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
    })
    public ResponseEntity<?> deleteBoard(
            @Authenticated StudentId studentId, @PathVariable Integer id) {

        boardService.delete(studentId, id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
