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
import java.util.Map;

@RequestMapping("/board/contest")
@RequiredArgsConstructor
@RestController
public class ContestBoardController implements BoardController <DetailContestBoardDto, ListContestBoardDto> {

    private final ContestBoardService boardService;

    @Operation(description = "공모전 게시판의 게시글 단일 조회")
    @GetMapping
    public DetailContestBoardDto getBoard(@RequestParam Integer menuId, @RequestParam Integer id) {
        return boardService.getBoard(menuId, id);
    }

    @Operation(description = "공모전 게시판의 모든 게시글 조회")
    @GetMapping("/all")
    public Page<ListContestBoardDto> getBoardList(@RequestParam Integer menuId, Pageable pageable) {
        return boardService.getBoardList(menuId, pageable);
    }

    @Operation(description = "공모전 게시판 게시글 추가")
    @PostMapping
    public Integer addBoard(@RequestParam Integer menuId, @Valid @RequestBody Map<String, Object> saveBoard) {
        return boardService.write(menuId, new SaveContestBoardDto(saveBoard));
    }

    @Operation(description = "공모전 게시판의 게시글 수정")
    @PutMapping
    public Integer updateBoard(@RequestParam Integer menuId, @Valid @RequestBody Map<String, Object> updateBoard) {
        return boardService.update(menuId, new UpdateContestBoardDto(updateBoard));
    }

    @Operation(description = "공모전 게시판의 게시글 삭제")
    @DeleteMapping
    public void deleteBoard(@RequestParam Integer menuId, @RequestParam Integer id) {
        boardService.delete(id);
    }
}
