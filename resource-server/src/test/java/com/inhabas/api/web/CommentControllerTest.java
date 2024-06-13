package com.inhabas.api.web;

import static com.inhabas.api.auth.domain.error.ErrorCode.INVALID_INPUT_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.usecase.BoardSecurityChecker;
import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.inhabas.api.domain.comment.dto.CommentSaveDto;
import com.inhabas.api.domain.comment.dto.CommentUpdateDto;
import com.inhabas.api.domain.comment.usecase.CommentServiceImpl;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NoSecureWebMvcTest(CommentController.class)
public class CommentControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private WebApplicationContext wac;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private CommentServiceImpl commentService;

  @MockBean private BoardSecurityChecker boardSecurityChecker;

  @BeforeEach
  void setUp() {
    this.mvc =
        MockMvcBuilders.webAppContextSetup(wac)
            .addFilters(new CharacterEncodingFilter("UTF-8", true))
            .build();
  }

  private String jsonOf(Object response) throws JsonProcessingException {
    return objectMapper.writeValueAsString(response);
  }

  @DisplayName("댓글 전체 조회 성공 200")
  @Test
  void getCommentsOfBoardTest() throws Exception {
    // given
    Member writer = MemberTest.chiefMember();
    List<CommentDetailDto> commentList =
        List.of(
            new CommentDetailDto[] {
              new CommentDetailDto(1L, writer, "contents1", false, LocalDateTime.now()),
              new CommentDetailDto(2L, writer, "contents2", false, LocalDateTime.now()),
              new CommentDetailDto(3L, writer, "contents3", false, LocalDateTime.now())
            });
    given(commentService.getComments(anyInt(), anyLong())).willReturn(commentList);

    // when
    String response =
        mvc.perform(get("/board/{menuId}/{boardId}/comments", 1, 1L))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // then
    assertThat(response).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(commentList));
  }

  @DisplayName("댓글 생성 성공 201")
  @Test
  void createNewCommentTest() throws Exception {
    // given
    Long newCommentId = 1L;
    CommentSaveDto commentSaveDto = new CommentSaveDto("new content", null);
    given(commentService.create(any(), anyInt(), anyLong(), anyLong())).willReturn(newCommentId);

    // when
    String header =
        mvc.perform(
                post("/board/{menuId}/{boardId}/comment", 1, 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(commentSaveDto)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("Location");

    // then
    assertThat(header).contains("/board/1/1/comments");
  }

  @DisplayName("대댓글 생성 성공 201")
  @Test
  void createNewReply() throws Exception {
    // given
    Long newReplyId = 2L;
    CommentSaveDto commentSaveDto = new CommentSaveDto("new children content", 1L);
    given(commentService.create(any(), anyInt(), anyLong(), anyLong())).willReturn(newReplyId);

    // when
    String header =
        mvc.perform(
                post("/board/{menuId}/{boardId}/comment", 1, 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(commentSaveDto)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("Location");

    // then
    assertThat(header).contains("/board/1/1/comments");
  }

  @DisplayName("댓글 생성 시 500자 이상의 댓글 400")
  @Test
  void tryToSaveTooLongContents() throws Exception {
    // given
    String tooLongContents = "-".repeat(500);
    CommentSaveDto commentSaveDto = new CommentSaveDto(tooLongContents, null);

    // when
    String errorMessage =
        mvc.perform(
                post("/board/{menuId}/{boardId}/comment", 1, 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(commentSaveDto)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // then
    assertThat(errorMessage).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("댓글 수정 성공 204")
  @Test
  void updateComment() throws Exception {
    // given
    CommentUpdateDto commentUpdateDto = new CommentUpdateDto("update comment");
    given(commentService.update(anyLong(), any())).willReturn(1L);

    // when then
    mvc.perform(
            put("/comment/{commentId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(commentUpdateDto)))
        .andExpect(status().isNoContent());
  }

  @DisplayName("댓글 수정 시 500자 이상의 댓글 400")
  @Test
  void tryToUpdateTooLongContents() throws Exception {
    // given
    String tooLongContents = "-".repeat(500);
    CommentUpdateDto param = new CommentUpdateDto(tooLongContents);

    // when
    String errorMessage =
        mvc.perform(
                put("/comment/{commendId}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(param)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // then
    assertThat(errorMessage).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("댓글 삭제 성공 204")
  @Test
  void deleteComment() throws Exception {
    // given
    doNothing().when(commentService).delete(anyLong());

    // when
    mvc.perform(delete("/comment/{commentId}", 1L)).andExpect(status().isNoContent());
  }
}
