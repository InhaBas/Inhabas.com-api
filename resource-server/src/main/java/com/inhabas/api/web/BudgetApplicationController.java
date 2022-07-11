package com.inhabas.api.web;

import com.inhabas.api.domain.budget.domain.valueObject.ApplicationStatus;
import com.inhabas.api.domain.budget.dto.BudgetApplicationDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationListDto;
import com.inhabas.api.domain.budget.dto.BudgetApplicationRegisterForm;
import com.inhabas.api.domain.budget.dto.BudgetApplicationUpdateForm;
import com.inhabas.api.domain.budget.usecase.BudgetApplicationService;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.web.argumentResolver.Authenticated;
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

    @PostMapping
    public ResponseEntity<?> createApplication(
            @Authenticated MemberId loginMember, @Valid @RequestBody BudgetApplicationRegisterForm form) {

        budgetApplicationService.registerApplication(form, loginMember);

        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<?> modifyApplication(
            @Authenticated MemberId loginMember, @Valid @RequestBody BudgetApplicationUpdateForm form) {

        budgetApplicationService.updateApplication(form, loginMember);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<?> deleteApplication(
            @Authenticated MemberId loginMember, @PathVariable Integer applicationId) {

        budgetApplicationService.deleteApplication(applicationId, loginMember);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{applicationId}")
    public ResponseEntity<BudgetApplicationDetailDto> getApplication(@PathVariable Integer applicationId) {

        BudgetApplicationDetailDto details = budgetApplicationService.getApplicationDetails(applicationId);

        return ResponseEntity.ok(details);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BudgetApplicationListDto>> getApplications(
            @Nullable @RequestParam ApplicationStatus status,
            @PageableDefault(size = 15, sort = "dateUsed", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<BudgetApplicationListDto> page = budgetApplicationService.getApplications(status, pageable);

        return ResponseEntity.ok(page);
    }
}
