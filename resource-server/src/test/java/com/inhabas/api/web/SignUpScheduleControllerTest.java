package com.inhabas.api.web;

import static com.inhabas.api.auth.domain.error.ErrorCode.INVALID_INPUT_VALUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.error.ErrorCode;
import com.inhabas.api.domain.signUpSchedule.dto.SignUpScheduleDto;
import com.inhabas.api.domain.signUpSchedule.exception.InvalidDateException;
import com.inhabas.api.domain.signUpSchedule.usecase.SignUpScheduler;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NoSecureWebMvcTest(SignUpScheduleController.class)
public class SignUpScheduleControllerTest {

  @Autowired private ObjectMapper objectMapper;

  @Autowired private MockMvc mvc;

  @MockBean private SignUpScheduler signUpScheduler;

  @DisplayName("회원가입 일정을 조회한다.")
  @Test
  public void getSignUpSchedule() throws Exception {
    // given
    SignUpScheduleDto existingSchedule =
        new SignUpScheduleDto(
            999,
            LocalDateTime.of(2022, 1, 1, 0, 0, 0),
            LocalDateTime.of(2022, 1, 2, 0, 0, 0),
            LocalDateTime.of(2022, 1, 3, 0, 0, 0),
            LocalDateTime.of(2022, 1, 4, 0, 0, 0),
            LocalDateTime.of(2022, 1, 5, 0, 0, 0));
    given(signUpScheduler.getSchedule()).willReturn(existingSchedule);

    mvc.perform(get("/signUp/schedule"))
        .andExpect(status().isOk())
        .andExpect(content().string(objectMapper.writeValueAsString(existingSchedule)));
  }

  @DisplayName("회원가입 일정을 수정한다.")
  @Test
  public void updateSignUpSchedule() throws Exception {
    String jsonRequest =
        objectMapper.writeValueAsString(
            new SignUpScheduleDto(
                999,
                LocalDateTime.of(2022, 1, 1, 0, 0, 0),
                LocalDateTime.of(2022, 1, 2, 0, 0, 0),
                LocalDateTime.of(2022, 1, 3, 0, 0, 0),
                LocalDateTime.of(2022, 1, 4, 0, 0, 0),
                LocalDateTime.of(2022, 1, 5, 0, 0, 0)));

    mvc.perform(
            put("/signUp/schedule").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
        .andExpect(status().isNoContent());
  }

  @DisplayName("회원 기수가 양의 정수가 아닌경우, 400")
  @Test
  public void invalidGenerationTest() throws Exception {
    String jsonRequest =
        objectMapper.writeValueAsString(
            new SignUpScheduleDto(
                0,
                LocalDateTime.of(2022, 1, 1, 0, 0, 0),
                LocalDateTime.of(2022, 1, 2, 0, 0, 0),
                LocalDateTime.of(2022, 1, 3, 0, 0, 0),
                LocalDateTime.of(2022, 1, 4, 0, 0, 0),
                LocalDateTime.of(2022, 1, 5, 0, 0, 0)));

    String response =
        mvc.perform(
                put("/signUp/schedule")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("날짜가 잘못된 경우, 400")
  @Test
  public void invalidDateInfoTest() throws Exception {
    doThrow(new InvalidDateException(ErrorCode.INVALID_SIGNUP_DATE))
        .when(signUpScheduler)
        .updateSchedule(any());
    String jsonRequest =
        objectMapper.writeValueAsString(
            new SignUpScheduleDto(
                999,
                LocalDateTime.of(2022, 1, 5, 0, 0, 0),
                LocalDateTime.of(2022, 1, 4, 0, 0, 0),
                LocalDateTime.of(2022, 1, 3, 0, 0, 0),
                LocalDateTime.of(2022, 1, 2, 0, 0, 0),
                LocalDateTime.of(2022, 1, 1, 0, 0, 0)));

    mvc.perform(
            put("/signUp/schedule").contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
        .andExpect(status().isBadRequest());
  }
}
