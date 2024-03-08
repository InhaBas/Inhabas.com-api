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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationRegisterForm;
import com.inhabas.api.domain.budget.dto.BudgetApplicationStatusChangeRequest;
import com.inhabas.api.domain.budget.usecase.BudgetApplicationProcessor;
import com.inhabas.api.domain.budget.usecase.BudgetApplicationService;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NoSecureWebMvcTest(BudgetApplicationController.class)
public class BudgetApplicationControllerTest {

  @Autowired private MockMvc mvc;
  @Autowired private ObjectMapper objectMapper;
  @MockBean private BudgetApplicationService budgetApplicationService;
  @MockBean private BudgetApplicationProcessor applicationProcessor;

  @DisplayName("예산지원요청 글 목록 조회 200")
  @Test
  public void getApplicationsTest_Success() throws Exception {
    // given
    Member applicant = MemberTest.basicMember1();
    BudgetApplicationDto dto =
        BudgetApplicationDto.builder()
            .id(1L)
            .title("title")
            .dateCreated(LocalDateTime.now())
            .applicant(applicant)
            .status(RequestStatus.PENDING)
            .build();
    List<BudgetApplicationDto> dtoList = List.of(dto);
    given(budgetApplicationService.getApplications(any())).willReturn(dtoList);

    // when then
    mvc.perform(get("/budget/applications"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].title").value(equalTo("title")));
  }

  @DisplayName("예산지원신청 글 단일 조회 200")
  @Test
  public void getApplicationTest_Success() throws Exception {
    // given
    Member applicant = MemberTest.basicMember1();
    ReflectionTestUtils.setField(applicant, "id", 1L);
    BudgetApplicationDetailDto dto =
        BudgetApplicationDetailDto.builder()
            .id(1L)
            .title("title")
            .details("details")
            .dateCreated(LocalDateTime.now())
            .dateUpdated(LocalDateTime.now())
            .dateUsed(LocalDateTime.now())
            .account("123-123-123")
            .outcome(10000)
            .memberInCharge(applicant)
            .applicant(applicant)
            .status(RequestStatus.PENDING)
            .build();
    given(budgetApplicationService.getApplicationDetails(any())).willReturn(dto);

    // when then
    mvc.perform(get("/budget/application/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value(equalTo("title")));
  }

  @DisplayName("예산지원신청 글 추가 201")
  @Test
  public void createApplicationTest_Success() throws Exception {
    // given
    BudgetApplicationRegisterForm form =
        BudgetApplicationRegisterForm.builder()
            .title("title")
            .details("details")
            .dateUsed(LocalDateTime.now())
            .outcome(10000)
            .account("123-123-123")
            .build();

    MockMultipartFile mockFormFile =
        new MockMultipartFile(
            "form", "", MediaType.APPLICATION_JSON_VALUE, jsonOf(form).getBytes());

    given(budgetApplicationService.registerApplication(any(), any(), any())).willReturn(1L);

    // when
    String header =
        mvc.perform(
                multipart("/budget/application")
                    .file(mockFormFile)
                    .file(new MockMultipartFile("files", new byte[0])))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("Location");

    // then
    assertThat(header).contains("/budget/application/1");
  }

  @DisplayName("예산지원신청 글 추가 400")
  @Test
  public void createApplicationTest_InvalidInput() throws Exception {
    // given
    String wrongForm =
        "{\"title\":null,\"details\":null,"
            + "\"dateUsed\":\"2024-11-01T00:00:00\",\"outcome\":0,\"account\":\"string\"}";

    MockMultipartFile mockFormFile =
        new MockMultipartFile("form", "", MediaType.APPLICATION_JSON_VALUE, wrongForm.getBytes());

    given(budgetApplicationService.registerApplication(any(), any(), any())).willReturn(1L);

    // when then
    mvc.perform(
            multipart("/budget/application")
                .file(mockFormFile)
                .file(new MockMultipartFile("files", new byte[0])))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("예산지원신청 글 수정 204")
  @Test
  public void modifyApplicationTest_Success() throws Exception {
    // given
    BudgetApplicationRegisterForm form =
        BudgetApplicationRegisterForm.builder()
            .title("title")
            .details("details")
            .dateUsed(LocalDateTime.now())
            .outcome(10000)
            .account("123-123-123")
            .build();
    doNothing().when(budgetApplicationService).updateApplication(any(), any(), any(), any());

    MockMultipartFile mockFormFile =
        new MockMultipartFile(
            "form", "", MediaType.APPLICATION_JSON_VALUE, jsonOf(form).getBytes());

    // when then
    mvc.perform(
            multipart("/budget/application/1")
                .file(mockFormFile)
                .file(new MockMultipartFile("files", new byte[0])))
        .andExpect(status().isNoContent());
  }

  @DisplayName("예산지원신청 글 수정 400")
  @Test
  public void modifyApplicationTest_InvalidInput() throws Exception {
    // given
    String wrongForm =
        "{\"title\":null,\"details\":null,"
            + "\"dateUsed\":\"2024-11-01T00:00:00\",\"outcome\":0,\"account\":\"string\"}";
    doNothing().when(budgetApplicationService).updateApplication(any(), any(), any(), any());

    MockMultipartFile mockFormFile =
        new MockMultipartFile("form", "", MediaType.APPLICATION_JSON_VALUE, wrongForm.getBytes());

    // when then
    mvc.perform(
            multipart("/budget/application/1")
                .file(mockFormFile)
                .file(new MockMultipartFile("files", new byte[0])))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("예산지원신청 글 수정 404")
  @Test
  public void modifyApplicationTest_NotFound() throws Exception {
    // given
    BudgetApplicationRegisterForm form =
        BudgetApplicationRegisterForm.builder()
            .title("title")
            .details("details")
            .dateUsed(LocalDateTime.now())
            .outcome(10000)
            .account("123-123-123")
            .build();
    doThrow(NotFoundException.class)
        .when(budgetApplicationService)
        .updateApplication(any(), any(), any(), any());

    MockMultipartFile mockFormFile =
        new MockMultipartFile(
            "form", "", MediaType.APPLICATION_JSON_VALUE, jsonOf(form).getBytes());

    // when then
    mvc.perform(
            multipart("/budget/application/1")
                .file(mockFormFile)
                .file(new MockMultipartFile("files", new byte[0])))
        .andExpect(status().isNotFound());
  }

  @DisplayName("예산지원신청 글 삭제 204")
  @Test
  public void deleteApplicationTest_Success() throws Exception {
    // given
    doNothing().when(budgetApplicationService).deleteApplication(any(), any());

    // when
    mvc.perform(delete("/budget/application/1")).andExpect(status().isNoContent());
  }

  @DisplayName("예산지원신청 글 삭제 404")
  @Test
  public void deleteApplicationTest_NotFound() throws Exception {
    // given
    doThrow(NotFoundException.class).when(budgetApplicationService).deleteApplication(any(), any());

    // when
    mvc.perform(delete("/budget/application/1")).andExpect(status().isNotFound());
  }

  @DisplayName("예산지원요청 글 상태 변경 204")
  @Test
  public void changeApplicationStatusTest_Success() throws Exception {
    // given
    BudgetApplicationStatusChangeRequest request =
        new BudgetApplicationStatusChangeRequest(RequestStatus.APPROVED, null);
    doNothing().when(applicationProcessor).process(any(), any(), any());

    // when then
    mvc.perform(
            put("/budget/application/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(request)))
        .andExpect(status().isNoContent());
  }

  @DisplayName("예산지원요청 글 상태 변경 400")
  @Test
  public void changeApplicationStatusTest_InvalidInput() throws Exception {
    // given
    BudgetApplicationStatusChangeRequest invalidRequest =
        new BudgetApplicationStatusChangeRequest(null, null);
    doNothing().when(applicationProcessor).process(any(), any(), any());

    // when then
    mvc.perform(
            put("/budget/application/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(invalidRequest)))
        .andExpect(status().isBadRequest());
  }

  @DisplayName("예산지원요청 글 상태 변경 404")
  @Test
  public void changeApplicationStatusTest_NotFound() throws Exception {
    // given
    BudgetApplicationStatusChangeRequest request =
        new BudgetApplicationStatusChangeRequest(RequestStatus.APPROVED, null);
    doThrow(NotFoundException.class).when(applicationProcessor).process(any(), any(), any());

    // when then
    mvc.perform(
            put("/budget/application/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(request)))
        .andExpect(status().isNotFound());
  }

  private String jsonOf(Object o) throws JsonProcessingException {
    return objectMapper.writeValueAsString(o);
  }
}
