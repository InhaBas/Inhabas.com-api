package com.inhabas.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.domain.member.MemberId;
import com.inhabas.api.dto.comment.CommentDetailDto;
import com.inhabas.api.dto.comment.CommentSaveDto;
import com.inhabas.api.dto.comment.CommentUpdateDto;
import com.inhabas.api.service.comment.CommentServiceImpl;
import com.inhabas.testConfig.DefaultWebMvcTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DefaultWebMvcTest(CommentController.class)
public class CommentControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @DisplayName("댓글 전체 조회")
    @Test
    void getCommentList() throws Exception {
        //given
        List<CommentDetailDto> commentList = List.of(new CommentDetailDto[]{
                new CommentDetailDto(1, "contents1", new MemberId(12171652), "유동현", "간호학과", LocalDateTime.now()),
                new CommentDetailDto(2, "contents2", new MemberId(12171652), "유동현", "간호학과", LocalDateTime.now()),
                new CommentDetailDto(3, "contents3", new MemberId(12171652), "유동현", "간호학과", LocalDateTime.now())
        });
        given(commentService.getComments(anyInt())).willReturn(commentList);

        //when
        MvcResult result = mockMvc.perform(get("/comment")
                        .param("boardId", String.valueOf(3)))
                .andExpect(status().isOk())
                .andReturn();

        //then
        String actualResponseBody = result.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(commentList));
    }

    @DisplayName("댓글 추가 요청")
    @Test
    void createNewComment() throws Exception {
        //given
        String jsonRequest = "{\"writerId\":12171652,\"contents\":\"아싸 1등\",\"boardId\":13}";
        Integer newCommentId = 1;
        given(commentService.create(any(CommentSaveDto.class))).willReturn(newCommentId);

        //when
        String responseBody = mockMvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse().getContentAsString();

        //then
        assertThat(responseBody).isNotBlank();
        assertThat(responseBody).isEqualTo(String.valueOf(newCommentId));
    }

    @DisplayName("500자 이상의 댓글 추가 요청은 유효성 검사 실패 후 400 반환")
    @Test
    void tryToSaveTooLongContents() throws Exception {
        //given
        String tooLongContents = "-".repeat(500);
        String jsonRequest = String.format("{\"writerId\":12171652,\"contents\":\"%s\",\"boardId\":13}", tooLongContents);

        //when
        String errorMessage = mockMvc.perform(post("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse().getContentAsString();

        assertThat(errorMessage).contains("500자 이하여야 합니다.");
    }

    @DisplayName("정상적인 댓글 수정 요청")
    @Test
    void updateComment() throws Exception {
        //given
        String jsonRequest = "{\"id\":1, \"writerId\":12171652,\"contents\":\"1등이 아니네,,,\",\"boardId\":12}";
        given(commentService.update(any(CommentUpdateDto.class))).willReturn(1);

        String responseBody = mockMvc.perform(put("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNoContent())
                .andReturn()
                .getResponse().getContentAsString();

        //then
        assertThat(responseBody).isBlank();
    }

    @DisplayName("500자 이상의 댓글 수정은 유효성 검사 실패 후 400 반환")
    @Test
    void tryToUpdateTooLongContents() throws Exception {
        //given
        String tooLongContents = "-".repeat(500);
        CommentUpdateDto param = new CommentUpdateDto(1, new MemberId(1217162), tooLongContents, 13);

        //when
        String errorMessage = mockMvc.perform(put("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(param)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse().getContentAsString();

        assertThat(errorMessage).contains("500자 이하여야 합니다.");
    }

    @DisplayName("타인의 댓글 수정 요청")
    @Test
    void illegalTryToUpdateComment() throws Exception {
        //given
        CommentUpdateDto param = new CommentUpdateDto(1, new MemberId(1217162), "1등이 아니네,,,", 12);
        given(commentService.update(any(CommentUpdateDto.class))).willThrow(RuntimeException.class);

        //when
        mockMvc.perform(put("/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(param)))
                .andExpect(status().isBadRequest());
    }

    @DisplayName("정상적인 댓글 삭제 요청")
    @Test
    void deleteComment() throws Exception {
        //given
        Integer commentIdToDelete = 1;
        doNothing().when(commentService).delete(anyInt());

        //when
        mockMvc.perform(delete("/comment")
                        .param("commentId", String.valueOf(commentIdToDelete))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @DisplayName("다른 사람의 댓글을 삭제 요청")
    @Test
    void illegalTryToDeleteComment() throws Exception {
        //given
        Integer commentIdToDelete = 1;
        doThrow(RuntimeException.class).when(commentService).delete(anyInt());

        //when
        mockMvc.perform(delete("/comment")
                        .param("commentId", String.valueOf(commentIdToDelete))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
}
