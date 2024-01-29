package com.inhabas.api.domain.club.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class SaveClubActivityDto {

  @NotBlank private String title;

  @NotBlank private String content;

  private List<MultipartFile> files;

  @Builder
  public SaveClubActivityDto(String title, String content, List<MultipartFile> files) {
    this.title = title;
    this.content = content;
    this.files = (files != null) ? files : new ArrayList<>();
  }
}
