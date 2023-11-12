package com.inhabas.api.domain.board.domain;

import com.inhabas.api.domain.board.BaseBoard;
import com.inhabas.api.domain.board.BoardCannotModifiableException;
import com.inhabas.api.domain.board.domain.valueObject.Contents;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.file.BoardFile;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
public class NormalBoard extends BaseBoard {

    @Embedded
    protected Contents contents;

    @OneToMany(mappedBy = "parentBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "parentBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    protected Set<BoardFile> files = new HashSet<>();


    /* constructor */

    public NormalBoard(String title, String contents) {
        this.title = new Title(title);
        this.contents = new Contents(contents);
    }


    /* getter */

    public String getContents() {
        return contents.getValue();
    }

    public Set<BoardFile> getFiles() {
        return Collections.unmodifiableSet(files);
    }


    /* relation method */

    public NormalBoard addFiles(Set<BoardFile> UploadFiles) {
        if (Objects.nonNull(UploadFiles))
            UploadFiles.forEach(this::addFile);

        return this;
    }

    public void addFile(BoardFile uploadFile) {
        files.add(uploadFile);
        uploadFile.toBoard(this);
    }

    public void addComment(Comment newComment) {
        comments.add(newComment);
    }

    public void modify(String title, String contents, StudentId loginMember) {

        if (cannotModifiableBy(loginMember)) {
            throw new BoardCannotModifiableException();
        }

        this.title = new Title(title);
        this.contents = new Contents(contents);
    }

    public boolean cannotModifiableBy(StudentId loginMember) {
        return !this.writerId.equals(loginMember);
    }
}
