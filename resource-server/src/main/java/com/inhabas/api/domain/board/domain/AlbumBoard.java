package com.inhabas.api.domain.board.domain;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.menu.domain.Menu;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ALBUM_BOARD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@DiscriminatorValue("ALBUM")
public class AlbumBoard extends BaseBoard {

    @Column
    private Content content;

    @Builder
    public AlbumBoard(String title, Member writer, Menu menu, String content) {
        super(title, writer, menu);
        this.content = new Content(content);
    }

    public String getContent() {
        return content.getValue();
    }

    public void updateText(String title, String content) {
        this.title = new Title(title);
        this.content = new Content(content);
    }

    public void updateFiles(List<BoardFile> files) {

        if (this.files != null) {
            this.files.clear();
        } else {
            this.files = new ArrayList<>();
        }

        for (BoardFile file : files) {
            addFile(file);
        }

    }

}