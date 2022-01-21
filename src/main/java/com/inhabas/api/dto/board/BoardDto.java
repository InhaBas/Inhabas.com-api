package com.inhabas.api.dto.board;

import com.inhabas.api.domain.board.type.wrapper.Contents;
import com.inhabas.api.domain.board.type.wrapper.Title;
import com.inhabas.api.domain.member.type.wrapper.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardDto {
    private Integer id;
    private String title;
    private String contents; // BoardListView에서 null
    private String writerName;
    private Integer categoryId;
    private LocalDateTime created;
    private LocalDateTime updated;

//    public BoardDto(Integer id, String title, String contents, String name, Integer categoryId, LocalDateTime created, LocalDateTime updated) {
//        this.id = id;
//        this.title = title;
//        this.contents = contents;
//        this.writerName = name;
//        this.categoryId = categoryId;
//        this.created = created;
//        this.updated = updated;
//    }

    public void setTitle(Title title) {
        this.title = title.getValue();
    }

    public void setContents(Contents contents) {
        this.contents = contents.getValue();
    }

    public void setWriterName(Name name) {
        this.writerName = name.getValue();
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }
}
