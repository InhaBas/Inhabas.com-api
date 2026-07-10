package com.inhabas.api.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import com.inhabas.api.domain.file.usecase.BoardFileService;
import com.inhabas.testAnnotation.NoSecureWebMvcTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@NoSecureWebMvcTest(FileController.class)
public class FileControllerTest {

  @Autowired private MockMvc mvc;

  @MockitoBean private BoardFileService boardFileService;

  private MockMultipartFile validFile() {
    return new MockMultipartFile("file", "test.txt", "text/plain", "hello".getBytes());
  }

  @DisplayName("게시판 첨부파일 업로드 성공 200")
  @Test
  void uploadBoardFile_Success() throws Exception {
    // given
    FileDownloadDto fileDownloadDto =
        FileDownloadDto.builder()
            .id("uuid")
            .name("test.txt")
            .url("https://s3.url/test.txt")
            .size(5L)
            .type("text/plain")
            .build();
    given(boardFileService.upload(anyInt(), any(), any())).willReturn(fileDownloadDto);

    // when then
    mvc.perform(multipart("/file/upload/{menuId}", 1).file(validFile()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("uuid"))
        .andExpect(jsonPath("$.name").value("test.txt"))
        .andExpect(jsonPath("$.url").value("https://s3.url/test.txt"))
        .andExpect(jsonPath("$.size").value(5))
        .andExpect(jsonPath("$.type").value("text/plain"));
  }

  @DisplayName("존재하지 않는 메뉴에 업로드하면 404")
  @Test
  void uploadBoardFile_NotFound() throws Exception {
    // given
    given(boardFileService.upload(anyInt(), any(), any())).willThrow(new NotFoundException());

    // when then
    mvc.perform(multipart("/file/upload/{menuId}", 1).file(validFile()))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("G004"));
  }

  @DisplayName("menuId 타입이 유효하지 않으면 400")
  @Test
  void uploadBoardFile_InvalidMenuIdType() throws Exception {
    // when then
    mvc.perform(multipart("/file/upload/{menuId}", "invalid").file(validFile()))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("G003"));
  }
}
