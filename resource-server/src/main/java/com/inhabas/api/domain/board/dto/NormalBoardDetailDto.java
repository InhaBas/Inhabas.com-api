package com.inhabas.api.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class NormalBoardDetailDto {

    @NotNull
    @Positive
    private Long id;

    @NotBlank
    private String title;

    @NotBlank private String content;

    @NotBlank private String writerName;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", example = "2024-11-01T00:00:00")
    private LocalDateTime dateCreated;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type = "string", example = "2024-11-01T00:00:00")
    private LocalDateTime dateUpdated;

    @NotNull private List<FileDownloadDto> files;

    @NotNull
    private Boolean isPinned;

    @Builder
    public NormalBoardDetailDto(
            Long id,
            String title,
            String content,
            String writerName,
            LocalDateTime dateCreated,
            LocalDateTime dateUpdated,
            List<FileDownloadDto> files,
            Boolean isPinned) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writerName = writerName;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.files = files;
        this.isPinned = isPinned;
    }
}
