package com.inhabas.api.securityConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.inhabas.api.domain.board.usecase.BoardService;
import com.inhabas.api.domain.member.domain.MemberService;
import com.inhabas.api.web.BoardController;
import com.inhabas.testAnnotataion.DefaultWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@DefaultWebMvcTest(BoardController.class)
public class RoleHierarchyTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    BoardController boardController;

    @MockBean
    BoardService boardService;

    @MockBean
    MemberService memberService;


    @Test
    @DisplayName("관리자는 일반회원 자료에 접근 가능하다.")
    @WithMockUser(roles = "ADMIN")
    public void adminCanAccessToResourceForBasicMember() throws Exception {
        공지사항_게시판_접근();
    }

    @Test
    @DisplayName("회장은 일반회원 자료에 접근 가능하다.")
    @WithMockUser(roles = "CHIEF")
    public void chiefCanAccessToResourceForBasicMember() throws Exception {
        공지사항_게시판_접근();
    }

    @Test
    @DisplayName("회장단은 일반회원 자료에 접근 가능하다.")
    @WithMockUser(roles = "EXECUTIVES")
    public void executivesCanAccessToResourceForBasicMember() throws Exception {
        공지사항_게시판_접근();
    }

    @Test
    @DisplayName("일반회원은 일반회원 자료에 접근 가능하다.")
    @WithMockUser(roles = "BASIC_MEMBER")
    public void basicMemberCanAccessToResourceForBasicMember() throws Exception {
        공지사항_게시판_접근();
    }

    @Test
    @DisplayName("비활동회원은 일반회원 자료에 접근 불가능하다.")
    @WithMockUser(roles = "DEACTIVATED_MEMBER")
    public void deactivatedMemberCannotAccessToResourceForBasicMember() throws Exception {
        공지사항_게시판_접근_불가();
    }

    @Test
    @DisplayName("미승인회원은 일반회원 자료에 접근 불가능하다.")
    @WithMockUser(roles = "NOT_APPROVED_MEMBER")
    public void notApprovedMemberCannotAccessToResourceForBasicMember() throws Exception {
        공지사항_게시판_접근_불가();
    }

    @Test
    @DisplayName("비회원은 일반회원 자료에 접근 불가능하다.")
    @WithMockUser(roles = "ANONYMOUS")
    public void anonymousCannotAccessToResourceForBasicMember() throws Exception {
        공지사항_게시판_접근_불가();
    }


    private void 공지사항_게시판_접근() throws Exception {
        mockMvc.perform(get("/boards")
                        .param("menu_id", "6"))
                .andExpect(status().isOk());
    }

    private void 공지사항_게시판_접근_불가() throws Exception {
        mockMvc.perform(get("/boards")
                        .param("menu_id", "6"))
                .andExpect(status().isForbidden());
    }
}
