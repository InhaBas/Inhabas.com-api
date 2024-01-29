package com.inhabas.api.domain.policy.domain;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.inhabas.api.domain.board.domain.valueObject.Title;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PolicyType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "TITLE")
  private Title title;

  public PolicyType(String title) {
    this.title = new Title(title);
  }
}
