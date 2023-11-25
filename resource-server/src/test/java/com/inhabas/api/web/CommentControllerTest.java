package com.inhabas.api.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.inhabas.api.domain.comment.dto.CommentUpdateDto;
import com.inhabas.api.domain.comment.usecase.CommentServiceImpl;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;
import java.time.LocalDateTime;
import java.util.List;
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

@NoSecureWebMvcTest(CommentController.class)
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
                new CommentDetailDto(1, "contents1", new StudentId("12171652"), "유동현", "간호학과", LocalDateTime.now()),
                new CommentDetailDto(2, "contents2", new StudentId("12171652"), "유동현", "간호학과", LocalDateTime.now()),
                new CommentDetailDto(3, "contents3", new StudentId("12171652"), "유동현", "간호학과", LocalDateTime.now())
        });
        given(commentService.getComments(anyInt())).willReturn(commentList);

        //when
        MvcResult result = mockMvc.perform(get("/board/3/comments"))
                .andExpect(status().isOk())
                .andReturn();

        //then
        String actualResponseBody = result.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(commentList));
    }

//    @DisplayName("댓글 추가 요청")
//    @Test
//    void createNewComment() throws Exception {
//        //given
//        String jsonRequest = "{\"writerId\":12171652,\"contents\":\"아싸 1등\",\"boardId\":13}";
//        Integer newCommentId = 1;
//        given(commentService.create(any(), any())).willReturn(newCommentId);
//
//        //when
//        String responseBody = mockMvc.perform(post("/comment")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonRequest))
//                .andExpect(status().isCreated())
//                .andReturn()
//                .getResponse().getContentAsString();
//
//        //then
//        assertThat(responseBody).isNotBlank();
//        assertThat(responseBody).isEqualTo(String.valueOf(newCommentId));
//    }

//    @DisplayName("대댓글 추가 요청")
//    @Test
//    void createNewReply() throws Exception {
//        //given
//        String jsonRequest = "{\"writerId\":12171652,\"contents\":\"아싸 1등\",\"boardId\":13, \"parentCommentId\":1}";
//        Integer newReplyId = 2;
//        given(commentService.create(any(), any())).willReturn(newReplyId);
//
//        //when
//        String responseBody = mockMvc.perform(post("/comment")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonRequest))
//                .andExpect(status().isCreated())
//                .andReturn()
//                .getResponse().getContentAsString();
//
//        //then
//        assertThat(responseBody).isNotBlank();
//        assertThat(responseBody).isEqualTo(String.valueOf(newReplyId));
//    }

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

//    @DisplayName("정상적인 댓글 수정 요청")
//    @Test
//    void updateComment() throws Exception {
//        //given
//        String jsonRequest = "{\"commentId\":1, \"writerId\":12171652,\"contents\":\"1등이 아니네,,,\",\"boardId\":12}";
//        given(commentService.update(any(), any())).willReturn(1);
//
//        String responseBody = mockMvc.perform(put("/comment")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonRequest))
//                .andExpect(status().isNoContent())
//                .andReturn()
//                .getResponse().getContentAsString();
//
//        //then
//        assertThat(responseBody).isBlank();
//    }


    @DisplayName("500자 이상의 댓글 수정은 유효성 검사 실패 후 400 반환")
    @Test
    void tryToUpdateTooLongContents() throws Exception {
        //given
        String tooLongContents = "-".repeat(500);
        CommentUpdateDto param = new CommentUpdateDto(1, tooLongContents);

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
        CommentUpdateDto param = new CommentUpdateDto(1, "1등이 아니네,,,");
        given(commentService.update(any(), any())).willThrow(RuntimeException.class);

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
        doNothing().when(commentService).delete(any(), any());

        //when
        mockMvc.perform(delete("/comment/1"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("다른 사람의 댓글을 삭제 요청")
    @Test
    void illegalTryToDeleteComment() throws Exception {
        //given
        doThrow(RuntimeException.class).when(commentService).delete(any(), any());

        //when
        mockMvc.perform(delete("/comment/1"))
                .andExpect(status().isBadRequest());

    }
}
