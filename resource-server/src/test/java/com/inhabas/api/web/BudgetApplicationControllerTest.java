package com.inhabas.api.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.inhabas.api.domain.budget.dto.BudgetApplicationRegisterForm;
import com.inhabas.api.domain.budget.dto.BudgetApplicationUpdateForm;
import com.inhabas.api.domain.budget.usecase.BudgetApplicationProcessor;
import com.inhabas.api.domain.budget.usecase.BudgetApplicationService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@NoSecureWebMvcTest(BudgetApplicationController.class)
public class BudgetApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BudgetApplicationService service;
    @MockBean
    private BudgetApplicationProcessor processor;


    @DisplayName("지원서를 작성한다.")
    @Test
    public void createApplicationTest() throws Exception {

        mockMvc.perform(post("/budget/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"서버 사용비\", \"dateUsed\":\"2000-01-01T00:00:00\", \"details\":\"aws 크롤링 비용\", \"outcome\":50000, \"accounts\":\"카카오 01-00022-13204 유동현\"}"))
                .andExpect(status().isNoContent());

        //then
        then(service).should(times(1))
                .registerApplication(any(BudgetApplicationRegisterForm.class), any());
    }

    @DisplayName("지원서를 수정한다.")
    @Test
    public void modifyApplicationTest() throws Exception {

        mockMvc.perform(put("/budget/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"title\":\"서버 사용비\", \"dateUsed\":\"2000-01-01T00:00:00\", \"details\":\"aws 크롤링 비용\", \"outcome\":50000, \"accounts\":\"카카오 01-00022-13204 유동현\", \"applicationId\":1}"))
                .andExpect(status().isNoContent());

        //then
        then(service).should(times(1))
                .updateApplication(any(BudgetApplicationUpdateForm.class), any());
    }

    @DisplayName("지원서를 삭제한다.")
    @Test
    public void deleteApplicationTest() throws Exception {

        mockMvc.perform(delete("/budget/application/1"))
                .andExpect(status().isNoContent());

        //then
        then(service).should(times(1))
                .deleteApplication(anyInt(), any());
    }

    @DisplayName("지원서를 조회한다.")
    @Test
    public void getApplicationTest() throws Exception {

        mockMvc.perform(get("/budget/application/1"))
                .andExpect(status().isOk());

        //then
        then(service).should(times(1))
                .getApplicationDetails(anyInt());
    }

    @DisplayName("지원서 목록을 검색한다.")
    @Test
    public void searchApplicationTest() throws Exception {

        mockMvc.perform(get("/budget/applications"))
                .andExpect(status().isOk());

        //then
        then(service).should(times(1))
                .getApplications(any(), any(Pageable.class));
    }

    @DisplayName("예산지원신청글의 상태를 변경한다.")
    @Test
    public void changeApplicationStatusTest() throws Exception {

        mockMvc.perform(put("/budget/application/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"PROCESSED\", \"rejectReason\":\"\"}"))
                .andExpect(status().isNoContent());

        //then
        then(processor).should(times(1)).process(anyInt(), any(), any());
    }
}
