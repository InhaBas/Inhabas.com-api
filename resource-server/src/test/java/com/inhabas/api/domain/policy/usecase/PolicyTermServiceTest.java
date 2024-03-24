package com.inhabas.api.domain.policy.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.Objects;
import java.util.Optional;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.domain.policy.domain.PolicyTerm;
import com.inhabas.api.domain.policy.domain.PolicyType;
import com.inhabas.api.domain.policy.dto.PolicyTermDto;
import com.inhabas.api.domain.policy.dto.SavePolicyTernDto;
import com.inhabas.api.domain.policy.respository.PolicyTermRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class PolicyTermServiceTest {

  @InjectMocks PolicyTermServiceImpl policyTermService;

  @Mock PolicyTermRepository policyTermRepository;

  @DisplayName("policyTermId로 policyTerm 을 조회한다.")
  @Test
  void findPolicyTermTest() {
    // given
    Long policyTermId = 1L;
    PolicyType policyType = new PolicyType("title"); // PolicyType에 적절한 생성자 또는 setter 메소드가 필요합니다.
    PolicyTerm policyTerm = PolicyTerm.builder().policyType(policyType).content("content").build();

    given(policyTermRepository.findById(policyTermId)).willReturn(Optional.ofNullable(policyTerm));

    // when
    PolicyTermDto result = policyTermService.findPolicyTerm(policyTermId);

    // then
    verify(policyTermRepository).findById(policyTermId);
    assertThat(result.getTitle())
        .isEqualTo(Objects.requireNonNull(policyTerm).getPolicyType().getTitle());
    assertThat(result.getContent()).isEqualTo(policyTerm.getContent());
  }

  @DisplayName("존재하지 않는 policyTermId 조회 시 NotFoundException")
  @Test
  void findPolicyTermTest_NotFound() {
    // given
    given(policyTermRepository.findById(any())).willReturn(Optional.empty());

    // then
    assertThatThrownBy(() -> policyTermService.findPolicyTerm(any()))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("데이터가 존재하지 않습니다.");
  }

  @DisplayName("policyTerm 을 수정한다.")
  @Test
  void updatePolicyTermTest() {
    // given
    Long policyTermId = 1L;
    SavePolicyTernDto savePolicyTermDto = new SavePolicyTernDto("content");
    PolicyTerm policyTerm = mock(PolicyTerm.class);

    given(policyTermRepository.findById(policyTermId)).willReturn(Optional.of(policyTerm));

    // when
    policyTermService.updatePolicyTerm(policyTermId, savePolicyTermDto);

    // then
    verify(policyTermRepository).findById(policyTermId);
    verify(policyTerm).updatePolicyTerm(savePolicyTermDto);
  }

  @DisplayName("존재하지 않는 policyTermId 수정 시 NotFoundException")
  @Test
  void updatePolicyTermTest_NotFound() {
    // given
    Long policyTermId = 1L;
    SavePolicyTernDto savePolicyTermDto = new SavePolicyTernDto("content");

    // then
    assertThatThrownBy(() -> policyTermService.updatePolicyTerm(policyTermId, savePolicyTermDto))
        .isInstanceOf(NotFoundException.class)
        .hasMessage("데이터가 존재하지 않습니다.");
  }
}
