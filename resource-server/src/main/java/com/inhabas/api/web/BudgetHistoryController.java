package com.inhabas.api.web;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.inhabas.api.auth.domain.error.ErrorResponse;
import com.inhabas.api.domain.budget.dto.BudgetHistoryCreateForm;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDetailDto;
import com.inhabas.api.domain.budget.dto.BudgetHistoryDto;
import com.inhabas.api.domain.budget.dto.BudgetHistoryListResponse;
import com.inhabas.api.domain.budget.usecase.BudgetHistoryService;
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

@Slf4j
@RestController
@Tag(name = "회계 내역")
@RequiredArgsConstructor
public class BudgetHistoryController {

  private final BudgetHistoryService budgetHistoryService;

  @Operation(summary = "회계 내역이 작성된 기간동안의 연도 목록 조회")
  @GetMapping("/budget/histories/years")
  @ApiResponse(responseCode = "200")
  public ResponseEntity<List<Integer>> getAllYearsOfHistory() {

    return ResponseEntity.ok(budgetHistoryService.getAllYearOfHistory());
  }

  @Operation(summary = "회계 내역 목록을 조회")
  @GetMapping("/budget/histories")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            description = "검색 결과를 pagination 적용해서 현재 잔고와 함께 반환, year 값 안주면 전체기간으로 적용됨.",
            content = {
              @Content(schema = @Schema(implementation = BudgetHistoryListResponse.class))
            }),
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
    List<BudgetHistoryDto> allDtoList = budgetHistoryService.searchHistoryList(year);
    List<BudgetHistoryDto> pagedDtoList = PageUtil.getPagedDtoList(pageable, allDtoList);

    PageImpl<BudgetHistoryDto> newBudgetHistoryDetailDtoPage =
        new PageImpl<>(pagedDtoList, pageable, allDtoList.size());
    PageInfoDto pageInfoDto = new PageInfoDto(newBudgetHistoryDetailDtoPage);
    PagedResponseDto<BudgetHistoryDto> budgetHistoryDetailDtoPagedResponseDto =
        new PagedResponseDto<>(pageInfoDto, pagedDtoList);

    Integer balance = budgetHistoryService.getBalance();

    return ResponseEntity.ok(
        new BudgetHistoryListResponse(budgetHistoryDetailDtoPagedResponseDto, balance));
  }

  @Operation(summary = "회계 내역 단일 조회")
  @GetMapping("/budget/history/{historyId}")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "200",
            content = {@Content(schema = @Schema(implementation = BudgetHistoryDetailDto.class))}),
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

  @Operation(summary = "회계 내역 추가 (Swagger 사용 불가. 명세서 참고)")
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
      @Authenticated Long memberId,
      @Valid @RequestPart(value = "form") BudgetHistoryCreateForm form,
      @RequestPart(value = "files") List<MultipartFile> files) {

    Long newHistory = budgetHistoryService.createHistory(form, files, memberId);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{historyId}")
            .buildAndExpand(newHistory)
            .toUri();

    return ResponseEntity.created(location).build();
  }

  @Operation(summary = "회계 내역 수정 (Swagger 사용 불가. 명세서 참고)")
  @PostMapping("/budget/history/{historyId}")
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
  @PreAuthorize("hasRole('SECRETARY')")
  public ResponseEntity<?> modifyHistory(
      @Authenticated Long memberId,
      @PathVariable Long historyId,
      @Valid @RequestPart(value = "form") BudgetHistoryCreateForm form,
      @RequestPart(value = "files") List<MultipartFile> files) {

    budgetHistoryService.modifyHistory(historyId, form, files, memberId);

    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "회계 내역 삭제")
  @DeleteMapping("/budget/history/{historyId}")
  @ApiResponses({
    @ApiResponse(responseCode = "204"),
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
  @PreAuthorize("@boardSecurityChecker.boardWriterOnly(#historyId) and hasRole('SECRETARY')")
  public ResponseEntity<?> deleteHistory(
      @Authenticated Long memberId, @PathVariable Long historyId) {

    budgetHistoryService.deleteHistory(historyId, memberId);

    return ResponseEntity.noContent().build();
  }
}
