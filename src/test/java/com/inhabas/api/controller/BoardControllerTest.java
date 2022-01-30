package com.inhabas.api.controller;

import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.dto.board.BoardDto;
import com.inhabas.api.dto.board.SaveBoardDto;
import com.inhabas.api.dto.board.UpdateBoardDto;
import com.inhabas.api.service.board.BoardServiceImpl;
import com.inhabas.api.service.member.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BoardController.class)
public class BoardControllerTest {

    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    BoardController boardController;

    @MockBean
    BoardServiceImpl boardService;

    @MockBean
    MemberServiceImpl memberService;

    @MockBean
    NormalBoard normalBoard;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(boardController)
                .build();
    }

    @DisplayName("게시글 저장을 요청한다.")
    @Test
    public void addNewBoard() throws Exception {
        //given
        SaveBoardDto saveBoardDto = new SaveBoardDto("This is title", "This is contents", 1, 12201863);
        given(boardService.write(any(SaveBoardDto.class))).willReturn(1);

        // when
        String responseBody  = mvc.perform(post("/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveBoardDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"))
                .andReturn()
                .getResponse().getContentAsString();

        // then
        assertThat(responseBody).isNotNull();
        assertThat(responseBody).isEqualTo("1");
    }

    @DisplayName("게시글 수정을 요청한다.")
    @Test
    public void updateBoard() throws Exception{
        //given
        UpdateBoardDto updateBoardDto = new UpdateBoardDto(1, "제목을 수정하였습니다.", "내용을 수정하였습니다.", 12201863);
        given(boardService.update(any(UpdateBoardDto.class))).willReturn(1);

        // when
        boardController.updateBoard(updateBoardDto);
        String responseBody = mvc.perform(put("/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBoardDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        // then
        assertThat(responseBody).isNotNull();
        assertThat(responseBody).isEqualTo("1");
    }

    @DisplayName("게시글 삭제를 요청한다.")
    @Test
    public void deleteBoard() throws Exception{
        //given
        doNothing().when(boardService).delete(anyInt());

        // when
        mvc.perform(delete("/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("게시글 목록 조회를 요청한다.")
    @Test
    public void getBoardList() throws Exception{
        //given


        // when

        // then
    }

    @DisplayName("게시글 단일 조회를 요청한다.")
    @Test
    public void getBoardDetail() throws Exception{
        //given
        BoardDto boardDto = new BoardDto(1, "Shown Title", "Shown Contents", "Mingyeom", 1, LocalDateTime.now(), null);
        given(boardService.getBoard(anyInt())).willReturn(Optional.of(boardDto));

        // when
        String responseBody = mvc.perform(get("/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        // then
        assertThat(responseBody).isNotNull();
        assertThat(responseBody).isEqualTo(objectMapper.writeValueAsString(boardDto));
    }

}
