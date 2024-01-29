package com.inhabas.api.domain.comment.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WriterInfo {

  @NotNull @Positive private Long id;

  @NotBlank private String name;

  @NotBlank private String major;

  private String pictureUrl;

  public WriterInfo(Long id, String name, String major, String pictureUrl) {
    this.id = id;
    this.name = name;
    this.major = major;
    this.pictureUrl = pictureUrl;
  }
}
