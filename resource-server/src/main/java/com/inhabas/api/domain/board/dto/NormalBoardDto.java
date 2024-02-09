package com.inhabas.api.domain.board.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class NormalBoardDto {
  private Long id;
  private String title;
  private String writerName;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime dateCreated;

  @NotNull
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
  @Schema(type = "string", example = "2024-11-01T00:00:00")
  private LocalDateTime dateUpdated;

  @NotNull
  private Boolean isPinned;

  @Builder
  public NormalBoardDto(Long id, String title, String writerName, LocalDateTime dateCreated, LocalDateTime dateUpdated, Boolean isPinned) {
    this.id = id;
    this.title = title;
    this.writerName = writerName;
    this.dateCreated = dateCreated;
    this.dateUpdated = dateUpdated;
    this.isPinned = isPinned;
  }

}
