package com.inhabas.api.domain.myInfo.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Builder;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

public class MyCommentsDto {

  // ParentsBoard의 id
  @NotNull @Positive private Long id;

  // ParentsBoard의 메뉴 이름
  @NotNull private String menuName;

  // 댓글의 내용
  @NotBlank private String content;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  String dateCreated;

  @Builder
  MyCommentsDto(Long id, String menuName, String content, String dateCreated) {
    this.id = id;
    this.menuName = menuName;
    this.content = content;
    this.dateCreated = dateCreated;
  }
}
