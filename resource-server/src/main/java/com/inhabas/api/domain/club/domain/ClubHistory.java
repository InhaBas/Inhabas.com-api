package com.inhabas.api.domain.club.domain;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.club.dto.SaveClubHistoryDto;

@Entity
@Table(name = "CLUB_HISTORY")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ClubHistory extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "USER_ID", foreignKey = @ForeignKey(name = "FK_MEMBER_OF_CLUB_HISTORY"))
  private Member member;

  @Embedded private Title title;

  @Embedded private Content content;

  @Column(name = "DATE_HISTORY", nullable = false, columnDefinition = "DATETIME(0)")
  private LocalDateTime dateHistory;

  @Builder
  public ClubHistory(Member member, Title title, Content content, LocalDateTime dateHistory) {
    this.member = member;
    this.title = title;
    this.content = content;
    this.dateHistory = dateHistory;
  }

  public void updateClubHistory(Member member, SaveClubHistoryDto saveClubHistoryDto) {
    this.member = member;
    this.title = new Title(saveClubHistoryDto.getTitle());
    this.content = new Content(saveClubHistoryDto.getContent());
    this.dateHistory = saveClubHistoryDto.getDateHistory();
  }
}
