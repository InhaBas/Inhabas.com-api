package com.inhabas.api.dto.board;

import com.inhabas.api.domain.board.Category;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class BoardListDto {
    private Integer id;
    @NotBlank
    private String title;
    @NotBlank
    private String name;
    @NotNull
    private Category category;
    private LocalDate created;
    private LocalDate updated;

    public BoardListDto(Integer id, String title, String name, LocalDate created, LocalDate updated, Category category) {
        this.id = id;
        this.title = title;
        this.name = name;
        this.created = created;
        this.updated = updated;
        this.category = category;
    }
}
