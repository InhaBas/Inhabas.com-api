package com.inhabas.api.domain.board.domain;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.board.domain.valueObject.Title;
 import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.menu.domain.Menu;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Entity
@DiscriminatorColumn(name = "TYPE", length = 15)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Embedded
    protected Title title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_BOARD_OF_USER_ID"))
    protected Member writer;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MENU_ID", foreignKey = @ForeignKey(name = "FK_BOARD_OF_MENU_ID"))
    protected Menu menu;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<BoardFile> files = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<Comment> comments = new ArrayList<>();

    public <T extends BaseBoard> T writtenBy(Member writer){

        if (Objects.isNull(this.writer)) {
            this.writer = writer;
            return (T) this;
        }
        else {
            throw new IllegalStateException("게시글 작성자를 수정할 수 없습니다.");
        }
    }

    public boolean isWriter(Member writer) {
        return this.writer.equals(writer);
    }

    public String getTitle() {
        return title.getValue();
    }

    public BaseBoard(String title, Member writer, Menu menu) {
        this.title = new Title(title);
        this.writer = writer;
        this.menu = menu;
    }

    public void addFile(BoardFile file) {
        if (this.files == null) {
            this.files = new ArrayList<>();
        }

        this.files.add(file);
    }

    public void addComment(Comment newComment) {
        comments.add(newComment);
    }

}
