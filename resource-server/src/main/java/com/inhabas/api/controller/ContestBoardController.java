package com.inhabas.api.controller;

import com.inhabas.api.web.argumentResolver.Authenticated;
import com.inhabas.api.domain.member.MemberId;
import com.inhabas.api.dto.contest.DetailContestBoardDto;
import com.inhabas.api.dto.contest.ListContestBoardDto;
import com.inhabas.api.dto.contest.SaveContestBoardDto;
import com.inhabas.api.dto.contest.UpdateContestBoardDto;
import com.inhabas.api.service.contest.ContestBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/contest")
@RequiredArgsConstructor
public class ContestBoardController {

    private final ContestBoardService boardService;

    @Operation(description = "공모전 게시판의 게시글 단일 조회")
    @GetMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "잘못된 게시글 조회 URL 요청"),
            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
    })
    public ResponseEntity<DetailContestBoardDto> getBoard(@RequestParam Integer id) {
        return new ResponseEntity<>(boardService.getBoard(id), HttpStatus.OK);
    }

    @Operation(description = "공모전 게시판의 모든 게시글 조회")
    @GetMapping("all")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "잘못된 게시글 목록 조회 URL 요청"),
            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
    })
    public ResponseEntity<Page<ListContestBoardDto>> getBoardList(@RequestParam Integer menuId, Pageable pageable) {
        return new ResponseEntity<>(boardService.getBoardList(menuId, pageable), HttpStatus.OK);
    }

    @Operation(description = "공모전 게시판 게시글 추가")
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "201"),
            @ApiResponse(responseCode = "400", description = "잘못된 게시글 폼 데이터 요청"),
            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
    })
    public ResponseEntity<Integer> addBoard(@Authenticated MemberId memberId, @Valid @RequestBody SaveContestBoardDto dto) {
        return new ResponseEntity<>(boardService.write(memberId, dto), HttpStatus.CREATED);
    }

    @Operation(description = "공모전 게시판의 게시글 수정")
    @PutMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "잘못된 게시글 폼 데이터 요청"),
            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
    })
    public ResponseEntity<Integer> updateBoard(@Authenticated MemberId memberId , @Valid @RequestBody UpdateContestBoardDto dto) {
        return new ResponseEntity<>( boardService.update(memberId, dto), HttpStatus.OK);
    }

    @Operation(description = "공모전 게시판의 게시글 삭제")
    @DeleteMapping
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 게시글 삭제 요청"),
            @ApiResponse(responseCode = "403", description = "클라이언트의 접근 권한이 없음")
    })
    public ResponseEntity<?> deleteBoard(@RequestParam Integer id) {
        boardService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
