package com.inhabas.api.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDto;
import com.inhabas.api.domain.budget.usecase.BudgetHistoryService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NoSecureWebMvcTest(BudgetHistoryController.class)
public class BudgetHistoryControllerTest {

  @Autowired private MockMvc mvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private BudgetHistoryService budgetHistoryService;

  @DisplayName("회계 내역의 모든 연도 조회 200.")
  @Test
  public void getAllYearsOfHistoryTest_Success() throws Exception {
    // given
    List<Integer> yearList = List.of(2024, 2023);
    given(budgetHistoryService.getAllYearOfHistory()).willReturn(yearList);

    // when
    String response =
        mvc.perform(get("/budget/histories/years"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    // then
    assertThat(response).contains("2024", "2023");
  }

  @DisplayName("회계 내역 목록 조회 200")
  @Test
  public void searchBudgetHistoryTest_Success() throws Exception {
    // given
    BudgetHistoryDto dto =
        BudgetHistoryDto.builder()
            .id(1L)
            .title("title")
            .dateCreated(LocalDateTime.now())
            .dateUpdated(LocalDateTime.now())
            .dateUsed(LocalDateTime.now())
            .income(0)
            .outcome(10000)
            .memberStudentIdInCharge("121212")
            .memberNameInCharge("홍길동")
            .memberStudentIdInCharge("232323")
            .memberNameReceived("김짱구")
            .build();
    List<BudgetHistoryDto> dtoList = List.of(dto);
    given(budgetHistoryService.searchHistoryList(null)).willReturn(dtoList);
    given(budgetHistoryService.getBalance()).willReturn(1000000);

    // when then
    mvc.perform(get("/budget/histories"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.page.data[0].title").value(equalTo("title")))
        .andExpect(jsonPath("$.balance").value(equalTo(1000000)));
  }

  @DisplayName("회계 내역 목록 조회 실패 400")
  @Test
  public void searchBudgetHistoryTest_Invalid_Input() throws Exception {
    // given
    BudgetHistoryDto dto =
        BudgetHistoryDto.builder()
            .id(1L)
            .title("title")
            .dateCreated(LocalDateTime.now())
            .dateUpdated(LocalDateTime.now())
            .dateUsed(LocalDateTime.now())
            .income(0)
            .outcome(10000)
            .memberStudentIdInCharge("121212")
            .memberNameInCharge("홍길동")
            .memberStudentIdInCharge("232323")
            .memberNameReceived("김짱구")
            .build();
    List<BudgetHistoryDto> dtoList = List.of(dto);
    given(budgetHistoryService.searchHistoryList(null)).willReturn(dtoList);
    given(budgetHistoryService.getBalance()).willReturn(1000000);

    // when then
    mvc.perform(get("/budget/histories").param("year", "invalid"))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("단일 회계 내역 조회 200")
  @Test
  public void getBudgetHistoryTest_Success() throws Exception {
    // given
    BudgetHistoryDetailDto dto =
        BudgetHistoryDetailDto.builder()
            .id(1L)
            .title("title")
            .details("details")
            .dateCreated(LocalDateTime.now())
            .dateUpdated(LocalDateTime.now())
            .dateUsed(LocalDateTime.now())
            .income(0)
            .outcome(10000)
            .account("123-1231-23")
            .memberStudentIdInCharge("123123123")
            .memberNameInCharge("김맹구")
            .memberStudentIdReceived("234234234")
            .memberNameReceived("김철수")
            .receipts(null)
            .build();
    given(budgetHistoryService.getHistory(any())).willReturn(dto);

    // when then
    mvc.perform(get("/budget/history/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value(equalTo("title")))
        .andExpect(jsonPath("$.details").value(equalTo("details")));
  }

  @DisplayName("단일 회계 내역 조회 400")
  @Test
  public void getBudgetHistoryTest_InvalidInput() throws Exception {
    // given
    BudgetHistoryDetailDto dto =
        BudgetHistoryDetailDto.builder()
            .id(1L)
            .title("title")
            .details("details")
            .dateCreated(LocalDateTime.now())
            .dateUpdated(LocalDateTime.now())
            .dateUsed(LocalDateTime.now())
            .income(0)
            .outcome(10000)
            .account("123-1231-23")
            .memberStudentIdInCharge("123123123")
            .memberNameInCharge("김맹구")
            .memberStudentIdReceived("234234234")
            .memberNameReceived("김철수")
            .receipts(null)
            .build();
    given(budgetHistoryService.getHistory(any())).willReturn(dto);

    // when then
    mvc.perform(get("/budget/history/invalid")).andExpect(status().isBadRequest());
  }

  @DisplayName("단일 회계 내역 조회 404")
  @Test
  public void getBudgetHistoryTest_NotFound() throws Exception {
    // given
    doThrow(NotFoundException.class).when(budgetHistoryService).getHistory(any());

    // when then
    mvc.perform(get("/budget/history/1")).andExpect(status().isNotFound());
  }

  @DisplayName("회계 내역 추가 201")
  @Test
  public void createNewHistoryTest_Success() throws Exception {
    // given
    BudgetHistoryCreateForm form =
        BudgetHistoryCreateForm.builder()
            .title("title")
            .details("details")
            .dateUsed(LocalDateTime.now())
            .income(0)
            .outcome(10000)
            .memberStudentIdReceived("123123123")
            .memberNameReceived("김짱구")
            .build();

    MockMultipartFile mockFormFile =
        new MockMultipartFile(
            "form", "", MediaType.APPLICATION_JSON_VALUE, jsonOf(form).getBytes());

    given(budgetHistoryService.createHistory(any(), any(), any())).willReturn(1L);

    // when
    String header =
        mvc.perform(
                multipart("/budget/history")
                    .file(mockFormFile)
                    .file(new MockMultipartFile("files", new byte[0])))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("Location");

    // then
    assertThat(header).contains("/budget/history/1");
  }

  @DisplayName("회계 내역 추가 400")
  @Test
  public void createNewHistoryTest_InvalidInput() throws Exception {
    // given
    String wrongForm =
        "{\"dateUsed\":null,\"title\":null,\"details\":null,"
            + "\"memberStudentIdReceived\":null,\"memberNameReceived\":null,\"income\":0,\"outcome\":0}";

    MockMultipartFile mockFormFile =
        new MockMultipartFile("form", "", MediaType.APPLICATION_JSON_VALUE, wrongForm.getBytes());

    given(budgetHistoryService.createHistory(any(), any(), any())).willReturn(1L);

    // when then
    mvc.perform(
            multipart("/budget/history")
                .file(mockFormFile)
                .file(new MockMultipartFile("files", new byte[0])))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("회계 내역 수정 204")
  @Test
  public void modifyHistoryTest_Success() throws Exception {
    // given
    BudgetHistoryCreateForm form =
        BudgetHistoryCreateForm.builder()
            .title("title")
            .details("details")
            .dateUsed(LocalDateTime.now())
            .income(0)
            .outcome(10000)
            .memberStudentIdReceived("123123123")
            .memberNameReceived("김짱구")
            .build();
    doNothing().when(budgetHistoryService).modifyHistory(any(), any(), any(), any());

    MockMultipartFile mockFormFile =
        new MockMultipartFile(
            "form", "", MediaType.APPLICATION_JSON_VALUE, jsonOf(form).getBytes());

    // when then
    mvc.perform(
            multipart("/budget/history/1")
                .file(mockFormFile)
                .file(new MockMultipartFile("files", new byte[0])))
        .andExpect(status().isNoContent());
  }

  @DisplayName("회계 내역 수정 400")
  @Test
  public void modifyHistoryTest_InvalidInput() throws Exception {
    // given
    String wrongForm =
        "{\"dateUsed\":null,\"title\":null,\"details\":null,"
            + "\"memberStudentIdReceived\":null,\"memberNameReceived\":null,\"income\":0,\"outcome\":0}";
    doNothing().when(budgetHistoryService).modifyHistory(any(), any(), any(), any());

    MockMultipartFile mockFormFile =
        new MockMultipartFile("form", "", MediaType.APPLICATION_JSON_VALUE, wrongForm.getBytes());

    // when then
    mvc.perform(
            multipart("/budget/history/1")
                .file(mockFormFile)
                .file(new MockMultipartFile("files", new byte[0])))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("회계 내역 수정 404")
  @Test
  public void modifyHistoryTest_NotFound() throws Exception {
    // given
    BudgetHistoryCreateForm form =
        BudgetHistoryCreateForm.builder()
            .title("title")
            .details("details")
            .dateUsed(LocalDateTime.now())
            .income(0)
            .outcome(10000)
            .memberStudentIdReceived("123123123")
            .memberNameReceived("김짱구")
            .build();
    doThrow(NotFoundException.class)
        .when(budgetHistoryService)
        .modifyHistory(any(), any(), any(), any());

    MockMultipartFile mockFormFile =
        new MockMultipartFile(
            "form", "", MediaType.APPLICATION_JSON_VALUE, jsonOf(form).getBytes());

    // when then
    mvc.perform(
            multipart("/budget/history/1")
                .file(mockFormFile)
                .file(new MockMultipartFile("files", new byte[0])))
        .andExpect(status().isNotFound());
  }

  @DisplayName("회계 내역 삭제 204")
  @Test
  public void deleteHistoryTest_Success() throws Exception {
    // given
    doNothing().when(budgetHistoryService).deleteHistory(any(), any());

    // when
    mvc.perform(delete("/budget/history/1")).andExpect(status().isNoContent());
  }

  @DisplayName("회계 내역 삭제 404")
  @Test
  public void deleteHistoryTest_NotFound() throws Exception {
    // given
    doThrow(NotFoundException.class).when(budgetHistoryService).deleteHistory(any(), any());

    // when
    mvc.perform(delete("/budget/history/1")).andExpect(status().isNotFound());
  }

  private String jsonOf(Object o) throws JsonProcessingException {
    return objectMapper.writeValueAsString(o);
  }
}
