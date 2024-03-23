package com.inhabas.api.domain.myInfo.dto;

import java.time.LocalDateTime;

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
public class MyBoardDto {

  // 게시글 id
  @NotNull @Positive private Long id;

  @NotNull private Integer menuId;

  @NotNull private String menuName;

  @NotBlank private String title;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime dateCreated;

  @Builder
  public MyBoardDto(
      Long id, Integer menuId, String menuName, String title, LocalDateTime dateCreated) {
    this.id = id;
    this.menuId = menuId;
    this.menuName = menuName;
    this.title = title;
    this.dateCreated = dateCreated;
  }
}
