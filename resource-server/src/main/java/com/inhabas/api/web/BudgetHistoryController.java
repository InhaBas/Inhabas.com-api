package com.inhabas.api.web;

import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryModifyForm;
import com.inhabas.api.domain.budget.usecase.BudgetHistoryService;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.web.argumentResolver.Authenticated;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/budget/history")
@RequiredArgsConstructor
public class BudgetHistoryController {

    private final BudgetHistoryService budgetHistoryService;

    @PostMapping("/create")
    public ResponseEntity<?> createNewHistory(
            @Authenticated MemberId memberId, @Valid @RequestBody BudgetHistoryCreateForm form) {

        budgetHistoryService.createNewHistory(form, memberId);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update")
    public ResponseEntity<?> modifyHistory(
            @Authenticated MemberId memberId, @Valid @RequestBody BudgetHistoryModifyForm form) {

        budgetHistoryService.modifyHistory(form, memberId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteHistory(
            @Authenticated MemberId memberId,
            @RequestParam(name = "id") Integer historyId) {

        budgetHistoryService.deleteHistory(historyId, memberId);

        return ResponseEntity.noContent().build();
    }
}
