package com.inhabas.api.web;

import static com.inhabas.api.auth.domain.error.ErrorCode.INVALID_INPUT_VALUE;
import static com.inhabas.api.auth.domain.error.ErrorCode.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.scholarship.dto.SaveScholarshipBoardDto;
import com.inhabas.api.domain.scholarship.dto.ScholarshipBoardDetailDto;
import com.inhabas.api.domain.scholarship.dto.ScholarshipBoardDto;
import com.inhabas.api.domain.scholarship.usecase.ScholarshipBoardService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NoSecureWebMvcTest(ScholarshipController.class)
public class ScholarshipControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ScholarshipBoardService scholarshipBoardService;

  private String jsonOf(Object response) throws JsonProcessingException {
    return objectMapper.writeValueAsString(response);
  }

  @DisplayName("장학회 게시글 목록 조회 성공 200")
  @Test
  void getBoardList_Success() throws Exception {
    // given
    Member writer = MemberTest.chiefMember();
    ScholarshipBoardDto scholarshipBoardDto =
        ScholarshipBoardDto.builder()
            .id(1L)
            .title("title")
            .writer(writer)
            .dateCreated(LocalDateTime.now())
            .dateUpdated(LocalDateTime.now())
            .build();
    List<ScholarshipBoardDto> dtoList = List.of(scholarshipBoardDto);
    given(scholarshipBoardService.getPosts(any(), any())).willReturn(dtoList);

    // when
    String response =
        mvc.perform(get("/scholarship/sponsor"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(jsonOf(dtoList));
  }

  @DisplayName("장학회 게시글 목록 조회 데이터가 올바르지 않다면 400")
  @Test
  void getBoardList_Invalid_Input() throws Exception {
    // when
    String response =
        mvc.perform(get("/scholarship/invalid"))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("장학회 게시글 단일 조회 성공 200")
  @Test
  void getBoard() throws Exception {
    // given
    Member writer = MemberTest.chiefMember();
    ScholarshipBoardDetailDto scholarshipBoardDetailDto =
        ScholarshipBoardDetailDto.builder()
            .id(1L)
            .title("title")
            .content("content")
            .writer(writer)
            .images(null)
            .otherFiles(null)
            .dateHistory(LocalDateTime.now())
            .dateCreated(LocalDateTime.now())
            .dateUpdated(LocalDateTime.now())
            .build();
    given(scholarshipBoardService.getPost(any(), any(), any()))
        .willReturn(scholarshipBoardDetailDto);

    // when
    String response =
        mvc.perform(get("/scholarship/sponsor/1"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(jsonOf(scholarshipBoardDetailDto));
  }

  @DisplayName("장학회 게시글 단일 조회 데이터가 올바르지 않다면 400")
  @Test
  void getBoard_Invalid_Input() throws Exception {
    // when
    String response =
        mvc.perform(get("/scholarship/sponsor/invalid"))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("장학회 게시글 단일 조회 데이터가 올바르지 않다면 404")
  @Test
  void getBoard_Not_Found() throws Exception {
    // given
    doThrow(NotFoundException.class).when(scholarshipBoardService).getPost(any(), any(), any());

    // when
    String response =
        mvc.perform(get("/scholarship/sponsor/1"))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(NOT_FOUND.getMessage());
  }

  @DisplayName("장학회 게시글 추가 성공 201")
  @Test
  void addBoard() throws Exception {
    // given
    given(scholarshipBoardService.write(any(), any(), any())).willReturn(1L);

    SaveScholarshipBoardDto form =
        SaveScholarshipBoardDto.builder()
            .title("good title")
            .content("good content")
            .dateHistory(LocalDateTime.now())
            .build();

    // when
    String header =
        mvc.perform(
                post("/scholarship/sponsor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(form)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("Location");

    // then
    assertThat(header).contains("/scholarship/sponsor/1");
  }

  @DisplayName("장학회 게시글 추가 데이터가 올바르지 않다면 400")
  @Test
  void addBoard_Invalid_Input() throws Exception {
    // given
    SaveScholarshipBoardDto form =
        SaveScholarshipBoardDto.builder()
            .title("")
            .content("good content")
            .dateHistory(LocalDateTime.now())
            .build();

    // when
    String response =
        mvc.perform(
                post("/scholarship/sponsor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(form)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("장학회 게시글 수정 성공 204")
  @Test
  void updateBoard() throws Exception {
    // given
    doNothing().when(scholarshipBoardService).update(any(), any(), any(), any());

    SaveScholarshipBoardDto form =
        SaveScholarshipBoardDto.builder()
            .title("good title")
            .content("good content")
            .dateHistory(LocalDateTime.now())
            .build();

    // when then
    mvc.perform(
            post("/scholarship/sponsor/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(form)))
        .andExpect(status().isNoContent());
  }

  @DisplayName("장학회 게시글 수정 데이터가 올바르지 않다면 400")
  @Test
  void updateBoard_Invalid_Input() throws Exception {
    // given
    doThrow(InvalidInputException.class)
        .when(scholarshipBoardService)
        .update(any(), any(), any(), any());

    SaveScholarshipBoardDto form =
        SaveScholarshipBoardDto.builder()
            .title("")
            .content("good content")
            .dateHistory(LocalDateTime.now())
            .build();

    // when
    String response =
        mvc.perform(
                post("/scholarship/sponsor")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(form)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("장학회 게시글 수정 데이터가 올바르지 않다면 404")
  @Test
  void updateBoard_Not_Found() throws Exception {
    // given
    doThrow(NotFoundException.class)
        .when(scholarshipBoardService)
        .update(any(), any(), any(), any());

    SaveScholarshipBoardDto form =
        SaveScholarshipBoardDto.builder()
            .title("good title")
            .content("good content")
            .dateHistory(LocalDateTime.now())
            .build();

    // when
    String response =
        mvc.perform(
                post("/scholarship/sponsor/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(form)))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(NOT_FOUND.getMessage());
  }

  @DisplayName("장학회 게시글 삭제 성공 204")
  @Test
  void deleteBoard() throws Exception {
    // given
    doNothing().when(scholarshipBoardService).delete(any(), any());

    // when then
    mvc.perform(delete("/scholarship/sponsor/1")).andExpect(status().isNoContent());
  }

  @DisplayName("장학회 게시글 삭제 데이터가 올바르지 않다면 400")
  @Test
  void deleteBoard_Invalid_Input() throws Exception {
    // given
    doNothing().when(scholarshipBoardService).delete(any(), any());

    // when
    String response =
        mvc.perform(delete("/scholarship/sponsor/invalid"))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("장학회 게시글 삭제 데이터가 올바르지 않다면 404")
  @Test
  void deleteBoard_Not_Found() throws Exception {
    // given
    doThrow(NotFoundException.class).when(scholarshipBoardService).delete(any(), any());

    // when
    String response =
        mvc.perform(delete("/scholarship/sponsor/1"))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(NOT_FOUND.getMessage());
  }
}
