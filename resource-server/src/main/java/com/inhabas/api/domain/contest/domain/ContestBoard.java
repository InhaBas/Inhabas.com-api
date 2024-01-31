package com.inhabas.api.domain.contest.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.domain.BaseBoard;
import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.board.exception.OnlyWriterModifiableException;
import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.contest.domain.valueObject.Association;
import com.inhabas.api.domain.contest.domain.valueObject.Topic;
import com.inhabas.api.domain.file.domain.BoardFile;

@Entity
@Table(name = "CONTEST")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@DiscriminatorValue("CONTEST")
public class ContestBoard extends BaseBoard {

  @Embedded private Topic topic;

  @Embedded private Association association;

  @Embedded private Content content;

  @Column(name = "DATE_CONTEST_START", nullable = false)
  private LocalDate dateContestStart;

  @Column(name = "DATE_CONTEST_END", nullable = false)
  private LocalDate dateContestEnd;

  @OneToMany(mappedBy = "parentBoard", cascade = CascadeType.ALL, orphanRemoval = true)
  protected List<Comment> comments = new ArrayList<>();

  /* Getter */
  public String getAssociation() {
    return association.getValue();
  }

  public String getTopic() {
    return topic.getValue();
  }

  /* Constructor */
  @Builder
  public ContestBoard(
      String title,
      String content,
      String association,
      String topic,
      LocalDate dateContestStart,
      LocalDate dateContestEnd) {

    this.title = new Title(title);
    this.content = new Content(content);
    this.association = new Association(association);
    this.topic = new Topic(topic);
    this.dateContestStart = dateContestStart;
    this.dateContestEnd = dateContestEnd;
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

  public void modify(
      String title,
      String content,
      Member writer,
      String association,
      String topic,
      LocalDate dateContestStart,
      LocalDate dateContestEnd) {

    if (cannotModifiableBy(writer)) {
      throw new OnlyWriterModifiableException();
    }

    this.association = new Association(association);
    this.topic = new Topic(topic);
    this.dateContestStart = dateContestStart;
    this.dateContestEnd = dateContestEnd;
  }

  public boolean cannotModifiableBy(Member writer) {
    return !this.writer.equals(writer);
  }
}
