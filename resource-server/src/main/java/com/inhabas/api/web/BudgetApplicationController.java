package com.inhabas.api.web;

import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationListDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationRegisterForm;
import com.inhabas.api.domain.budget.dto.BudgetApplicationUpdateForm;
import com.inhabas.api.domain.budget.usecase.BudgetApplicationService;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
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
@RequestMapping("/budget/application")
@RequiredArgsConstructor
public class BudgetApplicationController {

    private final BudgetApplicationService budgetApplicationService;

    @Operation(summary = "새로운 예산지원신청글을 추가한다.")
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력"),
    })
    public ResponseEntity<?> createApplication(
            @Authenticated MemberId loginMember, @Valid @RequestBody BudgetApplicationRegisterForm form) {

        budgetApplicationService.registerApplication(form, loginMember);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "예산지원신청글을 수정한다.")
    @PutMapping
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력, 또는 게시글이 존재하지 않는 경우"),
            @ApiResponse(responseCode = "401", description = "글쓴이가 아니면 수정 불가")
    })
    public ResponseEntity<?> modifyApplication(
            @Authenticated MemberId loginMember, @Valid @RequestBody BudgetApplicationUpdateForm form) {

        budgetApplicationService.updateApplication(form, loginMember);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "예산지원신청글을 삭제한다.")
    @DeleteMapping("/{applicationId}")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력, 또는 게시글이 존재하지 않는 경우"),
            @ApiResponse(responseCode = "401", description = "글쓴이가 아니면 삭제 불가")
    })
    public ResponseEntity<?> deleteApplication(
            @Authenticated MemberId loginMember, @PathVariable Integer applicationId) {

        budgetApplicationService.deleteApplication(applicationId, loginMember);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "예산지원신청글 내용을 조회한다.")
    @GetMapping("/{applicationId}")

    public ResponseEntity<BudgetApplicationDetailDto> getApplication(@PathVariable Integer applicationId) {

        BudgetApplicationDetailDto details = budgetApplicationService.getApplicationDetails(applicationId);

        return ResponseEntity.ok(details);
    }

    @Operation(summary = "예산지원 요청 게시글 목록을 조회한다.")
    @GetMapping("/search")
    @ApiResponse(responseCode = "200", description = "검색 결과를 pagination 적용해서 반환, status 값 안주면 전체 상태를 검색함.")
    public ResponseEntity<Page<BudgetApplicationListDto>> getApplications(
            @Nullable @RequestParam ApplicationStatus status,
            @PageableDefault(size = 15, sort = "dateUsed", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<BudgetApplicationListDto> page = budgetApplicationService.getApplications(status, pageable);

        return ResponseEntity.ok(page);
    }
}
