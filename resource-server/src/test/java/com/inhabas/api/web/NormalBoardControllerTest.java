package com.inhabas.api.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.dto.BoardCountDto;
import com.inhabas.api.domain.board.repository.BaseBoardRepository;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDetailDto;
import com.inhabas.api.domain.normalBoard.dto.NormalBoardDto;
import com.inhabas.api.domain.normalBoard.dto.SaveNormalBoardDto;
import com.inhabas.api.domain.normalBoard.usecase.NormalBoardService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static com.inhabas.api.auth.domain.error.ErrorCode.INVALID_INPUT_VALUE;
import static com.inhabas.api.auth.domain.error.ErrorCode.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@NoSecureWebMvcTest(NormalBoardController.class)
public class NormalBoardControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NormalBoardService normalBoardService;
    @MockBean
    private BaseBoardRepository baseBoardRepository;

    private String jsonOf(Object response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(response);
    }

    @DisplayName("게시판 종류 당 글 개수 조회 성공 200")
    @Test
    void getBoardCount_Success() throws Exception {
        // given
        BoardCountDto boardCountDto = new BoardCountDto("공지사항", 10);
        List<BoardCountDto> dtoList = List.of(boardCountDto);
        given(baseBoardRepository.countRowsGroupByMenuId()).willReturn(dtoList);

        // when
        String response =
                mvc.perform(get("/board/count"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8);

        // then
        assertThat(response).contains(jsonOf(dtoList));
    }

    @DisplayName("게시글 목록 조회 성공 200")
    @Test
    void getBoardList_Success() throws Exception {
        // given
        Member writer = MemberTest.chiefMember();
        NormalBoardDto normalBoardDto =
                NormalBoardDto.builder()
                        .id(1L)
                        .title("title")
                        .writerName(writer.getName())
                        .dateCreated(LocalDateTime.now())
                        .dateUpdated(LocalDateTime.now())
                        .isPinned(false)
                        .build();
        List<NormalBoardDto> dtoList = List.of(normalBoardDto);
        given(normalBoardService.getPosts(any(),any(),any())).willReturn(dtoList);

        // when
        String response =
                mvc.perform(get("/board/notice"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8);

        // then
        assertThat(response).contains(jsonOf(dtoList));
    }

    @DisplayName("게시글 목록 조회 데이터가 올바르지 않다면 400")
    @Test
    void getBoardList_Invalid_Input() throws Exception {
        // when
        String response =
                mvc.perform(get("/board/invalid"))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8);

        // then
        assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
    }

    @DisplayName("게시글 단일 조회 성공 200")
    @Test
    void getBoard() throws Exception {
        // given
        Member writer = MemberTest.chiefMember();
        NormalBoardDetailDto normalBoardDetailDto =
                NormalBoardDetailDto.builder()
                        .id(1L)
                        .title("title")
                        .content("content")
                        .writerName(writer.getName())
                        .files(null)
                        .dateCreated(LocalDateTime.now())
                        .dateUpdated(LocalDateTime.now())
                        .isPinned(false)
                        .build();
        given(normalBoardService.getPost(any(),any(),any())).willReturn(normalBoardDetailDto);

        // when
        String response =
                mvc.perform(get("/board/notice/1"))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8);

        // then
        assertThat(response).contains(jsonOf(normalBoardDetailDto));
    }

    @DisplayName("게시글 단일 조회 데이터가 올바르지 않다면 400")
    @Test
    void getBoard_Invalid_Input() throws Exception {
        // when
        String response =
                mvc.perform(get("/board/notice/invalid"))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8);

        // then
        assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
    }

    @DisplayName("게시글 단일 조회 데이터가 올바르지 않다면 404")
    @Test
    void getBoard_Not_Found() throws Exception {
        // given
        doThrow(NotFoundException.class).when(normalBoardService).getPost(any(), any(), any());

        // when
        String response =
                mvc.perform(get("/board/notice/1"))
                        .andExpect(status().isNotFound())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8);

        // then
        assertThat(response).contains(NOT_FOUND.getMessage());
    }

    @DisplayName("게시글 추가 성공 201")
    @Test
    void addBoard() throws Exception {
        // given
        SaveNormalBoardDto saveNormalBoardDto =
                new SaveNormalBoardDto("title", "content", null, false);
        given(normalBoardService.write(any(),any(),any())).willReturn(1L);

        // when
        String header =
                mvc.perform(
                                post("/board/notice")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(jsonOf(saveNormalBoardDto)))
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getHeader("Location");

        // then
        assertThat(header).contains("/board/notice/1");
    }

    @DisplayName("게시글 추가 데이터가 올바르지 않다면 400")
    @Test
    void addBoard_Invalid_Input() throws Exception {
        //given
        SaveNormalBoardDto saveNormalBoardDto =
                new SaveNormalBoardDto("meaningless", "meaningless", null, false);

        // when
        String response =
                mvc.perform(post("/board/invalid")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonOf(saveNormalBoardDto)))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8);

        // then
        assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
    }

    @DisplayName("게시글 수정 성공 204")
    @Test
    void updateBoard() throws Exception {
        // given
        SaveNormalBoardDto saveNormalBoardDto =
                new SaveNormalBoardDto("title", "content", null, false);
        doNothing().when(normalBoardService).update(any(), any(), any());

        // when then
        mvc.perform(
                        put("/board/notice/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonOf(saveNormalBoardDto)))
                .andExpect(status().isNoContent());
    }

    @DisplayName("게시글 수정 데이터가 올바르지 않다면 400")
    @Test
    void updateBoard_Invalid_Input() throws Exception {
        //given
        SaveNormalBoardDto saveNormalBoardDto =
                new SaveNormalBoardDto("title", "content", null, false);
        doThrow(InvalidInputException.class).when(normalBoardService).update(any(), any(), any());

        // when
        String response =
                mvc.perform(put("/board/notice/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonOf(saveNormalBoardDto)))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8);

        // then
        assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
    }

    @DisplayName("게시글 수정 데이터가 올바르지 않다면 404")
    @Test
    void updateBoard_Not_Found() throws Exception {
        //given
        SaveNormalBoardDto saveNormalBoardDto =
                new SaveNormalBoardDto("title", "content", null, false);
        doThrow(NotFoundException.class).when(normalBoardService).update(any(), any(), any());

        // when
        String response =
                mvc.perform(put("/board/notice/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(jsonOf(saveNormalBoardDto)))
                        .andExpect(status().isNotFound())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8);

        // then
        assertThat(response).contains(NOT_FOUND.getMessage());
    }

    @DisplayName("게시글 삭제 성공 204")
    @Test
    void deleteBoard() throws Exception {
        // given
        doNothing().when(normalBoardService).delete(any());

        // when then
        mvc.perform(delete("/board/notice/1"))
                .andExpect(status().isNoContent());
    }

    @DisplayName("게시글 삭제 데이터가 올바르지 않다면 400")
    @Test
    void deleteBoard_Invalid_Input() throws Exception {
        //given
        doNothing().when(normalBoardService).delete(any());

        // when
        String response =
                mvc.perform(delete("/board/invalid/invalid"))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8);

        // then
        assertThat(response).contains(INVALID_INPUT_VALUE.getMessage());
    }

    @DisplayName("게시글 삭제 데이터가 올바르지 않다면 404")
    @Test
    void deleteBoard_Not_Found() throws Exception {
        //given
        doThrow(NotFoundException.class).when(normalBoardService).delete(any());

        // when
        String response =
                mvc.perform(delete("/board/notice/1"))
                        .andExpect(status().isNotFound())
                        .andReturn()
                        .getResponse()
                        .getContentAsString(StandardCharsets.UTF_8);

        // then
        assertThat(response).contains(NOT_FOUND.getMessage());
    }

}