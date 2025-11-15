package com.inhabas.api.domain.myInfo.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;
import io.swagger.v3.oas.annotations.media.Schema;

@NoArgsConstructor
@Getter
public class MyCommentDto {

  // ParentsBoard의 게시판 id
  @NotNull @Positive private Long id;

  // ParentsBoard의 menuId
  @NotNull private Integer menuId;

  // ParentsBoard의 menuType
  @NotNull private MenuType menuType;

  // ParentsBoard의 메뉴 이름
  @NotNull private String menuName;

  // 댓글의 내용
  @NotBlank private String content;

  @NotNull private Boolean isDeleted;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime dateCreated;

  @Builder
  public MyCommentDto(
      Long id,
      Integer menuId,
      MenuType menuType,
      String menuName,
      String content,
      Boolean isDeleted,
      LocalDateTime dateCreated) {
    this.id = id;
    this.menuId = menuId;
    this.menuType = menuType;
    this.menuName = menuName;
    this.content = content;
    this.isDeleted = isDeleted;
    this.dateCreated = dateCreated;
  }
}
