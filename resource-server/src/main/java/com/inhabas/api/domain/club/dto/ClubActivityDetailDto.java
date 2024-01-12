package com.inhabas.api.domain.club.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inhabas.api.domain.file.dto.FileDownloadDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ClubActivityDetailDto {

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotBlank
    private String writerName;

    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss")
    @Schema(type="string" , example = "2024-11-01T00:00:00")
    private LocalDateTime dateUpdated;

    @NotNull
    private List<FileDownloadDto> files;

    @Builder
    public ClubActivityDetailDto(String title, String content, String writerName, LocalDateTime dateUpdated, List<FileDownloadDto> files) {
        this.title = title;
        this.content = content;
        this.writerName = writerName;
        this.dateUpdated = dateUpdated;
        this.files = files;
    }

}
