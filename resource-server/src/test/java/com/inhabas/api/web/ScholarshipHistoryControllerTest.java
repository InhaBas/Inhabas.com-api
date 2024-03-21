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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.inhabas.api.domain.scholarship.dto.SaveScholarshipHistoryDto;
import com.inhabas.api.domain.scholarship.repository.ScholarshipHistoryRepositoryImpl.Data;
import com.inhabas.api.domain.scholarship.repository.ScholarshipHistoryRepositoryImpl.YearlyData;
import com.inhabas.api.domain.scholarship.usecase.ScholarshipHistoryService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NoSecureWebMvcTest(ScholarshipHistoryController.class)
public class ScholarshipHistoryControllerTest {

  @Autowired private MockMvc mvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private ScholarshipHistoryService scholarshipHistoryService;

  private String jsonOf(Object response) throws JsonProcessingException {
    return objectMapper.writeValueAsString(response);
  }

  @DisplayName("장학회 연혁 조회 성공 200")
  @Test
  void getScholarHistoriesTest() throws Exception {
    // given
    YearlyData yearlyData =
        new YearlyData(2024, List.of(new Data(1L, "title", LocalDateTime.now())));
    List<YearlyData> yearlyDataList = List.of(yearlyData);
    given(scholarshipHistoryService.getScholarshipHistories()).willReturn(yearlyDataList);

    // when
    String response =
        mvc.perform(get("/scholarship/histories"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).isEqualTo(jsonOf(yearlyDataList));
  }

  @DisplayName("장학회 연혁 생성 성공 201")
  @Test
  void writeScholarshipHistoryTest() throws Exception {
    // given
    SaveScholarshipHistoryDto saveScholarshipHistoryDto =
        SaveScholarshipHistoryDto.builder()
            .title("good title")
            .dateHistory(LocalDateTime.parse("2023-11-01T00:00:00"))
            .build();
    given(scholarshipHistoryService.writeScholarshipHistory(any(), any())).willReturn(1L);

    // when
    String header =
        mvc.perform(
                post("/scholarship/history")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(saveScholarshipHistoryDto)))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("Location");

    // then
    assertThat(header).contains("/scholarship/history/1");
  }

  @DisplayName("장학회 연혁 생성 데이터가 올바르지 않다면 400")
  @Test
  void writeScholarshipHistory_Invalid_Input() throws Exception {
    // given
    SaveScholarshipHistoryDto saveScholarshipHistoryDto =
        SaveScholarshipHistoryDto.builder()
            .title("meaningless")
            .dateHistory(LocalDateTime.now())
            .build();
    doThrow(InvalidInputException.class)
        .when(scholarshipHistoryService)
        .writeScholarshipHistory(any(), any());

    // when
    String response =
        mvc.perform(
                post("/scholarship/history")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(saveScholarshipHistoryDto)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("장학회 연혁 수정 성공 204")
  @Test
  void updateScholarshipHistoryTest() throws Exception {
    // given
    SaveScholarshipHistoryDto saveScholarshipHistoryDto =
        SaveScholarshipHistoryDto.builder()
            .title("meaningless")
            .dateHistory(LocalDateTime.now())
            .build();
    doNothing().when(scholarshipHistoryService).updateScholarshipHistory(any(), any(), any());

    // when then
    mvc.perform(
            put("/scholarship/history/{scholarshipHistoryId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(saveScholarshipHistoryDto)))
        .andExpect(status().isNoContent());
  }

  @DisplayName("장학회 연혁 수정 데이터가 올바르지 않다면 400")
  @Test
  void updateScholarshipHistory_Invalid_Input() throws Exception {
    // given
    SaveScholarshipHistoryDto saveScholarshipHistoryDto =
        SaveScholarshipHistoryDto.builder()
            .title("meaningless")
            .dateHistory(LocalDateTime.now())
            .build();
    doThrow(InvalidInputException.class)
        .when(scholarshipHistoryService)
        .updateScholarshipHistory(any(), any(), any());

    // when
    String response =
        mvc.perform(
                put("/scholarship/history/{scholarshipHistoryId}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(saveScholarshipHistoryDto)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("장학회 연혁 수정 해당 id가 존재하지 않다면 404")
  @Test
  void updateScholarshipHistory_Not_Found() throws Exception {
    // given
    SaveScholarshipHistoryDto saveScholarshipHistoryDto =
        SaveScholarshipHistoryDto.builder()
            .title("meaningless")
            .dateHistory(LocalDateTime.now())
            .build();
    doThrow(NotFoundException.class)
        .when(scholarshipHistoryService)
        .updateScholarshipHistory(any(), any(), any());

    // when
    String response =
        mvc.perform(
                put("/scholarship/history/{scholarshipHistoryId}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(saveScholarshipHistoryDto)))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(NOT_FOUND.getMessage());
  }

  @DisplayName("장학회 연혁 삭제 성공 204")
  @Test
  void deleteScholarshipHistoryTest() throws Exception {
    // given
    doNothing().when(scholarshipHistoryService).deleteScholarshipHistory(any());

    // when then
    mvc.perform(delete("/scholarship/history/{scholarshipHistoryId}", 1L))
        .andExpect(status().isNoContent());
  }

  @DisplayName("장학회 연혁 삭제 데이터가 올바르지 않다면 400")
  @Test
  void deleteScholarshipHistory_Invalid_Input() throws Exception {
    // given
    doNothing().when(scholarshipHistoryService).deleteScholarshipHistory(any());

    // when
    String response =
        mvc.perform(delete("/scholarship/history/{scholarshipHistoryId}", "invalid"))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("장학회 연혁 삭제 해당 id가 없다면 404")
  @Test
  void deleteScholarshipHistory_Not_Found() throws Exception {
    // given
    doThrow(NotFoundException.class)
        .when(scholarshipHistoryService)
        .deleteScholarshipHistory(any());

    // when
    String response =
        mvc.perform(delete("/scholarship/history/{scholarshipHistoryId}", 1L))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(NOT_FOUND.getMessage());
  }
}
