package com.inhabas.api.domain.comment.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.comment.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
public class CommentDetailDto {

  @NotNull @Positive private Long id;

  @JsonProperty("writer")
  private WriterInfo writerInfo;

  @NotBlank private String content;

  @NotNull private Boolean isDeleted;

  private List<CommentDetailDto> childrenComment = new ArrayList<>();

  private static final String DELETED_MESSAGE = "삭제된 댓글입니다.";

  @NotBlank
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime dateUpdated;

  public static CommentDetailDto fromEntity(Comment comment) {
    return comment.getIsDeleted()
        ? new CommentDetailDto(
            comment.getId(),
            comment.getWriter(),
            DELETED_MESSAGE,
            comment.getIsDeleted(),
            comment.getDateUpdated())
        : new CommentDetailDto(
            comment.getId(),
            comment.getWriter(),
            comment.getContent(),
            comment.getIsDeleted(),
            comment.getDateUpdated());
  }

  public CommentDetailDto(
      Long id, Member writer, String content, Boolean isDeleted, LocalDateTime dateUpdated) {
    this.id = id;
    if (writer != null) {
      this.writerInfo =
          new WriterInfo(
              writer.getId(),
              writer.getName(),
              writer.getSchoolInformation().getMajor(),
              writer.getPicture());
    }
    this.content = content;
    this.isDeleted = isDeleted;
    this.dateUpdated = dateUpdated;
  }
}
