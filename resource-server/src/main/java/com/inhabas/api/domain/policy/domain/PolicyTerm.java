package com.inhabas.api.domain.policy.domain;

import com.inhabas.api.domain.BaseEntity;
import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.policy.dto.SavePolicyTernDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "POLICY_TERM")
@Getter
public class PolicyTerm extends BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "POLICY_TYPE_ID")
    private PolicyType policyType;

    @Embedded
    private Content content;

    @Builder
    public PolicyTerm(PolicyType policyType, String content) {
        this.policyType = policyType;
        this.content = new Content(content);
    }

    public void updatePolicyTerm(SavePolicyTernDto savePolicyTernDto) {
        this.content = new Content(savePolicyTernDto.getContent());
    }

}
