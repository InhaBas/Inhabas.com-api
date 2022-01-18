package com.inhabas.api.dto.board;

import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.member.Member;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class BoardDetailDto {
    private String title;
    private String contents;
    private String name;
    private LocalDateTime created;
    private LocalDateTime updated;
    private Category category;

    public BoardDetailDto(String title, String contents, String name, LocalDateTime created, LocalDateTime updated, Category category) {
        this.title = title;
        this.contents = contents;
        this.name = name;
        this.created = created;
        this.updated = updated;
        this.category = category;
    }
}