package com.inhabas.api.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

import lombok.Getter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {

  @CreatedDate
  @Column(nullable = false, updatable = false, columnDefinition = "DATETIME(0)")
  private LocalDateTime dateCreated;

  @CreatedDate
  @LastModifiedDate
  @Column(columnDefinition = "DATETIME(0)")
  private LocalDateTime dateUpdated;
}
