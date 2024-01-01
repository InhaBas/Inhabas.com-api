package com.inhabas.api.domain.club.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.club.domain.entity.ClubHistory;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
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

    @Builder
    public ClubHistoryDto(Long id, Title title, Content content, Long writerId, LocalDateTime dateHistory) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writerId = writerId;
        this.dateHistory = dateHistory;
    }

    public ClubHistoryDto(ClubHistory clubHistory) {
        this.id = clubHistory.getId();
        this.title = clubHistory.getTitle();
        this.content = clubHistory.getContent();
        this.writerId = clubHistory.getMember().getId();
        this.dateHistory = clubHistory.getDateHistory();
    }
}
