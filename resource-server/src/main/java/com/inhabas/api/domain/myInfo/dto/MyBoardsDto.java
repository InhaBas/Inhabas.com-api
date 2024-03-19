package com.inhabas.api.domain.myInfo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

@NoArgsConstructor
@Getter
public class MyBoardsDto {

  // 게시글 id
  @NotNull @Positive private Long id;

  @NotNull private String menuName;

  @NotBlank private String title;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  String dateCreated;

  @Builder
  MyBoardsDto(Long id, String menuName, String title, String dateCreated) {
    this.id = id;
    this.menuName = menuName;
    this.title = title;
    this.dateCreated = dateCreated;
  }
}
