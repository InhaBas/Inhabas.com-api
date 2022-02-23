package com.inhabas.api.controller;

import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.dto.board.BoardDto;
import com.inhabas.api.dto.board.SaveBoardDto;
import com.inhabas.api.dto.board.UpdateBoardDto;
import com.inhabas.api.service.board.BoardService;
import com.inhabas.api.service.member.MemberService;
import com.inhabas.api.service.menu.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NormalBoardController.class)
public class NormalBoardControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    NormalBoardController normalBoardController;

    @MockBean
    BoardService boardService;

    @MockBean
    MemberService memberService;

    @MockBean
    NormalBoard normalBoard;

    @MockBean
    MenuService menuService;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(normalBoardController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers(new ViewResolver() {
                    @Override
                    public View resolveViewName(String viewName, Locale locale) throws Exception {
                        return new MappingJackson2JsonView();
                    }
                })
                .build();
    }

    @DisplayName("게시글 저장을 요청한다.")
    @Test
    public void addNewBoard() throws Exception {
        //given
        SaveBoardDto saveBoardDto = new SaveBoardDto("This is title", "This is contents", 1, 12201863);
        given(boardService.write(any(SaveBoardDto.class))).willReturn(1);

        // when
        mvc.perform(post("/board/normal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("menuId","2")
                        .content(objectMapper.writeValueAsString(saveBoardDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @DisplayName("게시글 수정을 요청한다.")
    @Test
    public void updateBoard() throws Exception{
        //given
        UpdateBoardDto updateBoardDto = new UpdateBoardDto(1, "수정된 제목", "수정된 내용");
        given(boardService.update(any(UpdateBoardDto.class))).willReturn(1);

        // when
        mvc.perform(put("/board/normal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("menuId","2")
                        .content(objectMapper.writeValueAsString(updateBoardDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @DisplayName("게시글 삭제를 요청한다.")
    @Test
    public void deleteBoard() throws Exception{
        //given
        doNothing().when(boardService).delete(anyInt());

        // when
        mvc.perform(delete("/board/normal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("menuId","2")
                        .param("id", "1"))
                .andExpect(status().isOk());
    }

    @DisplayName("게시글 목록 조회를 요청한다.")
    @Test
    public void getBoardList() throws Exception {
        PageRequest pageable = PageRequest.of(0,10, Sort.Direction.DESC, "id");

        List<Object> results = new ArrayList<>();
        results.add((Object)(new BoardDto(1, "Shown Title1", null, "Mingyeom", 2, LocalDateTime.now(), null)));
        results.add((Object)(new BoardDto(2, "Shown Title2", null, "Mingyeom", 2, LocalDateTime.now(), null)));
        results.add((Object)(new BoardDto(3, "Shown Title3", null, "Mingyeom", 2, LocalDateTime.now(), null)));

        Page<Object> expectedBoardDto = new PageImpl<>(results,pageable, results.size());

        given(boardService.getBoardList(anyInt(), any())).willReturn(expectedBoardDto);

        // when
        String responseBody = mvc.perform(get("/board/normal/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("menuId", "2")
                        .param("page", "2")
                        .param("size", "1")
                        .param("sort", "ASC")
                        .param("properties", "id"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        // then
        assertThat(responseBody).isEqualTo(objectMapper.writeValueAsString(expectedBoardDto));
    }

    @DisplayName("게시글 단일 조회를 요청한다.")
    @Test
    public void getBoardDetail() throws Exception{
        //given
        BoardDto boardDto = new BoardDto(1, "Shown Title", "Shown Contents", "Mingyeom", 1, LocalDateTime.now(), null);
        given(boardService.getBoard(anyInt())).willReturn(boardDto);

        // when
        String responseBody = mvc.perform(get("/board/normal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("menuId","2")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        // then
        assertThat(responseBody).isEqualTo(objectMapper.writeValueAsString(boardDto));

    }
}
