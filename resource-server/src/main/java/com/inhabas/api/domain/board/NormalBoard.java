package com.inhabas.api.domain.board;

import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.board.type.wrapper.Contents;
import com.inhabas.api.domain.board.type.wrapper.Title;
import com.inhabas.api.domain.comment.Comment;
import com.inhabas.api.domain.file.BoardFile;
import com.inhabas.api.domain.member.MemberId;
import com.inhabas.api.domain.menu.MenuId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Table(name = "normal_board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "TYPE", length = 15)
public class NormalBoard extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Embedded
    protected Title title;

    @Embedded
    protected Contents contents;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "menu_id"))
    protected MenuId menuId;

    @Embedded
    @AttributeOverride(name = "id", column = @Column(name = "writer_id"))
    protected MemberId writerId;

    @OneToMany(mappedBy = "parentBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "parentBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    protected Set<BoardFile> files = new HashSet<>();

    /* constructor */

    public NormalBoard(Integer id, String title, String contents) {
        this.id = id;
        this.title = new Title(title);
        this.contents = new Contents(contents);
    }

    public NormalBoard(String title, String contents) {
        this.title = new Title(title);
        this.contents = new Contents(contents);
    }


    /* getter */

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title.getValue();
    }

    public String getContents() {
        return contents.getValue();
    }

    public MenuId getMenuId() {
        return menuId;
    }

    public MemberId getWriterId() {
        return writerId;
    }

    public Set<BoardFile> getFiles() {
        return Collections.unmodifiableSet(files);
    }

    /* relation method */

    @SuppressWarnings("unchecked")
    public <T extends NormalBoard> T writtenBy(MemberId writerId){

        if (Objects.isNull(this.writerId)) {
            this.writerId = writerId;
            return (T) this;
        }
        else {
            throw new IllegalStateException("게시글 작성자를 수정할 수 없습니다.");
        }
    }

    public NormalBoard addFiles(Set<BoardFile> UploadFiles) {
        if (Objects.nonNull(UploadFiles))
            UploadFiles.forEach(this::addFile);

        return this;
    }

    public boolean isWriter(MemberId writerId) {
        return this.writerId.equals(writerId);
    }

    public void addFile(BoardFile uploadFile) {
        files.add(uploadFile);
        uploadFile.toBoard(this);
    }

    public void addComment(Comment newComment) {
        comments.add(newComment);
    }

    @SuppressWarnings("unchecked")
    public <T extends NormalBoard> T inMenu(MenuId menuId) {
        this.menuId = menuId;
        return (T) this;
    }
}
