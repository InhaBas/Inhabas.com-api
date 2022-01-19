package com.inhabas.api.dto.board;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardDto {
    private final Integer id;
    private final String title;
    private final String contents; // BoardListView에서 null
    private final String writerName;
    private final Integer categoryId;
    private final LocalDateTime created;
    private final LocalDateTime updated;


    public BoardDto(Integer id, String title, String contents, String name, Integer categoryId, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.writerName = name;
        this.categoryId = categoryId;
        this.created = created;
        this.updated = updated;
    }
}
