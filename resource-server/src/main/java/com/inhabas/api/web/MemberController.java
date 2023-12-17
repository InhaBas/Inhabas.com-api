package com.inhabas.api.web;

import com.inhabas.api.auth.domain.oauth2.member.domain.service.MemberService;
import com.inhabas.api.auth.domain.oauth2.member.dto.*;
import com.inhabas.api.domain.member.dto.AnswerDto;
import com.inhabas.api.domain.member.usecase.AnswerService;
import com.inhabas.api.global.dto.PageInfoDto;
import com.inhabas.api.global.dto.PagedMemberResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "회원관리", description = "회원 정보 조회, 수정 / 총무, 회장단 이상")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AnswerService answerService;


    @Operation(summary = "(신입)미승인 멤버 정보 목록 조회",
            description = "신입 멤버 정보 목록 조회 (미승인 → 비활동 처리하기위해)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = { @Content(
            schema = @Schema(implementation = PagedMemberResponseDto.class)) }),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다.")
    })
    @GetMapping("/members/unapproved")
    public ResponseEntity<PagedMemberResponseDto> getUnapprovedMembers(
            @Parameter(description = "페이지", example = "0") @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "페이지당 개수", example = "10") @RequestParam(name = "size", defaultValue = "10") int size,
            @Parameter(description = "검색어 (학번 or 이름)", example = "홍길동") @RequestParam(name = "search", defaultValue = "") String search
    ) {

        Pageable pageable = PageRequest.of(page, size);
        List<NotApprovedMemberManagementDto> allDtos = memberService.getNotApprovedMembersBySearchAndRole(search);
        List<NotApprovedMemberManagementDto> pagedDtos = (List<NotApprovedMemberManagementDto>) memberService.getPagedDtoList(pageable, allDtos);

        PageImpl<NotApprovedMemberManagementDto> newMemberManagementDtoPage =
                new PageImpl<>(pagedDtos, pageable, allDtos.size());
        PageInfoDto pageInfoDto = new PageInfoDto(newMemberManagementDtoPage);

        return ResponseEntity.ok(new PagedMemberResponseDto(pageInfoDto, pagedDtos));

    }


    @Operation(summary = "(신입)미승인 멤버 -> 비활동 멤버로 변경 / 가입 거절 처리",
            description = "(신입)미승인 멤버 비활동 멤버로 변경 / 가입 거절 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "입력값이 없거나, 타입이 유효하지 않습니다."),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다.")
    })
    @PostMapping("/members/unapproved")
    public ResponseEntity<Void> updateUnapprovedMembers(@RequestBody UpdateRequestDto updateRequestDto) {

        memberService.updateUnapprovedMembers(updateRequestDto.getMemberIdList(), updateRequestDto.getState());
        return ResponseEntity.noContent().build();

    }


    @Operation(summary = "특정 신입 멤버 지원서 조회 (아직 미구현)",
            description = "특정 신입 멤버 지원서 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다."),
            @ApiResponse(responseCode = "404", description = "데이터가 존재하지 않습니다.")
    })
    @GetMapping("/members/{memberId}/application")
    public ResponseEntity<List<AnswerDto>> getUnapprovedMemberApplication(@PathVariable Long memberId) {

        List<AnswerDto> answers = answerService.getAnswers(memberId);
        return ResponseEntity.ok(answers);

    }


    @Operation(summary = "비활동 이상 모든 멤버 목록 조회",
            description = "이름, 학번 검색 가능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다.")
    })
    @GetMapping("/members")
    public ResponseEntity<PagedMemberResponseDto> getApprovedMembers(
            @Parameter(description = "페이지", example = "0") @RequestParam(name = "page", defaultValue = "0") int page,
            @Parameter(description = "페이지당 개수", example = "10") @RequestParam(name = "size", defaultValue = "10") int size,
            @Parameter(description = "검색어 (학번 or 이름)", example = "홍길동") @RequestParam(name = "search", defaultValue = "") String search
    ) {

        Pageable pageable = PageRequest.of(page, size);
        List<ApprovedMemberManagementDto> allDtos = memberService.getApprovedMembersBySearchAndRole(search);
        List<ApprovedMemberManagementDto> pagedDtos = (List<ApprovedMemberManagementDto>) memberService.getPagedDtoList(pageable, allDtos);

        PageImpl<ApprovedMemberManagementDto> oldMemberManagementDtoPage =
                new PageImpl<>(pagedDtos, pageable, allDtos.size());
        PageInfoDto pageInfoDto = new PageInfoDto(oldMemberManagementDtoPage);

        return ResponseEntity.ok(new PagedMemberResponseDto(pageInfoDto, pagedDtos));

    }


    @Operation(summary = "비활동 이상 멤버 권한 수정",
            description = "변경 가능 권한 [ADMIN, CHIEF, VICE_CHIEF, EXECUTIVES, SECRETARY, BASIC, DEACTIVATED]")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "입력값이 없거나, 타입이 유효하지 않습니다."),
            @ApiResponse(responseCode = "403", description = "권한이 없습니다.")
    })
    @PostMapping("/members/approved")
    public ResponseEntity<Void> updateApprovedMembers(@RequestBody UpdateRoleRequestDto updateRoleRequestDto) {

        memberService.updateApprovedMembers(updateRoleRequestDto.getMemberIdList(), updateRoleRequestDto.getRole());
        return ResponseEntity.noContent().build();

    }


    @Operation(summary = "회장 연락처 조회 (권한 필요 X)",
            description = "CHIEF 의 이름, 전화번호, 이메일")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
    })
    @GetMapping("/member/chief")
    public ResponseEntity<ContactDto> getChiefContact() {

        return ResponseEntity.ok(memberService.getChiefContact());

    }

}
