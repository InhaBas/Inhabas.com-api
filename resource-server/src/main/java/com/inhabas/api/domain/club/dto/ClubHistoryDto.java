package com.inhabas.api.domain.club.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.budget.domain.valueObject.Title;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class ClubHistoryDto {

    @NotNull
    @Positive
    private Long id;

    private Title title;

    private Content content;

    @NotNull
    @Positive
    private Long writerId;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", example = "2023-11-01T00:00:00")
    private LocalDateTime dateHistory;
}
