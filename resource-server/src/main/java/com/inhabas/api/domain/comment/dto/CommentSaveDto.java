package com.inhabas.api.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentSaveDto {

  @NotBlank
  @Length(max = 499, message = "500자 이하여야 합니다.")
  private String content;

  @Positive private Long parentCommentId;

  public CommentSaveDto(String content, Long parentCommentId) {
    this.content = content;
    this.parentCommentId = parentCommentId;
  }

  @JsonIgnore
  public boolean isNotRootComment() {
    return this.parentCommentId != null;
  }
}
