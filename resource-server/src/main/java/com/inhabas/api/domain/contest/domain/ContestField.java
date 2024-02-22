package com.inhabas.api.domain.contest.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.contest.domain.valueObject.ContestFieldName;

@Entity
@Table(name = "CONTEST_FIELD")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class ContestField extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Embedded private ContestFieldName name;

  // 테스트 목적으로만 사용
  public ContestField(Long id) {
    this.id = id;
  }

  @Builder
  public ContestField(String name) {
    this.name = new ContestFieldName(name);
  }
}
