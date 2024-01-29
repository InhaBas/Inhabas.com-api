package com.inhabas.api.domain.file.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileDownloadDto {

  @NotBlank private String name;

  @NotBlank private String url;

  @Builder
  public FileDownloadDto(String name, String url) {
    this.name = name;
    this.url = url;
  }
}
