package com.inhabas.api.web;

import static com.inhabas.api.auth.domain.error.ErrorCode.INVALID_INPUT_VALUE;
import static com.inhabas.api.auth.domain.error.ErrorCode.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.domain.policy.dto.PolicyTermDto;
import com.inhabas.api.domain.policy.dto.SavePolicyTernDto;
import com.inhabas.api.domain.policy.usecase.PolicyTermService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NoSecureWebMvcTest(PolicyTermController.class)
public class PolicyTermControllerTest {

  @Autowired private MockMvc mvc;

  @MockBean private PolicyTermService policyTermService;

  @Autowired private ObjectMapper objectMapper;

  private String jsonOf(Object response) throws JsonProcessingException {
    return objectMapper.writeValueAsString(response);
  }

  @DisplayName("정책 단일 조회 성공 200")
  @Test
  void findPolicyTerm() throws Exception {
    // given
    PolicyTermDto policyTermDto = PolicyTermDto.builder().title("title").content("content").build();
    given(policyTermService.findPolicyTerm(any())).willReturn(policyTermDto);

    // when
    String response =
        mvc.perform(get("/policy/{policyTermId}", 1L))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).isEqualTo(jsonOf(policyTermDto));
  }

  @DisplayName("정책 단일 조회 데이터가 올바르지 않다면 400")
  @Test
  void findPolicyTerm_Invalid_Input() throws Exception {
    // given
    PolicyTermDto policyTermDto = PolicyTermDto.builder().title("title").content("content").build();
    given(policyTermService.findPolicyTerm(any())).willReturn(policyTermDto);

    // when
    String response =
        mvc.perform(get("/policy/{policyTermId}", "invalid"))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("정책 단일 조회 해당 id가 없다면 404")
  @Test
  void findPolicyTerm_Not_Found() throws Exception {
    // given
    doThrow(NotFoundException.class).when(policyTermService).findPolicyTerm(any());

    // when
    String response =
        mvc.perform(get("/policy/{policyTermId}", 1L))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(NOT_FOUND.getMessage());
  }

  @DisplayName("정책 수정 성공 204")
  @Test
  void updatePolicyTerm() throws Exception {
    // given
    SavePolicyTernDto savePolicyTernDto =
        SavePolicyTernDto.builder().content("meaningless").build();
    doNothing().when(policyTermService).updatePolicyTerm(any(), any());

    // when then
    mvc.perform(
            put("/policy/{policyTermId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOf(savePolicyTernDto)))
        .andExpect(status().isNoContent());
  }

  @DisplayName("정책 수정 데이터가 올바르지 않다면 400")
  @Test
  void updatePolicyTerm_Invalid_Input() throws Exception {
    // given
    SavePolicyTernDto savePolicyTernDto =
        SavePolicyTernDto.builder().content("meaningless").build();
    doThrow(InvalidInputException.class).when(policyTermService).updatePolicyTerm(any(), any());

    // when
    String response =
        mvc.perform(
                put("/policy/{policyTermId}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(savePolicyTernDto)))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
  }

  @DisplayName("정책 수정 해당 id가 존재하지 않다면 404")
  @Test
  void updatePolicyTerm_Not_Found() throws Exception {
    // given
    SavePolicyTernDto savePolicyTernDto =
        SavePolicyTernDto.builder().content("meaningless").build();
    doThrow(NotFoundException.class).when(policyTermService).updatePolicyTerm(any(), any());

    // when
    String response =
        mvc.perform(
                put("/policy/{policyTermId}", 1L)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonOf(savePolicyTernDto)))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(NOT_FOUND.getMessage());
  }
}
