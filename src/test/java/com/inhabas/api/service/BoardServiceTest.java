package com.inhabas.api.service;

import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.board.NormalBoardRepository;
import com.inhabas.api.dto.board.BoardDto;
import com.inhabas.api.dto.board.SaveBoardDto;
import com.inhabas.api.service.board.BoardService;
import com.inhabas.api.service.board.BoardServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {


    @InjectMocks
    BoardServiceImpl boardService;

    @Mock
    NormalBoardRepository boardRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){ // mock 객체 생성
        mockMvc = MockMvcBuilders.standaloneSetup(boardService).build();
    }

    @Test
    public void 게시판_생성() {
        //given
        SaveBoardDto saveBoardDto = new SaveBoardDto();
        saveBoardDto.setTitle("title");
        saveBoardDto.setContents("contents");

        NormalBoard normalBoard = new NormalBoard("title", "contents");


        when(boardRepository.save(any())).thenReturn(normalBoard);

        // when
        Integer id = boardService.write(saveBoardDto);

        // then
        verify(boardRepository).save(any());
        assertThat(normalBoard.getId()).isEqualTo(id);
    }

    @Test
    public void 게시판_리스트_조회() {
        //given
        PageRequest pageable = PageRequest.of(0,10, Sort.Direction.ASC, "created");

        BoardDto boardDto1 = new BoardDto(1, "title", "contents", "mingyeom",1, LocalDateTime.now(), LocalDateTime.now() );
        BoardDto boardDto2 = new BoardDto(2, "title", "contents", "minji",1, LocalDateTime.now(), LocalDateTime.now() );

        List<BoardDto> results = new ArrayList<>();
        results.add(boardDto1);
        results.add(boardDto2);

        Page<BoardDto> boardDto = new PageImpl<> (results, pageable, results.size());

        when(boardRepository.findAllByMenuId(any(), any())).thenReturn(boardDto);

        //when
        Page<BoardDto> boardList = boardService.getBoardList(1, pageable);

        //then
        verify(boardRepository).findAllByMenuId(any(), any());
        assertThat(boardList).isEqualTo(boardDto);

    }

}
