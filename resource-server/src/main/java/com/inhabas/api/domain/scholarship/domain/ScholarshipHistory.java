package com.inhabas.api.domain.scholarship.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.scholarship.dto.SaveScholarshipHistoryDto;

@Entity
@Table(name = "SCHOLARSHIP_HISTORY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ScholarshipHistory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_MEMBER_OF_SCHOLARSHIP_HISTORY"))
  private Member writer;

  @Embedded private Title title;

  @Column(name = "DATE_HISTORY", nullable = false, columnDefinition = "DATETIME(0)")
  private LocalDateTime dateHistory;

  @Builder
  public ScholarshipHistory(Member writer, Title title, LocalDateTime dateHistory) {
    this.writer = writer;
    this.title = title;
    this.dateHistory = dateHistory;
  }

  public void update(Member writer, SaveScholarshipHistoryDto saveScholarshipHistoryDto) {
    this.writer = writer;
    this.title = new Title(saveScholarshipHistoryDto.getTitle());
    this.dateHistory = saveScholarshipHistoryDto.getDateHistory();
  }
}
