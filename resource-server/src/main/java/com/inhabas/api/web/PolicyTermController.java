package com.inhabas.api.web;

import java.util.List;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inhabas.api.domain.policy.dto.PolicyTermDto;
import com.inhabas.api.domain.policy.dto.SavePolicyTernDto;
import com.inhabas.api.domain.policy.usecase.PolicyTermService;
import com.inhabas.api.global.swagger.Response200And400And404;
import com.inhabas.api.global.swagger.Response204And400And404;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "정책 관리")
@RestController
@RequiredArgsConstructor
public class PolicyTermController {

  private final PolicyTermService policyTermService;

  @GetMapping("/policies")
  @SecurityRequirements(value = {})
  @Operation(summary = "모든 정책을 조회한다.")
  public ResponseEntity<List<PolicyTermDto>> getAllPolicyTerm() {

    List<PolicyTermDto> policyTermDto = policyTermService.getAllPolicyTerm();
    return ResponseEntity.ok(policyTermDto);
  }

  @GetMapping("/policy/{policyTermId}")
  @SecurityRequirements(value = {})
  @Operation(summary = "해당 정책을 조회한다.", description = "policyTermId는 1,2,3만 존재")
  @Response200And400And404
  public ResponseEntity<PolicyTermDto> findPolicyTerm(@PathVariable Long policyTermId) {

    PolicyTermDto policyTermDto = policyTermService.findPolicyTerm(policyTermId);
    return ResponseEntity.ok(policyTermDto);
  }

  @PutMapping("/policy/{policyTermId}")
  @Operation(summary = "해당 정책을 수정한다.", description = "policyTermId는 1,2,3만 존재")
  @Response204And400And404
  public ResponseEntity<PolicyTermDto> updatePolicyTerm(
      @PathVariable Long policyTermId, @Valid @RequestBody SavePolicyTernDto savePolicyTernDto) {

    policyTermService.updatePolicyTerm(policyTermId, savePolicyTernDto);
    return ResponseEntity.noContent().build();
  }
}
