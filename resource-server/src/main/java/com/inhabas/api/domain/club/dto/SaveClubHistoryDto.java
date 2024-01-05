package com.inhabas.api.domain.club.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SaveClubHistoryDto {

    private String title;

    private String content;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", example = "2023-11-01T00:00:00")
    private LocalDateTime dateHistory;

    @Builder
    public SaveClubHistoryDto(String title, String content, LocalDateTime dateHistory) {
        this.title = title;
        this.content = content;
        this.dateHistory = dateHistory;
    }

}
