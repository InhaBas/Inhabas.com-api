package com.inhabas.api.web;

import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetHistoryListResponse;
import com.inhabas.api.domain.budget.dto.BudgetHistoryModifyForm;
import com.inhabas.api.domain.budget.usecase.BudgetHistoryService;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "회계내역")
@RequiredArgsConstructor
public class BudgetHistoryController {

    private final BudgetHistoryService budgetHistoryService;

    @Operation(summary = "새로운 회계 내역을 추가한다.")
    @PostMapping("/budget/history")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력"),
            @ApiResponse(responseCode = "401", description = "총무가 아니면 접근 불가")
    })
    public ResponseEntity<?> createNewHistory(
            @Authenticated StudentId studentId, @Valid @RequestBody BudgetHistoryCreateForm form) {

        budgetHistoryService.createNewHistory(form, studentId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회계 내역을 수정한다.")
    @PutMapping("/budget/history")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력, 또는 id가 존재하지 않는 경우"),
            @ApiResponse(responseCode = "401", description = "총무가 아니면 접근 불가, 다른 총무가 작성한 것 수정 불가")
    })
    public ResponseEntity<?> modifyHistory(
            @Authenticated StudentId studentId, @Valid @RequestBody BudgetHistoryModifyForm form) {

        budgetHistoryService.modifyHistory(form, studentId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회계 내역을 삭제한다.")
    @DeleteMapping("/budget/history/{historyId}")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력, 또는 id가 존재하지 않는 경우"),
            @ApiResponse(responseCode = "401", description = "총무가 아니면 접근 불가, 다른 총무가 작성한 것 삭제 불가")
    })
    public ResponseEntity<?> deleteHistory(
            @Authenticated StudentId studentId,
            @PathVariable Integer historyId) {

        budgetHistoryService.deleteHistory(historyId, studentId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "단일 회계 내역을 조회한다.")
    @GetMapping("/budget/history/{historyId}")
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "잘못된 입력, 또는 id가 존재하지 않는 경우"),
    })
    public ResponseEntity<BudgetHistoryDetailDto> getBudgetHistory(@PathVariable Integer historyId) {

        BudgetHistoryDetailDto history = budgetHistoryService.getHistory(historyId);

        return ResponseEntity.ok(history);
    }

    @Operation(summary = "회계 내역을 검색한다.")
    @GetMapping("/budget/histories")
    @ApiResponse(responseCode = "200", description = "검색 결과를 pagination 적용해서 현재 잔고와 함께 반환, year 값 안주면 전체기간으로 적용됨.")
    public ResponseEntity<BudgetHistoryListResponse> searchBudgetHistory(
            @Nullable @RequestParam Integer year,
            @PageableDefault(size = 15, sort = "dateUsed", direction = Direction.DESC) Pageable pageable) {
        BudgetHistoryListResponse response = budgetHistoryService.searchHistoryList(
                year, pageable);

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "회계 내역이 작성된 기간동안의 연도 목록을 가져온다.")
    @GetMapping("/budget/histories/years")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<Integer>> getAllYearsOfHistory() {

        return ResponseEntity.ok(budgetHistoryService.getAllYearOfHistory());
    }
}
