package com.inhabas.api.domain.file.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class FileDownloadDto {

    @NotBlank
    private String name;

    @NotBlank
    private String url;

    @Builder
    public FileDownloadDto(String name, String url) {
        this.name = name;
        this.url = url;
    }

}
