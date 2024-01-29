package com.inhabas.api.domain.policy.usecase;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.domain.policy.domain.PolicyTerm;
import com.inhabas.api.domain.policy.dto.PolicyTermDto;
import com.inhabas.api.domain.policy.dto.SavePolicyTernDto;
import com.inhabas.api.domain.policy.respository.PolicyTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PolicyTermServiceImpl implements PolicyTermService {

  private final PolicyTermRepository policyTermRepository;

  @Override
  @Transactional(readOnly = true)
  public PolicyTermDto findPolicyTerm(Long policyTermId) {

    PolicyTerm policyTerm =
        policyTermRepository.findById(policyTermId).orElseThrow(NotFoundException::new);

    return PolicyTermDto.builder()
        .title(policyTerm.getPolicyType().getTitle().getValue())
        .content(policyTerm.getContent().getValue())
        .build();
  }

  @Override
  @Transactional
  public void updatePolicyTerm(Long policyTermId, SavePolicyTernDto savePolicyTernDto) {

    PolicyTerm policyTerm =
        policyTermRepository.findById(policyTermId).orElseThrow(NotFoundException::new);
    policyTerm.updatePolicyTerm(savePolicyTernDto);
  }
}
