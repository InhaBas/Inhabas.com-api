package com.inhabas.api.web;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.domain.policy.dto.PolicyTermDto;
import com.inhabas.api.domain.policy.dto.SavePolicyTernDto;
import com.inhabas.api.domain.policy.usecase.PolicyTermService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "정책 관리")
@RestController
@RequiredArgsConstructor
public class PolicyTermController {

  private final PolicyTermService policyTermService;

  @GetMapping("/policy/{policyTermId}")
  @SecurityRequirements(value = {})
  @Operation(summary = "해당 정책을 조회한다.", description = "policyTermId는 1,2,3만 존재")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        content = @Content(schema = @Schema(implementation = PolicyTermDto.class))),
    @ApiResponse(
        responseCode = "400 ",
        description = "입력값이 없거나, 타입이 유효하지 않습니다.",
        content =
            @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"))),
    @ApiResponse(
        responseCode = "404",
        description = "데이터가 존재하지 않습니다.",
        content =
            @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            "{\"status\": 404, \"code\": \"G004\", \"message\": \"데이터가 존재하지 않습니다.\"}")))
  })
  public ResponseEntity<PolicyTermDto> findPolicyTerm(@PathVariable Long policyTermId) {

    PolicyTermDto policyTermDto = policyTermService.findPolicyTerm(policyTermId);
    return ResponseEntity.ok(policyTermDto);
  }

  @PutMapping("/policy/{policyTermId}")
  @Operation(summary = "해당 정책을 수정한다.", description = "policyTermId는 1,2,3만 존재")
  @ApiResponses({
    @ApiResponse(responseCode = "204"),
    @ApiResponse(
        responseCode = "400 ",
        description = "입력값이 없거나, 타입이 유효하지 않습니다.",
        content =
            @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"))),
    @ApiResponse(
        responseCode = "404",
        description = "데이터가 존재하지 않습니다.",
        content =
            @Content(
                schema = @Schema(implementation = ErrorResponse.class),
                examples =
                    @ExampleObject(
                        value =
                            "{\"status\": 404, \"code\": \"G004\", \"message\": \"데이터가 존재하지 않습니다.\"}")))
  })
  public ResponseEntity<PolicyTermDto> updatePolicyTerm(
      @PathVariable Long policyTermId, @Valid @RequestBody SavePolicyTernDto savePolicyTernDto) {

    policyTermService.updatePolicyTerm(policyTermId, savePolicyTernDto);
    return ResponseEntity.noContent().build();
  }
}
