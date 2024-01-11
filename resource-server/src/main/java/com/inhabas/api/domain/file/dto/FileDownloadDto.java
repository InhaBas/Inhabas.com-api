package com.inhabas.api.domain.file.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FileDownloadDto {

    private String name;
    private String url;

    @Builder
    public FileDownloadDto(String name, String url) {
        this.name = name;
        this.url = url;
    }

}
