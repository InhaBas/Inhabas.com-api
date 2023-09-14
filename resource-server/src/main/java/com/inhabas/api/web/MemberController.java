package com.inhabas.api.web;

import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.dto.ContactDto;
import com.inhabas.api.domain.member.domain.MemberService;
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
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(description = "유저 정보 변경")
    @PutMapping("/member")
    public Member updateMember(@RequestBody Member member) {
        return memberService.updateMember(member).get();
    }

    @Operation(summary = "회장 연락처 정보 불러오기")
    @GetMapping("/member/chief")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<ContactDto> getChiefContact() {
        ContactDto contact = memberService.getChiefContact();
        return ResponseEntity.ok(contact);
    }

}
