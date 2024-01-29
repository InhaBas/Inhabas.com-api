package com.inhabas.api.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.inhabas.api.domain.budget.usecase.BudgetHistoryService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NoSecureWebMvcTest(BudgetHistoryController.class)
public class BudgetHistoryControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private BudgetHistoryService budgetHistoryService;

  //    @DisplayName("예산내역 생성 시 form validation 을 통과하지 못한다.")
  //    @Test
  //    public void historyCreationFormTest() throws Exception {
  //        //given
  //        String jsonOfCreationForm = "{\"dateUsed\":\"2999-01-01T01:01:00\",\"title\":\"
  // \",\"details\":\"aws 작년 비용\",\"personReceived\":12171652,\"income\":-1,\"outcome\":-1}";
  //
  //        //when
  //        Exception resolvedException =
  //                mockMvc.perform(post("/budget/history")
  //                                .contentType(MediaType.APPLICATION_JSON)
  //                                .content(jsonOfCreationForm))
  //                        .andExpect(status().isBadRequest())
  //                        .andReturn().getResolvedException();
  //
  //        //then
  //        MethodArgumentNotValidException validException = (MethodArgumentNotValidException)
  // resolvedException;
  //        assert validException != null;
  //        assertThat(validException.getFieldErrorCount()).isEqualTo(4);
  //        assertThat(validException.getFieldErrors())
  //                .extracting("defaultMessage", "field")
  //                .contains(
  //                        tuple("must not be blank", "title"),
  //                        tuple("must be greater than or equal to 0", "income"),
  //                        tuple("must be greater than or equal to 0", "outcome"),
  //                        tuple("must be a past date", "dateUsed"));
  //
  //        then(budgetHistoryService).should(times(0)).createNewHistory(any(), any());
  //    }

  //    @DisplayName("예산내역 생성 시 validation 을 통과한다.")
  //    @Test
  //    public void budgetCreationFormWillPassValidationTest() throws Exception {
  //        //given
  //        String jsonOfCreationForm = "{\"dateUsed\":\"2000-01-01T01:01:00\",\"title\":\"서버
  // 운영비\",\"details\":\"aws 작년 비용\",\"personReceived\":12171652,\"income\":0,\"outcome\":500000}";
  //
  //        //when
  //        mockMvc.perform(post("/budget/history")
  //                        .contentType(MediaType.APPLICATION_JSON)
  //                        .content(jsonOfCreationForm))
  //                .andExpect(status().isNoContent());
  //
  //        then(budgetHistoryService).should(times(1)).createNewHistory(any(), any());
  //    }

  //    @DisplayName("예산내역 생성 시 form validation 을 통과하지 못한다.")
  //    @Test
  //    public void historyModificationFormTest() throws Exception {
  //        //given
  //        String jsonOfCreationForm = "{\"id\":1,
  // \"dateUsed\":\"2999-01-01T01:01:00\",\"title\":\" \",\"details\":\"aws 작년
  // 비용\",\"personReceived\":12171652,\"income\":-1,\"outcome\":-1}";
  //
  //        //when
  //        Exception resolvedException =
  //                mockMvc.perform(put("/budget/history")
  //                                .contentType(MediaType.APPLICATION_JSON)
  //                                .content(jsonOfCreationForm))
  //                        .andExpect(status().isBadRequest())
  //                        .andReturn().getResolvedException();
  //
  //        //then
  //        MethodArgumentNotValidException validException = (MethodArgumentNotValidException)
  // resolvedException;
  //        assert validException != null;
  //        assertThat(validException.getFieldErrorCount()).isEqualTo(4);
  //        assertThat(validException.getFieldErrors())
  //                .extracting("defaultMessage", "field")
  //                .contains(
  //                        tuple("must not be blank", "title"),
  //                        tuple("must be greater than or equal to 0", "income"),
  //                        tuple("must be greater than or equal to 0", "outcome"),
  //                        tuple("must be a past date", "dateUsed"));
  //
  //        then(budgetHistoryService).should(times(0)).modifyHistory(any(), any());
  //    }

  //    @DisplayName("예산내역 수정 시 validation 을 통과한다.")
  //    @Test
  //    public void budgetModificationFormWillPassValidationTest() throws Exception {
  //        //given
  //        String jsonOfModificationForm =
  // "{\"id\":1,\"dateUsed\":\"2000-01-01T01:01:00\",\"title\":\"서버 운영비\",\"details\":\"aws 작년
  // 비용\",\"personReceived\":12171652,\"income\":0,\"outcome\":500000}";
  //
  //        //when
  //        mockMvc.perform(put("/budget/history")
  //                        .contentType(MediaType.APPLICATION_JSON)
  //                        .content(jsonOfModificationForm))
  //                .andExpect(status().isNoContent());
  //
  //        then(budgetHistoryService).should(times(1)).modifyHistory(any(), any());
  //    }

  @DisplayName("예산내역 삭제 시 validation 을 통과한다.")
  @Test
  public void budgetDeletionFormWillPassValidationTest() throws Exception {
    // given

    // when
    mockMvc.perform(delete("/budget/history/2")).andExpect(status().isNoContent());

    then(budgetHistoryService).should(times(1)).deleteHistory(any(), any());
  }

  @DisplayName("회계내역 리스트를 불러온다.")
  @Test
  public void getListOfBudgetHistoryListTest() throws Exception {

    mockMvc.perform(get("/budget/histories")).andExpect(status().isOk());

    then(budgetHistoryService).should(times(1)).searchHistoryList(any(), any());
  }

  @DisplayName("회계내역 정보 하나를 불러온다.")
  @Test
  public void fetchOneBudgetHistoryTest() throws Exception {

    mockMvc.perform(get("/budget/history/2")).andExpect(status().isOk());

    then(budgetHistoryService).should(times(1)).getHistory(anyInt());
  }

  @DisplayName("회계 내역의 모든 연도를 불러온다.")
  @Test
  public void fetchAllYearsOfHistoryTest() throws Exception {

    mockMvc.perform(get("/budget/histories/years")).andExpect(status().isOk());

    then(budgetHistoryService).should(times(1)).getAllYearOfHistory();
  }
}
