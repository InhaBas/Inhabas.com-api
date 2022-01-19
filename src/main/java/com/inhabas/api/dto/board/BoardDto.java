package com.inhabas.api.dto.board;

import java.time.LocalDateTime;

public class BoardDto {
    private Integer id; // BoardDetailView에서 null..?
    private String title;
    private String contents; // BoardListView에서 null
    private String name;
    private Integer categoryId;
    private LocalDateTime created;
    private LocalDateTime updated;


    public BoardDto(Integer id, String title, String contents, String name, Integer categoryId, LocalDateTime created, LocalDateTime updated) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.name = name;
        this.categoryId = categoryId;
        this.created = created;
        this.updated = updated;
    }
}
