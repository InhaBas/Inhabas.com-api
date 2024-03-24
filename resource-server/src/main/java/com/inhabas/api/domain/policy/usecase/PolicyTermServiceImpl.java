package com.inhabas.api.domain.policy.usecase;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.domain.policy.domain.PolicyTerm;
import com.inhabas.api.domain.policy.dto.PolicyTermDto;
import com.inhabas.api.domain.policy.dto.SavePolicyTernDto;
import com.inhabas.api.domain.policy.respository.PolicyTermRepository;

@Service
@RequiredArgsConstructor
public class PolicyTermServiceImpl implements PolicyTermService {

  private final PolicyTermRepository policyTermRepository;

  @Transactional(readOnly = true)
  @Override
  public List<PolicyTermDto> getAllPolicyTerm() {
    List<PolicyTerm> policyTermList = policyTermRepository.findAll();

    return policyTermList.stream()
        .map(
            policyTerm ->
                PolicyTermDto.builder()
                    .id(policyTerm.getId())
                    .policyTypeId(policyTerm.getPolicyType().getId())
                    .title(policyTerm.getPolicyType().getTitle())
                    .content(policyTerm.getContent())
                    .build())
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public PolicyTermDto findPolicyTerm(Long policyTermId) {

    PolicyTerm policyTerm =
        policyTermRepository.findById(policyTermId).orElseThrow(NotFoundException::new);

    return PolicyTermDto.builder()
        .id(policyTerm.getId())
        .policyTypeId(policyTerm.getPolicyType().getId())
        .title(policyTerm.getPolicyType().getTitle())
        .content(policyTerm.getContent())
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
