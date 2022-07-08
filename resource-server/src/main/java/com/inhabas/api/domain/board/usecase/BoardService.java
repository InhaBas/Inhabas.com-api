package com.inhabas.api.domain.board.usecase;

import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.board.dto.BoardDto;
import com.inhabas.api.domain.board.dto.SaveBoardDto;
import com.inhabas.api.domain.board.dto.UpdateBoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {
    Integer write(MemberId memberId, SaveBoardDto saveBoardDto);

    Integer update(MemberId memberId, UpdateBoardDto updateBoardDto);

    void delete(Integer id);

    BoardDto getBoard(Integer boardId);

    Page<BoardDto> getBoardList(MenuId menuId, Pageable pageable);

}
