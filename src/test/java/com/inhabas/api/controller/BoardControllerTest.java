package com.inhabas.api.controller;

import com.inhabas.api.dto.board.SaveBoardDto;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        mvc.perform(post("/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveBoardDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"))
                .andDo(print());
    }

}
