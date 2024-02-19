package com.inhabas.api.domain.normalBoard.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@NoArgsConstructor
public class SaveNormalBoardDto {
  @NotBlank
  private String title;

  @NotBlank
  private String content;

  private List<MultipartFile> files;

  private Integer pinOption;

  public SaveNormalBoardDto(String title, String content, List<MultipartFile> files, Integer pinOption) {
    this.title = title;
    this.content = content;
    this.files = files;
    this.pinOption = pinOption;
  }
}
