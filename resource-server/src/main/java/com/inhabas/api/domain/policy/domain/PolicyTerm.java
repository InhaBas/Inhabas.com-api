package com.inhabas.api.domain.policy.domain;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.policy.dto.SavePolicyTernDto;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "POLICY_TERM")
@Getter
public class PolicyTerm extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "POLICY_TYPE_ID")
  private PolicyType policyType;

  @Embedded private Content content;

  @Builder
  public PolicyTerm(PolicyType policyType, String content) {
    this.policyType = policyType;
    this.content = new Content(content);
  }

  public void updatePolicyTerm(SavePolicyTernDto savePolicyTernDto) {
    this.content = new Content(savePolicyTernDto.getContent());
  }
}
