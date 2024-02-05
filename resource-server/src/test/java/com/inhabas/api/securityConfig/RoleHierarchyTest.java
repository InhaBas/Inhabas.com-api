package com.inhabas.api.securityConfig;

import com.inhabas.api.web.BoardController;
import com.inhabas.testAnnotataion.DefaultWebMvcTest;

@DefaultWebMvcTest(BoardController.class)
public class RoleHierarchyTest {

  //    @Autowired
  //    private MockMvc mockMvc;
  //
  //    @Autowired
  //    BoardController boardController;
  //
  //    @MockBean
  //    BoardService boardService;
  //
  //    @MockBean
  //    MemberManageService memberService;
  //
  //    @MockBean
  //    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  //
  //
  //    @Test
  //    @DisplayName("관리자는 일반회원 자료에 접근 가능하다.")
  //    @WithMockUser(roles = "ADMIN")
  //    public void adminCanAccessToResourceForBasicMember() throws Exception {
  //        공지사항_게시판_접근();
  //    }
  //
  //    @Test
  //    @DisplayName("회장은 일반회원 자료에 접근 가능하다.")
  //    @WithMockUser(roles = "CHIEF")
  //    public void chiefCanAccessToResourceForBasicMember() throws Exception {
  //        공지사항_게시판_접근();
  //    }
  //
  //    @Test
  //    @DisplayName("회장단은 일반회원 자료에 접근 가능하다.")
  //    @WithMockUser(roles = "EXECUTIVES")
  //    public void executivesCanAccessToResourceForBasicMember() throws Exception {
  //        공지사항_게시판_접근();
  //    }
  //
  //    @Test
  //    @DisplayName("일반회원은 일반회원 자료에 접근 가능하다.")
  //    @WithMockUser(roles = "BASIC")
  //    public void basicMemberCanAccessToResourceForBasicMember() throws Exception {
  //        공지사항_게시판_접근();
  //    }
  //
  //    @Test
  //    @DisplayName("비활동회원은 일반회원 자료에 접근 불가능하다.")
  //    @WithMockUser(roles = "DEACTIVATED")
  //    public void deactivatedMemberCannotAccessToResourceForBasicMember() throws Exception {
  //        공지사항_게시판_접근_불가();
  //    }
  //
  //    @Test
  //    @DisplayName("미승인회원은 일반회원 자료에 접근 불가능하다.")
  //    @WithMockUser(roles = "NOT_APPROVED")
  //    public void notApprovedMemberCannotAccessToResourceForBasicMember() throws Exception {
  //        공지사항_게시판_접근_불가();
  //    }
  //
  //    @Test
  //    @DisplayName("비회원은 일반회원 자료에 접근 불가능하다.")
  //    @WithMockUser(roles = "ANONYMOUS")
  //    public void anonymousCannotAccessToResourceForBasicMember() throws Exception {
  //        공지사항_게시판_접근_불가();
  //    }
  //
  //
  //    private void 공지사항_게시판_접근() throws Exception {
  //        mockMvc.perform(get("/boards")
  //                        .param("menu_id", "6"))
  //                .andExpect(status().isOk());
  //    }
  //
  //    private void 공지사항_게시판_접근_불가() throws Exception {
  //        mockMvc.perform(get("/boards")
  //                        .param("menu_id", "6"))
  //                .andExpect(status().isForbidden());
  //    }
}
