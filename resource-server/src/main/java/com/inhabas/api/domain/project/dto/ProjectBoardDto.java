package com.inhabas.api.domain.project.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@NoArgsConstructor
public class ProjectBoardDto {
  @NotNull @Positive private Long id;

  @NotBlank private String title;

  @NotNull private Long writerId;

  @NotBlank private String writerName;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  private LocalDateTime datePinExpiration;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  private LocalDateTime dateCreated;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2025-11-01T00:00:00")
  private LocalDateTime dateUpdated;

  @NotNull private Boolean isPinned;

  @Builder
  public ProjectBoardDto(
      Long id,
      String title,
      Long writerId,
      String writerName,
      LocalDateTime datePinExpiration,
      LocalDateTime dateCreated,
      LocalDateTime dateUpdated,
      Boolean isPinned) {
    this.id = id;
    this.title = title;
    this.writerId = writerId;
    this.writerName = writerName;
    this.datePinExpiration = datePinExpiration;
    this.dateCreated = dateCreated;
    this.dateUpdated = dateUpdated;
    this.isPinned = isPinned;
  }
}
