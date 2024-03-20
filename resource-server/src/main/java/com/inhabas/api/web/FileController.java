package com.inhabas.api.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import com.inhabas.api.domain.file.usecase.BoardFileService;
import com.inhabas.api.global.dto.PagedResponseDto;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Slf4j
@Tag(name = "첨부파일 관리")
@RestController
@RequiredArgsConstructor
public class FileController {

  private final BoardFileService boardFileService;

  @Operation(summary = "게시판 첨부파일 업로드")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = PagedResponseDto.class))}),
        @ApiResponse(
            responseCode = "400",
            description = "입력값이 없거나, 타입이 유효하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"))),
        @ApiResponse(
            responseCode = "404",
            description = "데이터가 존재하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 404, \"code\": \"G004\", \"message\": \"데이터가 존재하지 않습니다.\"}")))
      })
  @PostMapping(value = "/file/upload/{menuId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize(
      "@boardSecurityChecker.checkMenuAccess(#menuId, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).CREATE_BOARD)")
  public FileDownloadDto uploadBoardFile(
      @Authenticated Long memberId,
      @PathVariable Integer menuId,
      @RequestParam("file") MultipartFile file) {
    return boardFileService.upload(menuId, file, memberId);
  }
}
