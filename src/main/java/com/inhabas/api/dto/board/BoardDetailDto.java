package com.inhabas.api.dto.board;

import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.member.Member;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
public class BoardDetailDto {
    @NotBlank
    private String title;
    @NotEmpty
    private String contents;
    @NotBlank
    private String name;
    @NotNull
    private Category category;

    private LocalDateTime created;
    private LocalDateTime updated;

    public BoardDetailDto(String title, String contents, String name, LocalDateTime created, LocalDateTime updated, Category category) {
        this.title = title;
        this.contents = contents;
        this.name = name;
        this.created = created;
        this.updated = updated;
        this.category = category;
    }
}