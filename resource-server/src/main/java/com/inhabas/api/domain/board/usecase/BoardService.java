package com.inhabas.api.domain.board.usecase;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.board.dto.BoardDto;
import com.inhabas.api.domain.board.dto.SaveBoardDto;
import com.inhabas.api.domain.board.dto.UpdateBoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    Integer write(StudentId studentId, SaveBoardDto saveBoardDto);

    Integer update(StudentId studentId, UpdateBoardDto updateBoardDto);

    void delete(StudentId studentId, Integer boardId);

    BoardDto getBoard(Integer boardId);

    Page<BoardDto> getBoardList(MenuId menuId, Pageable pageable);

}
