package com.inhabas.api.domain.file.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class FileUploadDto {

  private String name;
  private String url;
  private MultipartFile multipartFile;

  @Builder
  public FileUploadDto(String url, String name, MultipartFile multipartFile) {
    this.url = url;
    this.name = name;
    this.multipartFile = multipartFile;
  }
}
