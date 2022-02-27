package com.inhabas.api.controller;

import com.inhabas.api.dto.contest.DetailContestBoardDto;
import com.inhabas.api.dto.contest.ListContestBoardDto;
import com.inhabas.api.dto.contest.SaveContestBoardDto;
import com.inhabas.api.dto.contest.UpdateContestBoardDto;
import com.inhabas.api.security.argumentResolver.Authenticated;
import com.inhabas.api.service.contest.ContestBoardService;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity<DetailContestBoardDto> getBoard(@RequestParam Integer id) {
        return new ResponseEntity<>(boardService.getBoard(id), HttpStatus.OK);
    }

    @Operation(description = "공모전 게시판의 모든 게시글 조회")
    @GetMapping("all")
    public ResponseEntity<Page<ListContestBoardDto>> getBoardList(@RequestParam Integer menuId, Pageable pageable) {
        return new ResponseEntity<>(boardService.getBoardList(menuId, pageable), HttpStatus.OK);
    }

    @Operation(description = "공모전 게시판 게시글 추가")
    @PostMapping
    public ResponseEntity<Integer> addBoard(@Authenticated Integer memberId, @Valid @RequestBody SaveContestBoardDto dto) {
        return new ResponseEntity<>(boardService.write(memberId, dto), HttpStatus.CREATED);
    }

    @Operation(description = "공모전 게시판의 게시글 수정")
    @PutMapping
    public ResponseEntity<Integer> updateBoard(@Authenticated Integer memberId , @Valid @RequestBody UpdateContestBoardDto dto) {
        return new ResponseEntity<>( boardService.update(memberId, dto), HttpStatus.OK);
    }

    @Operation(description = "공모전 게시판의 게시글 삭제")
    @DeleteMapping
    public ResponseEntity deleteBoard(@RequestParam Integer id) {
        boardService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
