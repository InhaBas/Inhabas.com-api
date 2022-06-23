package com.inhabas.api.controller;

import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.MemberId;
import com.inhabas.api.dto.member.ContactDto;
import com.inhabas.api.service.member.MemberService;
import com.inhabas.api.service.member.MemberTeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "회원관리")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MemberTeamService memberTeamService;

    @Operation(description = "멤버 조회")
    @GetMapping
    public Member member(@RequestParam MemberId id) {
        return memberService.findById(id);
    }

    @Operation(description = "모든 유저 조회")
    @GetMapping("/all")
    public List<Member> members() {
        return memberService.findMembers();
    }

    @Operation(description = "유저 정보 변경")
    @PutMapping
    public Member updateMember(@RequestBody Member member) {
        return memberService.updateMember(member).get();
    }

    @Operation(summary = "회장 연락처 정보 불러오기")
    @GetMapping("/chief")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<ContactDto> getChiefContact() {
        ContactDto contact = memberService.getChiefContact();
        return ResponseEntity.ok(contact);
    }

    @Operation(summary = "회원을 팀에 포함시킨다.")
    @PostMapping("/team")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "401", description = "권한이 있어야한다.")
    })
    public ResponseEntity<?> addOneTeamToMember(@RequestParam MemberId memberId, @RequestParam Integer teamId) {

        memberTeamService.addMemberToTeam(memberId, teamId);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "회원을 팀에서 제외시킨다.")
    @DeleteMapping("/team")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "401", description = "권한이 있어야한다.")
    })
    public ResponseEntity<?> deleteOneTeamOfMember(@RequestParam MemberId memberId, @RequestParam Integer teamId) {

        memberTeamService.deleteMemberFromTeam(memberId, teamId);

        return ResponseEntity.noContent().build();
    }



}
