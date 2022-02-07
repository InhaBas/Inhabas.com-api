package com.inhabas.api.controller;

import com.inhabas.api.dto.contest.DetailContestBoardDto;
import com.inhabas.api.dto.contest.ListContestBoardDto;
import com.inhabas.api.dto.contest.SaveContestBoardDto;
import com.inhabas.api.dto.contest.UpdateContestBoardDto;
import com.inhabas.api.service.contest.ContestBoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/board/contest")
@RequiredArgsConstructor
public class ContestBoardController {

    private final ContestBoardService boardService;

    @Operation(description = "공모전 게시판의 게시글 단일 조회")
    @GetMapping
    public DetailContestBoardDto getBoard(@RequestParam Integer id) {
        return boardService.getBoard(id);
    }

    @Operation(description = "공모전 게시판의 모든 게시글 조회")
    @GetMapping("all")
    public Page<ListContestBoardDto> getBoardList(Integer menuId, Pageable pageable) {
        return boardService.getBoardList(menuId, pageable);
    }

    @Operation(description = "공모전 게시판 게시글 추가")
    @PostMapping
    public Integer addBoard(@Valid @RequestBody SaveContestBoardDto dto) {
        return boardService.write(dto);
    }

    @Operation(description = "공모전 게시판의 게시글 수정")
    @PutMapping
    public Integer updateBoard(@Valid @RequestBody UpdateContestBoardDto dto) {
        return boardService.update(dto);
    }

    @Operation(description = "공모전 게시판의 게시글 삭제")
    @DeleteMapping
    public void deleteBoard(@RequestParam Integer id) {
        boardService.delete(id);
    }
}
