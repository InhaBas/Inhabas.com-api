package com.inhabas.api.domain.project.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.domain.board.domain.BaseBoard;
import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.menu.domain.Menu;

@Entity
@Table(name = "NORMAL_BOARD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("PROJECT")
public class ProjectBoard extends BaseBoard {

  @Embedded private Content content;

  @Column private Boolean isPinned = false;

  @Column(columnDefinition = "DATETIME(0)")
  private LocalDateTime datePinExpiration;

  @OneToMany(mappedBy = "parentBoard", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Comment> comments = new ArrayList<>();

  /* constructor */

  public ProjectBoard(
      String title, Menu menu, String content, Boolean isPinned, LocalDateTime datePinExpiration) {
    super(title, menu);
    this.content = new Content(content);
    this.isPinned = isPinned;
    this.datePinExpiration = datePinExpiration;
  }

  /* getter */

  public Boolean getPinned() {
    return isPinned;
  }

  public LocalDateTime getDatePinExpiration() {
    return datePinExpiration;
  }

  public String getContent() {
    return content.getValue();
  }

  public List<BoardFile> getFiles() {
    return Collections.unmodifiableList(files);
  }

  /* relation method */

  public void updateText(String title, String content) {
    this.title = new Title(title);
    this.content = new Content(content);
  }

  public void updatePinned(Boolean isPinned, LocalDateTime datePinExpiration) {
    this.isPinned = isPinned;
    this.datePinExpiration = datePinExpiration;
  }
}
