package com.inhabas.api.web;

import static com.inhabas.api.auth.domain.error.ErrorCode.INVALID_INPUT_VALUE;
import static com.inhabas.api.auth.domain.error.ErrorCode.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.dto.BoardCountDto;
import com.inhabas.api.domain.board.repository.BaseBoardRepository;
import com.inhabas.api.domain.contest.dto.ContestBoardDetailDto;
import com.inhabas.api.domain.contest.dto.ContestBoardDto;
import com.inhabas.api.domain.contest.dto.SaveContestBoardDto;
import com.inhabas.api.domain.contest.usecase.ContestBoardService;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NoSecureWebMvcTest(ContestBoardController.class)
public class ContestBoardControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ContestBoardService contestBoardService;
  @MockBean private BaseBoardRepository baseBoardRepository;

  private String jsonOf(Object response) throws JsonProcessingException {
    return objectMapper.writeValueAsString(response);
  }

  @DisplayName("게시판 종류 당 글 개수 조회 성공 200")
  @Test
  void getBoardCount_Success() throws Exception {
    // given
    BoardCountDto boardCountDto = new BoardCountDto("대외활동", 10);
    List<BoardCountDto> dtoList = List.of(boardCountDto);
    given(baseBoardRepository.countRowsGroupByMenuName(any())).willReturn(dtoList);

    // when
    String response =
        mvc.perform(get("/contest/count"))
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
    ContestBoardDto contestBoardDto =
        ContestBoardDto.builder()
            .id(1L)
            .contestFieldId(1L)
            .title("테스트 제목")
            .topic("테스트 주제")
            .association("테스트 협회")
            .dateContestStart(LocalDate.of(2024, 1, 1))
            .dateContestEnd(LocalDate.of(2024, 3, 1))
            .thumbnail(new FileDownloadDto("thumbnail.jpg", "thumbnailUrl"))
            .build();
    List<ContestBoardDto> dtoList = List.of(contestBoardDto);
    given(contestBoardService.getContestBoards(any(), any(), any(), any())).willReturn(dtoList);

    // when
    String response =
        mvc.perform(
                get("/contest/contest")
                    .param("contestFieldId", "1")
                    .param("search", "")
                    .param("page", "0")
                    .param("size", "4")
                    .param("sortBy", "dateContestEnd"))
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
        mvc.perform(get("/contest/invalid"))
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
    ContestBoardDetailDto contestBoardDetailDto =
        ContestBoardDetailDto.builder()
            .id(1L)
            .contestFieldId(1L)
            .title("테스트 제목")
            .content("테스트 내용")
            .writerName(writer.getName())
            .association("테스트 협회")
            .topic("테스트 주제")
            .dateContestStart(LocalDate.of(2024, 1, 1))
            .dateContestEnd(LocalDate.of(2024, 3, 1))
            .dateCreated(LocalDateTime.now())
            .dateUpdated(LocalDateTime.now())
            .thumbnail(new FileDownloadDto("thumbnail.jpg", "thumbnailUrl"))
            .images(null)
            .otherFiles(null)
            .build();
    given(contestBoardService.getContestBoard(any(), any())).willReturn(contestBoardDetailDto);

    // when
    String response =
        mvc.perform(get("/contest/activity/1"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(jsonOf(contestBoardDetailDto));
  }

  @DisplayName("게시글 단일 조회 데이터가 올바르지 않다면 400")
  @Test
  void getBoard_Invalid_Input() throws Exception {
    // when
    String response =
        mvc.perform(get("/contest/contest/invalid"))
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
    doThrow(NotFoundException.class).when(contestBoardService).getContestBoard(any(), any());

    // when
    String response =
        mvc.perform(get("/contest/contest/1"))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(NOT_FOUND.getMessage());
  }

  @DisplayName("공모전 게시글 추가 성공 201")
  @Test
  void addBoard() throws Exception {
    // given
    given(contestBoardService.writeContestBoard(any(), any(), any())).willReturn(1L);

    SaveContestBoardDto saveContestBoardDto =
        SaveContestBoardDto.builder()
            .contestFieldId(1L)
            .title("good title")
            .content("good content")
            .association("good association")
            .topic("good topic")
            .dateContestStart(LocalDate.now())
            .dateContestEnd(LocalDate.now().plusDays(10))
            .build();

    String saveContestBoardDtoJson = objectMapper.writeValueAsString(saveContestBoardDto);
    MockMultipartFile formPart =
        new MockMultipartFile("form", "", "application/json", saveContestBoardDtoJson.getBytes());

    MockMultipartFile filePart =
        new MockMultipartFile("files", "filename.txt", "text/plain", "file content".getBytes());

    // when
    String header =
        mvc.perform(
                multipart("/contest/activity")
                    .file(formPart)
                    .file(filePart)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("Location");

    // then
    assertThat(header).contains("/contest/activity/1");
  }

  @DisplayName("게시글 추가 데이터가 올바르지 않다면 400")
  @Test
  void addBoard_Invalid_Input() throws Exception {

    // given

    SaveContestBoardDto saveContestBoardDto =
        SaveContestBoardDto.builder()
            .contestFieldId(1L)
            .title("")
            .content("good content")
            .association("good association")
            .topic("good topic")
            .dateContestStart(LocalDate.now())
            .dateContestEnd(LocalDate.now().plusDays(10))
            .build();

    String saveContestBoardDtoJson = objectMapper.writeValueAsString(saveContestBoardDto);
    MockMultipartFile formPart =
        new MockMultipartFile("form", "", "application/json", saveContestBoardDtoJson.getBytes());

    MockMultipartFile filePart =
        new MockMultipartFile("files", "filename.txt", "text/plain", "file content".getBytes());

    // when
    String response =
        mvc.perform(
                multipart("/contest/contest")
                    .file(formPart)
                    .file(filePart)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
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

    doNothing().when(contestBoardService).updateContestBoard(any(), any(), any());

    SaveContestBoardDto saveContestBoardDto =
        SaveContestBoardDto.builder()
            .contestFieldId(1L)
            .title("good title")
            .content("good content")
            .association("good association")
            .topic("good topic")
            .dateContestStart(LocalDate.now())
            .dateContestEnd(LocalDate.now().plusDays(10))
            .build();

    String saveContestBoardDtoJson = objectMapper.writeValueAsString(saveContestBoardDto);
    MockMultipartFile formPart =
        new MockMultipartFile("form", "", "application/json", saveContestBoardDtoJson.getBytes());

    MockMultipartFile filePart =
        new MockMultipartFile("files", "filename.txt", "text/plain", "file content".getBytes());

    // when then
    mvc.perform(
            multipart("/contest/contest/1")
                .file(formPart)
                .file(filePart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isNoContent());
  }

  @DisplayName("게시글 수정 데이터가 올바르지 않다면 400")
  @Test
  void updateBoard_Invalid_Input() throws Exception {
    // given

    doThrow(InvalidInputException.class)
        .when(contestBoardService)
        .updateContestBoard(any(), any(), any());

    SaveContestBoardDto saveContestBoardDto =
        SaveContestBoardDto.builder()
            .contestFieldId(1L)
            .title("")
            .content("good content")
            .association("good association")
            .topic("good topic")
            .dateContestStart(LocalDate.now())
            .dateContestEnd(LocalDate.now().plusDays(10))
            .build();

    String saveContestBoardDtoJson = objectMapper.writeValueAsString(saveContestBoardDto);
    MockMultipartFile formPart =
        new MockMultipartFile("form", "", "application/json", saveContestBoardDtoJson.getBytes());
    MockMultipartFile filePart =
        new MockMultipartFile("files", "filename.txt", "text/plain", "file content".getBytes());

    // when
    String response =
        mvc.perform(
                multipart("/contest/contest/")
                    .file(formPart)
                    .file(filePart)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
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
    // given
    doThrow(NotFoundException.class)
        .when(contestBoardService)
        .updateContestBoard(any(), any(), any());

    SaveContestBoardDto saveContestBoardDto =
        SaveContestBoardDto.builder()
            .contestFieldId(1L)
            .title("good title")
            .content("good content")
            .association("good association")
            .topic("good topic")
            .dateContestStart(LocalDate.now())
            .dateContestEnd(LocalDate.now().plusDays(10))
            .build();

    String saveContestBoardDtoJson = objectMapper.writeValueAsString(saveContestBoardDto);
    MockMultipartFile formPart =
        new MockMultipartFile("form", "", "application/json", saveContestBoardDtoJson.getBytes());

    MockMultipartFile filePart =
        new MockMultipartFile("files", "filename.txt", "text/plain", "file content".getBytes());

    // when
    String response =
        mvc.perform(
                multipart("/contest/contest/1")
                    .file(formPart)
                    .file(filePart)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
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
    doNothing().when(contestBoardService).deleteContestBoard(any());

    // when then
    mvc.perform(delete("/contest/contest/1")).andExpect(status().isNoContent());
  }

  @DisplayName("게시글 삭제 데이터가 올바르지 않다면 400")
  @Test
  void deleteBoard_Invalid_Input() throws Exception {
    // given
    doNothing().when(contestBoardService).deleteContestBoard(any());

    // when
    String response =
        mvc.perform(delete("/contest/invalid/invalid"))
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
    // given
    doThrow(NotFoundException.class).when(contestBoardService).deleteContestBoard(any());

    // when
    String response =
        mvc.perform(delete("/contest/contest/1"))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(NOT_FOUND.getMessage());
  }
}
