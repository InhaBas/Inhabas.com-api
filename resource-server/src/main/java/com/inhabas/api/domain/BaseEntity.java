package com.inhabas.api.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import lombok.Getter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {

  @CreatedDate
  @Column(
      nullable = false,
      updatable = false,
      insertable = false,
      columnDefinition = "DATETIME(0) DEFAULT CURRENT_TIMESTAMP")
  private LocalDateTime dateCreated;

  @CreatedDate
  @LastModifiedDate
  @Column(columnDefinition = "DATETIME(0)")
  private LocalDateTime dateUpdated;
}
