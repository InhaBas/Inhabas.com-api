package com.inhabas.api.web;

import static com.inhabas.api.auth.domain.error.ErrorCode.INVALID_INPUT_VALUE;
import static com.inhabas.api.auth.domain.error.ErrorCode.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
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
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.project.dto.ProjectBoardDetailDto;
import com.inhabas.api.domain.project.dto.ProjectBoardDto;
import com.inhabas.api.domain.project.dto.SaveProjectBoardDto;
import com.inhabas.api.domain.project.usecase.ProjectBoardService;
import com.inhabas.testAnnotataion.NoSecureWebMvcTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NoSecureWebMvcTest(ProjectBoardController.class)
public class ProjectBoardControllerTest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private ProjectBoardService projectBoardService;
  @MockBean private BaseBoardRepository baseBoardRepository;

  private String jsonOf(Object response) throws JsonProcessingException {
    return objectMapper.writeValueAsString(response);
  }

  @DisplayName("프로젝트 게시판 종류 당 글 개수 조회 성공 200")
  @Test
  void getBoardCount_Success() throws Exception {
    // given
    BoardCountDto boardCountDto = new BoardCountDto("알파 테스터", 10);
    List<BoardCountDto> dtoList = List.of(boardCountDto);
    given(baseBoardRepository.countRowsGroupByMenuName(any())).willReturn(dtoList);

    // when
    String response =
        mvc.perform(get("/project/count"))
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
    ProjectBoardDto projectBoardDto =
        ProjectBoardDto.builder()
            .id(1L)
            .title("title")
            .writerName(writer.getName())
            .dateCreated(LocalDateTime.now())
            .dateUpdated(LocalDateTime.now())
            .isPinned(false)
            .build();
    List<ProjectBoardDto> dtoList = List.of(projectBoardDto);
    given(projectBoardService.getPosts(any(), any())).willReturn(dtoList);

    // when
    String response =
        mvc.perform(get("/project/alpha"))
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
        mvc.perform(get("/project/invalid"))
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
    ProjectBoardDetailDto projectBoardDetailDto =
        ProjectBoardDetailDto.builder()
            .id(1L)
            .title("title")
            .content("content")
            .writerName(writer.getName())
            .images(null)
            .otherFiles(null)
            .dateCreated(LocalDateTime.now())
            .dateUpdated(LocalDateTime.now())
            .isPinned(false)
            .build();
    given(projectBoardService.getPost(any(), any(), any())).willReturn(projectBoardDetailDto);

    // when
    String response =
        mvc.perform(get("/project/alpha/1"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(jsonOf(projectBoardDetailDto));
  }

  @DisplayName("게시글 단일 조회 데이터가 올바르지 않다면 400")
  @Test
  void getBoard_Invalid_Input() throws Exception {
    // when
    String response =
        mvc.perform(get("/project/alpha/invalid"))
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
    doThrow(NotFoundException.class).when(projectBoardService).getPost(any(), any(), any());

    // when
    String response =
        mvc.perform(get("/project/alpha/1"))
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
    given(projectBoardService.write(any(), any(), any())).willReturn(1L);

    SaveProjectBoardDto saveProjectBoardDto =
        SaveProjectBoardDto.builder()
            .title("good title")
            .content("good content")
            .pinOption(1)
            .build();

    String saveProjectBoardDtoJson = objectMapper.writeValueAsString(saveProjectBoardDto);
    MockMultipartFile formPart =
        new MockMultipartFile("form", "", "application/json", saveProjectBoardDtoJson.getBytes());
    MockMultipartFile filePart =
        new MockMultipartFile("files", "filename.txt", "text/plain", "file content".getBytes());

    // when
    String header =
        mvc.perform(
                multipart("/project/alpha")
                    .file(formPart)
                    .file(filePart)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getHeader("Location");

    // then
    assertThat(header).contains("/project/alpha/1");
  }

  @DisplayName("게시글 추가 데이터가 올바르지 않다면 400")
  @Test
  void addBoard_Invalid_Input() throws Exception {
    // given

    SaveProjectBoardDto saveProjectBoardDto =
        SaveProjectBoardDto.builder().title("").content("good content").pinOption(1).build();

    String saveProjectBoardDtoJson = objectMapper.writeValueAsString(saveProjectBoardDto);
    MockMultipartFile formPart =
        new MockMultipartFile("form", "", "application/json", saveProjectBoardDtoJson.getBytes());
    MockMultipartFile filePart =
        new MockMultipartFile("files", "filename.txt", "text/plain", "file content".getBytes());

    // when
    String response =
        mvc.perform(
                multipart("/project/alpha")
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

    doNothing().when(projectBoardService).update(any(), any(), any());

    SaveProjectBoardDto saveProjectBoardDto =
        SaveProjectBoardDto.builder()
            .title("good title")
            .content("good content")
            .pinOption(1)
            .build();

    String saveProjectBoardDtoJson = objectMapper.writeValueAsString(saveProjectBoardDto);
    MockMultipartFile formPart =
        new MockMultipartFile("form", "", "application/json", saveProjectBoardDtoJson.getBytes());
    MockMultipartFile filePart =
        new MockMultipartFile("files", "filename.txt", "text/plain", "file content".getBytes());

    // when then
    mvc.perform(
            multipart("/project/alpha/1")
                .file(formPart)
                .file(filePart)
                .contentType(MediaType.MULTIPART_FORM_DATA))
        .andExpect(status().isNoContent());
  }

  @DisplayName("게시글 수정 데이터가 올바르지 않다면 400")
  @Test
  void updateBoard_Invalid_Input() throws Exception {
    // given

    doThrow(InvalidInputException.class).when(projectBoardService).update(any(), any(), any());

    SaveProjectBoardDto saveProjectBoardDto =
        SaveProjectBoardDto.builder().title("").content("good content").pinOption(1).build();

    String saveProjectBoardDtoJson = objectMapper.writeValueAsString(saveProjectBoardDto);
    MockMultipartFile formPart =
        new MockMultipartFile("form", "", "application/json", saveProjectBoardDtoJson.getBytes());
    MockMultipartFile filePart =
        new MockMultipartFile("files", "filename.txt", "text/plain", "file content".getBytes());

    // when
    String response =
        mvc.perform(
                multipart("/project/alpha")
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
    doThrow(NotFoundException.class).when(projectBoardService).update(any(), any(), any());

    SaveProjectBoardDto saveProjectBoardDto =
        SaveProjectBoardDto.builder()
            .title("good title")
            .content("good content")
            .pinOption(1)
            .build();

    String saveProjectBoardDtoJson = objectMapper.writeValueAsString(saveProjectBoardDto);
    MockMultipartFile formPart =
        new MockMultipartFile("form", "", "application/json", saveProjectBoardDtoJson.getBytes());
    MockMultipartFile filePart =
        new MockMultipartFile("files", "filename.txt", "text/plain", "file content".getBytes());

    // when
    String response =
        mvc.perform(
                multipart("/project/alpha/1")
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
    doNothing().when(projectBoardService).delete(any());

    // when then
    mvc.perform(delete("/project/alpha/1")).andExpect(status().isNoContent());
  }

  @DisplayName("게시글 삭제 데이터가 올바르지 않다면 400")
  @Test
  void deleteBoard_Invalid_Input() throws Exception {
    // given
    doNothing().when(projectBoardService).delete(any());

    // when
    String response =
        mvc.perform(delete("/project/invalid/invalid"))
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
    doThrow(NotFoundException.class).when(projectBoardService).delete(any());

    // when
    String response =
        mvc.perform(delete("/project/alpha/1"))
            .andExpect(status().isNotFound())
            .andReturn()
            .getResponse()
            .getContentAsString(StandardCharsets.UTF_8);

    // then
    assertThat(response).contains(NOT_FOUND.getMessage());
  }
}
