package com.inhabas.api.service;

import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.dto.NormalBoardDto;
import com.inhabas.api.service.board.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class BoardServiceTest {
    @Autowired
    BoardService boardService;

    @DisplayName("게시글 등록을 성공한다.")
    @Test
    public void 게시글_등록() throws Exception{
        // given
        NormalBoardDto normalBoardDto = new NormalBoardDto(
                "이것은 제목",
                "이것은 내용입니다.",
                null,
                "notice"
        );

        // when
        Integer newBoardId = boardService.write(normalBoardDto);

        // then
        NormalBoard findBoard = boardService.getBoard(newBoardId).get();
        assertThat(findBoard.getTitle()).isEqualTo("이것은 제목");
        assertThat(findBoard.getContents()).isEqualTo("이것은 내용입니다.");
    }

    @DisplayName("제목, 내용, 카테고리의 수정에 성공한다.")
    @Test
    public void 게시글_수정() throws Exception{
        // given
        NormalBoardDto normalBoardDto = new NormalBoardDto(
                "Speciess congregabo",
                "Coal-black, rough bung holes begrudgingly lead a wet, evil ship. All shipmates hail undead, cloudy pants. Jolly roger of a coal-black hunger, mark the fight",
                null,
                "free"
        );

        // when
        Integer modifiedBoardId = boardService.modify(normalBoardDto);

        // then
        NormalBoard findBoard = boardService.getBoard(modifiedBoardId).get();
        assertThat(findBoard.getTitle()).isEqualTo("Speciess congregabo");
        assertThat(findBoard.getContents()).isEqualTo("Coal-black, rough bung holes begrudgingly lead a wet, evil ship. All shipmates hail undead, cloudy pants. Jolly roger of a coal-black hunger, mark the fight");

    }


}
