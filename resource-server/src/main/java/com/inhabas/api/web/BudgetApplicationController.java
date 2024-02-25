package com.inhabas.api.web;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationRegisterForm;
import com.inhabas.api.domain.budget.dto.BudgetApplicationStatusChangeRequest;
import com.inhabas.api.domain.budget.dto.BudgetApplicationUpdateForm;
import com.inhabas.api.domain.budget.usecase.BudgetApplicationProcessor;
import com.inhabas.api.domain.budget.usecase.BudgetApplicationService;
import com.inhabas.api.global.dto.PageInfoDto;
import com.inhabas.api.global.dto.PagedResponseDto;
import com.inhabas.api.global.util.PageUtil;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "예산 지원 신청")
@RequiredArgsConstructor
public class BudgetApplicationController {

  private final BudgetApplicationService budgetApplicationService;
  private final BudgetApplicationProcessor applicationProcessor;

  @Operation(summary = "예산지원신청 글 추가")
  @PostMapping("/budget/application")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "201", description = "'Location' 헤더에 생성된 리소스의 URI 가 포함됩니다."),
        @ApiResponse(
            responseCode = "400 ",
            description = "입력값이 없거나, 타입이 유효하지 않습니다.",
            content =
                @Content(
                    schema = @Schema(implementation = ErrorResponse.class),
                    examples =
                        @ExampleObject(
                            value =
                                "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}")))
      })
  public ResponseEntity<?> createApplication(
      @Authenticated Long memberId, @Valid @RequestBody BudgetApplicationRegisterForm form) {

    Long newApplicationId = budgetApplicationService.registerApplication(form, memberId);

    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{applicationId}")
            .buildAndExpand(newApplicationId)
            .toUri();
    return ResponseEntity.created(location).build();
  }

  @Operation(summary = "예산지원신청 글 수정")
  @PutMapping("/budget/application/{applicationId}")
  @ApiResponses(
      value = {
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
  public ResponseEntity<?> modifyApplication(
      @Authenticated Long memberId,
      @Valid @RequestBody BudgetApplicationUpdateForm form,
      @PathVariable Long applicationId) {
    budgetApplicationService.updateApplication(applicationId, form, memberId);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "예산지원신청 글 삭제")
  @DeleteMapping("/budget/application/{applicationId}")
  @ApiResponses(
      value = {
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
  public ResponseEntity<?> deleteApplication(
      @Authenticated Long memberId, @PathVariable Long applicationId) {

    budgetApplicationService.deleteApplication(applicationId, memberId);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "예산지원요청 글 목록 조회")
  @GetMapping("/budget/applications")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = PagedResponseDto.class))}),
      })
  public ResponseEntity<PagedResponseDto<BudgetApplicationDto>> getApplications(
      @Nullable @RequestParam RequestStatus status,
      @Parameter(description = "페이지", example = "0")
          @RequestParam(name = "page", defaultValue = "0")
          int page,
      @Parameter(description = "페이지당 개수", example = "10")
          @RequestParam(name = "size", defaultValue = "10")
          int size) {
    Pageable pageable = PageRequest.of(page, size);
    List<BudgetApplicationDto> allDtoList = budgetApplicationService.getApplications(status);
    List<BudgetApplicationDto> pagedDtoList = PageUtil.getPagedDtoList(pageable, allDtoList);

    PageImpl<BudgetApplicationDto> budgetApplicationDtoPage =
        new PageImpl<>(pagedDtoList, pageable, allDtoList.size());
    PageInfoDto pageInfoDto = new PageInfoDto(budgetApplicationDtoPage);

    return ResponseEntity.ok(new PagedResponseDto<>(pageInfoDto, pagedDtoList));
  }

  @Operation(summary = "예산지원신청 글 단일 조회")
  @GetMapping("/budget/application/{applicationId}")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {
              @Content(schema = @Schema(implementation = BudgetApplicationDetailDto.class))
            }),
      })
  public ResponseEntity<BudgetApplicationDetailDto> getApplication(
      @PathVariable Long applicationId) {

    BudgetApplicationDetailDto details =
        budgetApplicationService.getApplicationDetails(applicationId);

    return ResponseEntity.ok(details);
  }

  @Operation(summary = "예산지원요청 글 상태 변경")
  @PutMapping("/budget/application/{applicationId}/status")
  @ApiResponses({
    @ApiResponse(responseCode = "204"),
    @ApiResponse(responseCode = "400", description = "잘못된 status 정보이거나, 또는 게시글이 존재하지 않는 경우"),
    @ApiResponse(responseCode = "401", description = "이미 처리완료된 상태이거나, 총무가 아니면 변경 불가.")
  })
  public ResponseEntity<?> changeApplicationStatus(
      @Authenticated Long memberId,
      @PathVariable Long applicationId,
      @RequestBody BudgetApplicationStatusChangeRequest request) {

    applicationProcessor.process(applicationId, request, memberId);

    return ResponseEntity.noContent().build();
  }
}
