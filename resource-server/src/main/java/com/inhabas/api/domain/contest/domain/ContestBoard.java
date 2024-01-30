package com.inhabas.api.domain.contest.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.inhabas.api.domain.board.domain.NormalBoard;
import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.contest.domain.valueObject.Association;
import com.inhabas.api.domain.contest.domain.valueObject.Topic;

@Entity
@Table(name = "CONTEST")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContestBoard extends NormalBoard {

  @Embedded private Topic topic;

  @Embedded private Association association;

  @Column(name = "DATE_CONTEST_START", nullable = false)
  private LocalDate dateContestStart;

  @Column(name = "DATE_CONTEST_END", nullable = false)
  private LocalDate dateContestEnd;

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

  //    public void modify(String title, String contents, String association, String topic,
  //            LocalDate start, LocalDate deadline, StudentId loginMember) {
  //
  //        super.modify(title, contents, loginMember);
  //        this.association = new Association(association);
  //        this.topic = new Topic(topic);
  //        this.start = start;
  //        this.deadline = deadline;
  //    }
}
