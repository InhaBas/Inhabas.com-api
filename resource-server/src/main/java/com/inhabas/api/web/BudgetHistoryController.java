package com.inhabas.api.web;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetHistoryListResponse;
import com.inhabas.api.domain.budget.usecase.BudgetHistoryService;
import com.inhabas.api.web.argumentResolver.Authenticated;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Slf4j
@RestController
@Tag(name = "회계 내역")
@RequiredArgsConstructor
public class BudgetHistoryController {

  private final BudgetHistoryService budgetHistoryService;

  @Operation(summary = "회계 내역 추가")
  @PostMapping("/budget/history")
  @ApiResponses({
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
                      "{\"status\": 400, \"code\": \"G003\", \"message\": \"입력값이 없거나, 타입이 유효하지 않습니다.\"}"))),
  })
  @PreAuthorize("hasRole('SECRETARY')")
  public ResponseEntity<?> createNewHistory(
      @Authenticated Long memberId, @Valid @RequestBody BudgetHistoryCreateForm form) {

    budgetHistoryService.createNewHistory(form, memberId);

    Long newNormalBoardId = budgetHistoryService.createNewHistory(form, memberId);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .replaceQueryParam("pinOption")
            .path("/{boardId}")
            .buildAndExpand(newNormalBoardId)
            .toUri();

    return ResponseEntity.created(location).build();
  }

  @Operation(summary = "회계 내역을 수정한다.")
  @PutMapping("/budget/history/{historyId}")
  @ApiResponses({
      @ApiResponse(responseCode = "200"),
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
  public ResponseEntity<?> modifyHistory(
      @Authenticated Long memberId,
      @PathVariable Long historyId,
      @Valid @RequestBody BudgetHistoryCreateForm form) {

    budgetHistoryService.modifyHistory(historyId, form, memberId);

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
      @Authenticated Long memberId,
      @PathVariable Long historyId) {

    budgetHistoryService.deleteHistory(historyId, memberId);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "단일 회계 내역 조회")
  @GetMapping("/budget/history/{historyId}")
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              content = {
                  @Content(schema = @Schema(implementation = BudgetHistoryDetailDto.class))}),
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
  @PreAuthorize(
      "@boardSecurityChecker.checkMenuAccess(15, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).READ_BOARD)")
  public ResponseEntity<BudgetHistoryDetailDto> getBudgetHistory(@PathVariable Long historyId) {

    BudgetHistoryDetailDto history = budgetHistoryService.getHistory(historyId);

    return ResponseEntity.ok(history);
  }

  @Operation(summary = "회계 내역 목록을 조회")
  @GetMapping("/budget/histories")
  @ApiResponses(
      value = {
          @ApiResponse(
              responseCode = "200",
              description = "검색 결과를 pagination 적용해서 현재 잔고와 함께 반환, year 값 안주면 전체기간으로 적용됨.",
              content = {
                  @Content(schema = @Schema(implementation = BudgetHistoryListResponse.class))}),
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
      })
  @PreAuthorize(
      "@boardSecurityChecker.checkMenuAccess(15, T(com.inhabas.api.domain.board.usecase.BoardSecurityChecker).READ_BOARD_LIST)")
  public ResponseEntity<BudgetHistoryListResponse> searchBudgetHistory(
      @Nullable @RequestParam Integer year,
      @Parameter(description = "페이지", example = "0")
      @RequestParam(name = "page", defaultValue = "0")
      int page,
      @Parameter(description = "페이지당 개수", example = "10")
      @RequestParam(name = "size", defaultValue = "10")
      int size) {
    Pageable pageable = PageRequest.of(page, size);
    BudgetHistoryListResponse response = budgetHistoryService.searchHistoryList(year, pageable);

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "회계 내역이 작성된 기간동안의 연도 목록 조회")
  @GetMapping("/budget/histories/years")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<List<Integer>> getAllYearsOfHistory() {

    return ResponseEntity.ok(budgetHistoryService.getAllYearOfHistory());
  }
}
