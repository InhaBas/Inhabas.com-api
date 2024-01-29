package com.inhabas.api.web;

import static com.inhabas.api.auth.domain.error.ErrorCode.INVALID_INPUT_VALUE;
import static com.inhabas.api.auth.domain.error.ErrorCode.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
import com.inhabas.api.domain.club.dto.ClubHistoryDto;
import com.inhabas.api.domain.club.dto.SaveClubHistoryDto;
import com.inhabas.api.domain.club.usecase.ClubHistoryService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NoSecureWebMvcTest(ClubHistoryController.class)
public class ClubHistoryControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ClubHistoryService clubHistoryService;

  private String jsonOf(Object response) throws JsonProcessingException {
    return objectMapper.writeValueAsString(response);
  }

  @DisplayName("동아리 연혁 조회 성공 200")
  @Test
  void getClubHistories() throws Exception {
    // given
    ClubHistoryDto clubHistoryDto =
        ClubHistoryDto.builder()
            .title("title")
            .content("content")
            .dateHistory(LocalDateTime.now())
            .build();
    List<ClubHistoryDto> clubHistoryList = List.of(clubHistoryDto);
    given(clubHistoryService.getClubHistories()).willReturn(clubHistoryList);

    // when
    String response =
        mvc.perform(get("/club/histories"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).isEqualTo(jsonOf(clubHistoryList));
  }

  @DisplayName("동아리 연혁 단일 조회 성공 200")
  @Test
  void findClubHistory() throws Exception {
    // given
    ClubHistoryDto clubHistoryDto =
        ClubHistoryDto.builder()
            .title("title")
            .content("content")
            .dateHistory(LocalDateTime.now())
            .build();
    given(clubHistoryService.findClubHistory(any())).willReturn(clubHistoryDto);

    // when
    String response =
        mvc.perform(get("/club/history/{clubHistoryId}", 1L))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).isEqualTo(jsonOf(clubHistoryDto));
  }

  @DisplayName("동아리 연혁 단일 조회 데이터가 올바르지 않다면 400")
  @Test
  void findClubHistory_Invalid_Input() throws Exception {
    // given
    ClubHistoryDto clubHistoryDto =
        ClubHistoryDto.builder()
            .title("title")
            .content("content")
            .dateHistory(LocalDateTime.now())
            .build();
    given(clubHistoryService.findClubHistory(any())).willReturn(clubHistoryDto);

    // when
    String response =
        mvc.perform(get("/club/history/{clubHistoryId}", "invalid"))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("동아리 연혁 단일 조회 해당 id가 없다면 404")
  @Test
  void findClubHistory_Not_Found() throws Exception {
    // given
    doThrow(NotFoundException.class).when(clubHistoryService).findClubHistory(any());

    // when
    String response =
        mvc.perform(get("/club/history/{clubHistoryId}", 1L))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(NOT_FOUND.getMessage());
  }

  @DisplayName("동아리 연혁 생성 성공 201")
  @Test
  void writeClubHistories() throws Exception {
    // given
    SaveClubHistoryDto saveClubHistoryDto =
        SaveClubHistoryDto.builder()
            .title("good title")
            .content("good content")
            .dateHistory(LocalDateTime.parse("2023-11-01T00:00:00"))
            .build();
    given(clubHistoryService.writeClubHistory(any(), any())).willReturn(1L);

    // when
    String header =
        mvc.perform(
                post("/club/history")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(saveClubHistoryDto)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("Location");

    // then
    System.out.println("header is " + header);
    assertThat(header).contains("/club/history/1");
  }

  @DisplayName("동아리 연혁 생성 데이터가 올바르지 않다면 400")
  @Test
  void writeClubHistories_Invalid_Input() throws Exception {
    // given
    SaveClubHistoryDto saveClubHistoryDto =
        SaveClubHistoryDto.builder()
            .title("meaningless")
            .content("meaningless")
            .dateHistory(LocalDateTime.now())
            .build();
    doThrow(InvalidInputException.class).when(clubHistoryService).writeClubHistory(any(), any());

    // when
    String response =
        mvc.perform(
                post("/club/history")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(saveClubHistoryDto)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("동아리 연혁 수정 성공 204")
  @Test
  void updateClubHistory() throws Exception {
    // given
    SaveClubHistoryDto saveClubHistoryDto =
        SaveClubHistoryDto.builder()
            .title("meaningless")
            .content("meaningless")
            .dateHistory(LocalDateTime.now())
            .build();
    doNothing().when(clubHistoryService).updateClubHistory(any(), any(), any());

    // when then
    mvc.perform(
            put("/club/history/{clubHistoryId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(saveClubHistoryDto)))
        .andExpect(status().isNoContent());
  }

  @DisplayName("동아리 연혁 수정 데이터가 올바르지 않다면 400")
  @Test
  void updateClubHistory_Invalid_Input() throws Exception {
    // given
    SaveClubHistoryDto saveClubHistoryDto =
        SaveClubHistoryDto.builder()
            .title("meaningless")
            .content("meaningless")
            .dateHistory(LocalDateTime.now())
            .build();
    doThrow(InvalidInputException.class)
        .when(clubHistoryService)
        .updateClubHistory(any(), any(), any());

    // when
    String response =
        mvc.perform(
                put("/club/history/{clubHistoryId}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(saveClubHistoryDto)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("동아리 연혁 수정 해당 id가 존재하지 않다면 404")
  @Test
  void updateClubHistory_Not_Found() throws Exception {
    // given
    SaveClubHistoryDto saveClubHistoryDto =
        SaveClubHistoryDto.builder()
            .title("meaningless")
            .content("meaningless")
            .dateHistory(LocalDateTime.now())
            .build();
    doThrow(NotFoundException.class)
        .when(clubHistoryService)
        .updateClubHistory(any(), any(), any());

    // when
    String response =
        mvc.perform(
                put("/club/history/{clubHistoryId}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(saveClubHistoryDto)))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(NOT_FOUND.getMessage());
  }

  @DisplayName("동아리 연혁 삭제 성공 204")
  @Test
  void deleteClubHistory() throws Exception {
    // given
    doNothing().when(clubHistoryService).deleteClubHistory(any());

    // when then
    mvc.perform(delete("/club/history/{clubHistoryId}", 1L)).andExpect(status().isNoContent());
  }

  @DisplayName("동아리 연혁 삭제 데이터가 올바르지 않다면 400")
  @Test
  void deleteClubHistory_Invalid_Input() throws Exception {
    // given
    doNothing().when(clubHistoryService).deleteClubHistory(any());

    // when
    String response =
        mvc.perform(delete("/club/history/{clubHistoryId}", "invalid"))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("동아리 연혁 삭제 해당 id가 없다면 404")
  @Test
  void deleteClubHistory_Not_Found() throws Exception {
    // given
    doThrow(NotFoundException.class).when(clubHistoryService).deleteClubHistory(any());

    // when
    String response =
        mvc.perform(delete("/club/history/{clubHistoryId}", 1L))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(NOT_FOUND.getMessage());
  }
}
