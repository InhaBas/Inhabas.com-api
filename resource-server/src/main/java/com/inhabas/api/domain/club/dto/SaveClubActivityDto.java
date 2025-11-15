package com.inhabas.api.domain.club.dto;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SaveClubActivityDto {

  @NotBlank private String title;

  @NotBlank private String content;

  @NotNull
  @Size(min = 1)
  private List<String> files = new ArrayList<>();

  @Builder
  public SaveClubActivityDto(String title, String content, List<String> files) {
    this.title = title;
    this.content = content;
    this.files = (files != null) ? files : new ArrayList<>();
  }
}
