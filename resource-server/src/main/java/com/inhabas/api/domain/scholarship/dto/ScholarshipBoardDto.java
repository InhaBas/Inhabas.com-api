package com.inhabas.api.domain.scholarship.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
public class ScholarshipBoardDto {

  @NotNull @Positive private Long id;

  @NotBlank private String title;

  @NotNull private Long writerId;

  @NotBlank private String writerName;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime dateCreated;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime dateUpdated;

  @Builder
  public ScholarshipBoardDto(
      Long id, String title, Member writer, LocalDateTime dateCreated, LocalDateTime dateUpdated) {
    this.id = id;
    this.title = title;
    this.writerId = writer.getId();
    this.writerName = writer.getName();
    this.dateCreated = dateCreated;
    this.dateUpdated = dateUpdated;
  }
}
