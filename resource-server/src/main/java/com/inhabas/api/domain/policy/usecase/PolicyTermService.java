package com.inhabas.api.domain.policy.usecase;

import com.inhabas.api.domain.policy.dto.PolicyTermDto;
import com.inhabas.api.domain.policy.dto.SavePolicyTernDto;

public interface PolicyTermService {

    PolicyTermDto findPolicyTerm(Long policyTermId);

    void updatePolicyTerm(Long policyTermId, SavePolicyTernDto savePolicyTernDto);

}
