package com.inhabas.api.web;

import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetHistoryModifyForm;
import com.inhabas.api.domain.budget.usecase.BudgetHistoryService;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.web.argumentResolver.Authenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @GetMapping("/get")
    public ResponseEntity<Page<BudgetHistoryDetailDto>> getListBudgetHistory(
            @PageableDefault(size = 15, sort = "dateUsed", direction = Direction.DESC) Pageable pageable) {

        Page<BudgetHistoryDetailDto> historyList = budgetHistoryService.getHistoryList(pageable);

        return ResponseEntity.ok(historyList);
    }

    @GetMapping
    public ResponseEntity<BudgetHistoryDetailDto> getBudgetHistory(@RequestParam("id") Integer historyId) {

        BudgetHistoryDetailDto history = budgetHistoryService.getHistory(historyId);

        return ResponseEntity.ok(history);
    }

}
