package com.inhabas.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.dto.contest.DetailContestBoardDto;
import com.inhabas.api.dto.contest.ListContestBoardDto;
import com.inhabas.api.dto.contest.SaveContestBoardDto;
import com.inhabas.api.dto.contest.UpdateContestBoardDto;
import com.inhabas.api.service.contest.ContestBoardService;
import com.inhabas.security.annotataion.WithMockJwtAuthenticationToken;
import com.inhabas.testConfig.DefaultWebMvcTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DefaultWebMvcTest(ContestBoardController.class)
public class ContestBoardControllerTest {

    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ContestBoardController contestBoardController;

    @MockBean
    private ContestBoardService contestBoardService;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(contestBoardController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @DisplayName("공모전 게시글 저장을 요청한다.")
    @Test
    @WithMockJwtAuthenticationToken
    public void addNewContestBoard() throws Exception {
        //given
        SaveContestBoardDto saveContestBoardDto = new SaveContestBoardDto("title", "contents", "association", "topic", LocalDate.of(2022, 1, 1), LocalDate.of(9022, 3, 26));
        given(contestBoardService.write(any(), any(SaveContestBoardDto.class))).willReturn(1);

        // when
        mvc.perform(post("/contest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveContestBoardDto)))
                .andExpect(status().isCreated())
                .andExpect(content().string("1"));
    }

    @DisplayName("공모전 게시글 수정을 요청한다.")
    @Test
    @WithMockJwtAuthenticationToken
    public void updateContestBoard() throws Exception{
        //given
        UpdateContestBoardDto updateContestBoardDto = new UpdateContestBoardDto(1, "수정된 제목", "수정된 내용", "수정된 협회기관명", "수정된 공모전 주제", LocalDate.of(2022, 1, 1), LocalDate.of(9022, 3, 26));
        given(contestBoardService.update(any(), any(UpdateContestBoardDto.class))).willReturn(1);

        // when
        mvc.perform(put("/contest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateContestBoardDto)))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @DisplayName("공모전 게시글 삭제를 요청한다.")
    @Test
    public void deleteContestBoard() throws Exception{
        //given
        doNothing().when(contestBoardService).delete(anyInt());

        // when
        mvc.perform(delete("/contest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "1"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("공모전 게시판 목록 조회를 요청한다.")
    @Test
    public void getContestBoardList() throws Exception {
        PageRequest pageable = PageRequest.of(0,10, Sort.Direction.DESC, "id");

        List<ListContestBoardDto> results = new ArrayList<>();
        results.add(new ListContestBoardDto("title1", "contents1", LocalDate.of(2022,1,1), LocalDate.of(2022, 1, 29)));
        results.add(new ListContestBoardDto("title2", "contents2", LocalDate.of(2022,1,1), LocalDate.of(2022, 1, 29)));
        results.add(new ListContestBoardDto("title3", "contents3", LocalDate.of(2022,1,1), LocalDate.of(2022, 1, 29)));

        Page<ListContestBoardDto> expectedContestBoardDto = new PageImpl<>(results, pageable, results.size());

        given(contestBoardService.getBoardList(anyInt(), any())).willReturn(expectedContestBoardDto);

        // when
        String responseBody = mvc.perform(get("/contest/all")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("menuId", "9")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "DESC")
                        .param("properties", "id"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        // then
        assertThat(responseBody).isEqualTo(objectMapper.writeValueAsString(expectedContestBoardDto));
    }

    @DisplayName("공모전 게시글 단일 조회를 요청한다.")
    @Test
    public void getContestBoardDetail() throws Exception{
        //given
        DetailContestBoardDto contestBoardDto = new DetailContestBoardDto(1, "mingyeom", "title", "contents", "association","topic",  LocalDate.of(2022,1,1), LocalDate.of(2022, 1, 29), LocalDateTime.now(), null);
        given(contestBoardService.getBoard(anyInt())).willReturn(contestBoardDto);

        // when
        String responseBody = mvc.perform(get("/contest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        // then
        assertThat(responseBody).isEqualTo(objectMapper.writeValueAsString(contestBoardDto));

    }

    @DisplayName("공모전 게시글 작성 시 Title의 길이가 범위를 초과해 오류 발생")
    @Test
    @WithMockJwtAuthenticationToken
    public void TitleIsTooLongError() throws Exception {
        //given
        SaveContestBoardDto saveContestBoardDto = new SaveContestBoardDto("title".repeat(20)+ ".", "contents", "association", "topic", LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1,26));


        // when
        String errorMessage = Objects.requireNonNull(
                mvc.perform(post("/contest")
                        .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(saveContestBoardDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResolvedException())
                .getMessage();

        // then
        assertThat(errorMessage).isNotBlank();
        assertThat(errorMessage).contains("제목은 최대 100자입니다.");
    }

    @DisplayName("공모전 게시글 작성 시 Contents가 null인 경우 오류 발생")
    @Test
    @WithMockJwtAuthenticationToken
    public void ContentIsNullError() throws Exception {
        //given
        SaveContestBoardDto saveContestBoardDto = new SaveContestBoardDto("title",  " ", "association", "topic", LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1,26));

        // when
        String errorMessage = Objects.requireNonNull(
                mvc.perform(post("/contest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveContestBoardDto)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResolvedException())
                .getMessage();

        // then
        assertThat(errorMessage).isNotBlank();
        assertThat(errorMessage).contains("본문을 입력하세요.");
    }

}
