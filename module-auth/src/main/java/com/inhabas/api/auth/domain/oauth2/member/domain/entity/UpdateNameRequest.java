package com.inhabas.api.auth.domain.oauth2.member.domain.entity;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus.PENDING;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Name;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;

@Entity
@Table(name = "UPDATE_NAME_REQUEST")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class UpdateNameRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "USER_ID")
  private Member member;

  @Embedded private Name name;

  @CreatedDate
  @Column(
      name = "DATE_REQUESTED",
      nullable = false,
      updatable = false,
      columnDefinition = "DATETIME(0) DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime dateRequested;

  @Column(
      name = "REQUEST_STATUS",
      nullable = false,
      columnDefinition = "VARCHAR(20) DEFAULT 'PENDING'")
  private RequestStatus requestStatus;

  @Column(name = "REJECT_REASON", length = 255)
  private String rejectReason;

  @Builder
  public UpdateNameRequest(Member member, String name) {
    this.member = member;
    this.name = new Name(name);
    this.dateRequested = LocalDateTime.now();
    this.requestStatus = PENDING;
  }
}
