package com.inhabas.api.domain.club.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class SaveClubActivityDto {

    private String title;

    private String content;

    private List<MultipartFile> files;

    @Builder
    public SaveClubActivityDto(String title, String content, List<MultipartFile> files) {
        this.title = title;
        this.content = content;
        this.files = files;
    }

}
