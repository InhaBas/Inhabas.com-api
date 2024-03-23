package com.inhabas.api.web;

import static com.inhabas.api.auth.domain.error.ErrorCode.INVALID_INPUT_VALUE;
import static com.inhabas.api.auth.domain.error.ErrorCode.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import com.inhabas.api.domain.club.dto.ClubActivityDetailDto;
import com.inhabas.api.domain.club.dto.ClubActivityDto;
import com.inhabas.api.domain.club.dto.SaveClubActivityDto;
import com.inhabas.api.domain.club.usecase.ClubActivityService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NoSecureWebMvcTest(ClubActivityController.class)
public class ClubActivityControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ClubActivityService clubActivityService;

  private String jsonOf(Object response) throws JsonProcessingException {
    return objectMapper.writeValueAsString(response);
  }

  @DisplayName("동아리 활동 조회 성공 200")
  @Test
  void getClubActivities() throws Exception {
    // given
    ClubActivityDto clubActivityDto =
        ClubActivityDto.builder()
            .id(1L)
            .title("title")
            .dateCreated(LocalDateTime.now())
            .dateUpdated(LocalDateTime.now())
            .writerName("jsh")
            .thumbnail(null)
            .build();
    List<ClubActivityDto> clubActivityDtoList = List.of(clubActivityDto);
    given(clubActivityService.getClubActivities()).willReturn(clubActivityDtoList);

    // when
    mvc.perform(get("/club/activities"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].id").value(equalTo(1)))
        .andExpect(jsonPath("$.data[0].title").value(equalTo("title")))
        .andExpect(jsonPath("$.data[0].writerName").value(equalTo("jsh")))
        .andExpect(jsonPath("$.data[0].thumbnail").value(equalTo(null)));
  }

  @DisplayName("동아리 활동 글 단일 조회 성공 200")
  @Test
  void findClubActivity() throws Exception {
    // given
    ClubActivityDetailDto clubActivityDetailDto =
        ClubActivityDetailDto.builder()
            .id(1L)
            .title("title")
            .content("content")
            .dateCreated(LocalDateTime.now())
            .dateUpdated(LocalDateTime.now())
            .writerName("jsh")
            .images(null)
            .otherFiles(null)
            .build();
    given(clubActivityService.getClubActivity(any())).willReturn(clubActivityDetailDto);

    // when
    String response =
        mvc.perform(get("/club/activity/{boardId}", 1L))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).isEqualTo(jsonOf(clubActivityDetailDto));
  }

  @DisplayName("동아리 활동 글 단일 조회 데이터가 올바르지 않다면 400")
  @Test
  void findClubActivity_Invalid_Input() throws Exception {
    // given
    ClubActivityDetailDto clubActivityDetailDto =
        ClubActivityDetailDto.builder()
            .id(1L)
            .title("title")
            .content("content")
            .dateCreated(LocalDateTime.now())
            .dateUpdated(LocalDateTime.now())
            .writerName("jsh")
            .images(null)
            .otherFiles(null)
            .build();
    given(clubActivityService.getClubActivity(any())).willReturn(clubActivityDetailDto);

    // when
    String response =
        mvc.perform(get("/club/activity/{boardId}", "invalid"))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("동아리 활동 글 단일 조회 해당 id가 없다면 404")
  @Test
  void findClubActivity_Not_Found() throws Exception {
    // given
    doThrow(NotFoundException.class).when(clubActivityService).getClubActivity(any());

    // when
    String response =
        mvc.perform(get("/club/activity/{boardId}", 1L))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(NOT_FOUND.getMessage());
  }

  @DisplayName("동아리 활동 글 생성 성공 201")
  @Test
  void writeClubActivity() throws Exception {
    // given
    SaveClubActivityDto form = new SaveClubActivityDto("title", "content", null);
    given(clubActivityService.writeClubActivity(any(), any())).willReturn(1L);

    // when
    String header =
        mvc.perform(
                post("/club/activity")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(form)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("Location");

    // then
    System.out.println("header is " + header);
    assertThat(header).contains("/club/activity/1");
  }

  @DisplayName("동아리 활동 글 생성 데이터가 올바르지 않다면 성공 400")
  @Test
  void writeClubActivity_Invalid_Input() throws Exception {
    // given
    SaveClubActivityDto form = new SaveClubActivityDto("", "", null);
    doThrow(InvalidInputException.class).when(clubActivityService).writeClubActivity(any(), any());

    // when
    String response =
        mvc.perform(
                post("/club/activity")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(form)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("동아리 활동 글 수정 성공 204")
  @Test
  void updateClubActivity() throws Exception {
    // given
    SaveClubActivityDto form = new SaveClubActivityDto("title", "content", null);
    doNothing().when(clubActivityService).updateClubActivity(any(), any(), any());

    // when then
    mvc.perform(
            post("/club/activity/1").contentType(MediaType.APPLICATION_JSON).content(jsonOf(form)))
        .andExpect(status().isNoContent());
  }

  @DisplayName("동아리 활동 글 수정 데이터가 올바르지 않다면 400")
  @Test
  void updateClubActivity_Invalid_Input() throws Exception {
    // given
    SaveClubActivityDto form = new SaveClubActivityDto("", "", null);

    doThrow(InvalidInputException.class)
        .when(clubActivityService)
        .updateClubActivity(any(), any(), any());

    // when
    String response =
        mvc.perform(
                post("/club/activity/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(form)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("동아리 활동 글 수정 해당 id가 없다면 404")
  @Test
  void updateClubActivity_Not_Found() throws Exception {
    // given
    SaveClubActivityDto form = new SaveClubActivityDto("title", "content", null);

    doThrow(NotFoundException.class)
        .when(clubActivityService)
        .updateClubActivity(any(), any(), any());

    // when
    String response =
        mvc.perform(
                post("/club/activity/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(form)))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(NOT_FOUND.getMessage());
  }

  @DisplayName("동아리 활동 글 삭제 성공 204")
  @Test
  void deleteClubActivity() throws Exception {
    // given
    doNothing().when(clubActivityService).deleteClubActivity(any());

    // when then
    mvc.perform(delete("/club/activity/{boardId}", 1L)).andExpect(status().isNoContent());
  }

  @DisplayName("동아리 활동 글 삭제 데이터가 올바르지 않다면 400")
  @Test
  void deleteClubActivity_Invalid_Input() throws Exception {
    // given
    doNothing().when(clubActivityService).deleteClubActivity(any());

    // when
    String response =
        mvc.perform(delete("/club/activity/{boardId}", "invalid"))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("동아리 활동 글 삭제 해당 id가 없다면 404")
  @Test
  void deleteClubActivity_Not_Found() throws Exception {
    // given
    doThrow(NotFoundException.class).when(clubActivityService).deleteClubActivity(any());

    // when
    String response =
        mvc.perform(delete("/club/activity/{boardId}", 1))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(NOT_FOUND.getMessage());
  }
}
