package com.inhabas.api.domain.club.dto;

import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class SaveClubActivityDto {

    private Title title;

    private Content content;

    private List<MultipartFile> files;

    @Builder
    public SaveClubActivityDto(String title, String content, List<MultipartFile> files) {
        this.title = new Title(title);
        this.content = new Content(content);
        this.files = files;
    }

}
