package com.inhabas.api.domain.file.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileDownloadDto {

  @NotNull private String id;
  @NotBlank private String name;
  @NotBlank private String url;
  @NotNull private Long size;
  @NotNull private String type;

  @Builder
  public FileDownloadDto(String id, String name, String url, Long size, String type) {
    this.id = id;
    this.name = name;
    this.url = url;
    this.size = size;
    this.type = type;
  }
}
