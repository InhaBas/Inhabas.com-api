package com.inhabas.api.domain.board.domain;

import java.util.*;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.board.exception.OnlyWriterModifiableException;
import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.file.domain.BoardFile;

@Entity
@Table(name = "normal_board")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("NORMAL")
public class NormalBoard extends BaseBoard {

  @Embedded protected Content content;

  @OneToMany(mappedBy = "parentBoard", cascade = CascadeType.ALL, orphanRemoval = true)
  protected List<Comment> comments = new ArrayList<>();

  /* constructor */

  public NormalBoard(String title, String contents) {
    this.title = new Title(title);
    this.content = new Content(contents);
  }

  /* getter */

  public String getContent() {
    return content.getValue();
  }

  public List<BoardFile> getFiles() {
    return Collections.unmodifiableList(files);
  }

  /* relation method */

  public NormalBoard addFiles(Set<BoardFile> UploadFiles) {
    if (Objects.nonNull(UploadFiles)) UploadFiles.forEach(this::addFile);

    return this;
  }

  public void addFile(BoardFile uploadFile) {
    files.add(uploadFile);
    uploadFile.toBoard(this);
  }

  public void addComment(Comment newComment) {
    comments.add(newComment);
  }

  public void modify(String title, String contents, Member writer) {

    if (cannotModifiableBy(writer)) {
      throw new OnlyWriterModifiableException();
    }

    this.title = new Title(title);
    this.content = new Content(contents);
  }

  public boolean cannotModifiableBy(Member writer) {
    return !this.writer.equals(writer);
  }
}
