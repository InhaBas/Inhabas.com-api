package com.inhabas.api.domain.project.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class SaveProjectBoardDto {

  @NotBlank private String title;

  @NotBlank private String content;

  private List<MultipartFile> files;

  private Integer pinOption;

  @Builder
  public SaveProjectBoardDto(
      String title, String content, List<MultipartFile> files, Integer pinOption) {
    this.title = title;
    this.content = content;
    this.files = files;
    this.pinOption = pinOption;
  }
}
