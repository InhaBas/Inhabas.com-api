package com.inhabas.api.domain.board.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class SaveNormalBoardDto {
  @NotBlank
  private String title;

  @NotBlank
  private String content;

  private List<MultipartFile> files;

  @NotNull
  private Boolean isPinned;

  public SaveNormalBoardDto(String title, String content, List<MultipartFile> files, Boolean isPinned) {
    this.title = title;
    this.content = content;
    this.files = (files != null) ? files : new ArrayList<>();
    this.isPinned = isPinned;
  }
}
