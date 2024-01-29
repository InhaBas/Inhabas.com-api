package com.inhabas.api.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.oauth2.member.service.MemberService;
import com.inhabas.api.domain.board.domain.NormalBoard;
import com.inhabas.api.domain.board.usecase.BoardService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@NoSecureWebMvcTest(BoardController.class)
public class BoardControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired BoardController boardController;

  @MockBean BoardService boardService;

  @MockBean MemberService memberService;

  @MockBean NormalBoard normalBoard;

  //    @DisplayName("게시글 저장을 요청한다.")
  //    @Test
  //    @WithMockJwtAuthenticationToken
  //    public void addNewBoard() throws Exception {
  //        //given
  //        SaveBoardDto saveBoardDto = new SaveBoardDto("This is title", "This is content", new
  // MenuId(1));
  //        given(boardService.write(any(), any(SaveBoardDto.class))).willReturn(1);
  //
  //        // when
  //        mvc.perform(post("/board")
  //                        .contentType(MediaType.APPLICATION_JSON)
  //                        .content(objectMapper.writeValueAsString(saveBoardDto)))
  //                .andExpect(status().isCreated())
  //                .andExpect(content().string("1"));
  //    }

  //    @DisplayName("게시글 수정을 요청한다.")
  //    @Test
  //    @WithMockJwtAuthenticationToken
  //    public void updateBoard() throws Exception{
  //        //given
  //        UpdateBoardDto updateBoardDto = new UpdateBoardDto(1, "제목을 수정하였습니다.", "내용을 수정하였습니다.");
  //        given(boardService.update(any(), any(UpdateBoardDto.class))).willReturn(1);
  //
  //        // when
  //        mvc.perform(put("/board")
  //                        .contentType(MediaType.APPLICATION_JSON)
  //                        .content(objectMapper.writeValueAsString(updateBoardDto)))
  //                .andExpect(status().isOk())
  //                .andExpect(content().string("1"));
  //    }

  //    @DisplayName("게시글 삭제를 요청한다.")
  //    @Test
  //    public void deleteBoard() throws Exception{
  //        //given
  //        doNothing().when(boardService).delete(any(), anyInt());
  //
  //        // when
  //        mvc.perform(delete("/board/1"))
  //                    .andExpect(status().isNoContent());
  //    }

  //    @DisplayName("게시글 목록 조회를 요청한다.")
  //    @Test
  //    public void getBoardList() throws Exception {
  //        PageRequest pageable = PageRequest.of(2,1, Sort.Direction.ASC, "id");
  //
  //        List<BoardDto> results = new ArrayList<>();
  //        results.add(new BoardDto(1, "Shown Title1", null, "Mingyeom",
  //                new MenuId(2), LocalDateTime.now(), null));
  //        results.add(new BoardDto(2, "Shown Title2", null, "Mingyeom",
  //                new MenuId(2), LocalDateTime.now(), null));
  //        results.add(new BoardDto(3, "Shown Title3", null, "Mingyeom",
  //                new MenuId(2), LocalDateTime.now(), null));
  //
  //        Page<BoardDto> expectedBoardDto = new PageImpl<>(results, pageable, results.size());
  //
  //        given(boardService.getBoardList(any(), any())).willReturn(expectedBoardDto);
  //
  //        // when
  //        String responseBody = mvc.perform(get("/boards?menu_id=6")
  //                        .contentType(MediaType.APPLICATION_JSON)
  //                        .param("page", "2")
  //                        .param("size", "1")
  //                        .param("sort", "ASC")
  //                        .param("properties", "id"))
  //                .andExpect(status().isOk())
  //                .andDo(print())
  //                .andReturn()
  //                .getResponse().getContentAsString();
  //
  //        // then
  //        assertThat(responseBody).isEqualTo(objectMapper.writeValueAsString(expectedBoardDto));
  //    }

  //    @DisplayName("게시글 단일 조회를 요청한다.")
  //    @Test
  //    public void getBoardDetail() throws Exception{
  //        //given
  //        BoardDto boardDto =
  //                new BoardDto(1, "Shown Title", "Shown Content", "Mingyeom",
  //                        new MenuId(1), LocalDateTime.now(), null);
  //        given(boardService.getBoard(anyLong())).willReturn(boardDto);
  //
  //        // when
  //        String responseBody = mvc.perform(get("/board/1"))
  //                .andExpect(status().isOk())
  //                .andReturn()
  //                .getResponse().getContentAsString();
  //
  //        // then
  //        assertThat(responseBody).isEqualTo(objectMapper.writeValueAsString(boardDto));
  //
  //    }

  //    @DisplayName("게시글 작성 시 Title의 길이가 범위를 초과해 오류 발생")
  //    @Test
  //    @WithMockJwtAuthenticationToken
  //    public void TitleIsTooLongError() throws Exception {
  //        //given
  //        SaveBoardDto saveBoardDto = new SaveBoardDto("title".repeat(20) + ".", "content", new
  // MenuId(1));
  //
  //        // when
  //        String errorMessage = Objects.requireNonNull(
  //                mvc.perform(post("/board")
  //                        .contentType(MediaType.APPLICATION_JSON)
  //                        .content(objectMapper.writeValueAsString(saveBoardDto)))
  //                .andExpect(status().isBadRequest())
  //                .andReturn()
  //                .getResolvedException())
  //                .getMessage();
  //
  //        // then
  //        assertThat(errorMessage).isNotBlank();
  //        assertThat(errorMessage).contains("제목은 최대 100자입니다.");
  //    }

  //    @DisplayName("게시글 작성 시 Contents가 null인 경우 오류 발생")
  //    @Test
  //    @WithMockJwtAuthenticationToken
  //    public void ContentIsNullError() throws Exception {
  //        //given
  //        SaveBoardDto saveBoardDto = new SaveBoardDto("title", "   ", new MenuId(1));
  //
  //        // when
  //        String errorMessage = Objects.requireNonNull(
  //                mvc.perform(post("/board")
  //                        .contentType(MediaType.APPLICATION_JSON)
  //                        .content(objectMapper.writeValueAsString(saveBoardDto)))
  //                .andExpect(status().isBadRequest())
  //                .andReturn()
  //                .getResolvedException())
  //                .getMessage();
  //
  //        // then
  //        assertThat(errorMessage).isNotBlank();
  //        assertThat(errorMessage).contains("본문을 입력하세요.");
  //    }
}
