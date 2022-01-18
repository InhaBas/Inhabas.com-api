package com.inhabas.api.dto.board;

import com.inhabas.api.domain.board.Category;
import java.time.LocalDate;

public class BoardListDto {
    private Integer id;
    private String title;
    private String name;
    private LocalDate created;
    private LocalDate updated;
    private Category category;

    public BoardListDto(Integer id, String title, String name, LocalDate created, LocalDate updated, Category category) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.created = created;
        this.updated = updated;
        this.category = category;
    }
}
