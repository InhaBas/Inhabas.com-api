package com.inhabas.api.domain.scholarship.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.domain.board.domain.BaseBoard;
import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.file.domain.BoardFile;
import com.inhabas.api.domain.menu.domain.Menu;

@Getter
@Entity
@Table(name = "SCHOLARSHIP_BOARD")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorValue("SCHOLARSHIP")
public class Scholarship extends BaseBoard {

  @Embedded private Content content;

  @Column(name = "DATE_HISTORY", nullable = false, columnDefinition = "DATETIME(0)")
  private LocalDateTime dateHistory;

  /* constructor */
  public Scholarship(String title, Menu menu, String content, LocalDateTime dateHistory) {
    super(title, menu);
    this.content = new Content(content);
    this.dateHistory = dateHistory;
  }

  /* getter */
  public String getContent() {
    return content.getValue();
  }

  public List<BoardFile> getFiles() {
    return Collections.unmodifiableList(files);
  }

  public void updateText(String title, String content, LocalDateTime dateHistory) {
    this.title = new Title(title);
    this.content = new Content(content);
    this.dateHistory = dateHistory;
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
