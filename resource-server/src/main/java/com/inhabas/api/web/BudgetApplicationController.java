package com.inhabas.api.web;

import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;
import com.inhabas.api.domain.budget.dto.*;
import com.inhabas.api.domain.budget.usecase.BudgetApplicationService;
import com.inhabas.api.domain.budget.usecase.BudgetApplicationProcessor;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Tag(name = "예산 지원 신청")
@RequiredArgsConstructor
public class BudgetApplicationController {

    private final BudgetApplicationService budgetApplicationService;
    private final BudgetApplicationProcessor applicationProcessor;

    @Operation(summary = "새로운 예산지원신청글을 추가한다.")
    @PostMapping("/budget/application")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력"),
    })
    public ResponseEntity<?> createApplication(
            @Authenticated StudentId loginMember, @Valid @RequestBody BudgetApplicationRegisterForm form) {

        budgetApplicationService.registerApplication(form, loginMember);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "예산지원신청글을 수정한다.")
    @PutMapping("/budget/application")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력, 또는 게시글이 존재하지 않는 경우"),
            @ApiResponse(responseCode = "401", description = "글쓴이가 아니면 수정 불가")
    })
    public ResponseEntity<?> modifyApplication(
            @Authenticated StudentId loginMember, @Valid @RequestBody BudgetApplicationUpdateForm form) {

        budgetApplicationService.updateApplication(form, loginMember);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "예산지원신청글을 삭제한다.")
    @DeleteMapping("/budget/application/{applicationId}")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력, 또는 게시글이 존재하지 않는 경우"),
            @ApiResponse(responseCode = "401", description = "글쓴이가 아니면 삭제 불가")
    })
    public ResponseEntity<?> deleteApplication(
            @Authenticated StudentId loginMember, @PathVariable Integer applicationId) {

        budgetApplicationService.deleteApplication(applicationId, loginMember);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "예산지원신청글 내용을 조회한다.")
    @GetMapping("/budget/application/{applicationId}")

    public ResponseEntity<BudgetApplicationDetailDto> getApplication(@PathVariable Integer applicationId) {

        BudgetApplicationDetailDto details = budgetApplicationService.getApplicationDetails(applicationId);

        return ResponseEntity.ok(details);
    }

    @Operation(summary = "예산지원 요청 게시글 목록을 조회한다.")
    @GetMapping("/budget/applications")
    @ApiResponse(responseCode = "200", description = "검색 결과를 pagination 적용해서 반환, status 값 안주면 전체 상태를 검색함.")
    public ResponseEntity<Page<BudgetApplicationListDto>> getApplications(
            @Nullable @RequestParam ApplicationStatus status,
            @PageableDefault(size = 15, sort = "dateUsed", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<BudgetApplicationListDto> page = budgetApplicationService.getApplications(status, pageable);

        return ResponseEntity.ok(page);
    }

    @Operation(summary = "총무가 예산지원요청글의 상태를 변경한다.")
    @PutMapping("/budget/application/{applicationId}/status")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 status 정보이거나, 또는 게시글이 존재하지 않는 경우"),
            @ApiResponse(responseCode = "401", description = "이미 처리완료된 상태이거나, 총무가 아니면 변경 불가.")
    })
    public ResponseEntity<?> changeApplicationStatus(
            @Authenticated StudentId loginMember, @PathVariable Integer applicationId,
            @RequestBody BudgetApplicationStatusChangeRequest request) {

        applicationProcessor.process(applicationId, request, loginMember);

        return ResponseEntity.noContent().build();
    }
}
