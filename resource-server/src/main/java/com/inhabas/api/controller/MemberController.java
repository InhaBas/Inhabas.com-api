package com.inhabas.api.controller;

import com.inhabas.api.domain.member.Member;
import com.inhabas.api.service.member.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "Member")
@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @Operation(description = "멤버 조회")
    @GetMapping
    public Member member(@RequestParam Integer id) {
        return memberService.findById(id);
    }

    @Operation(description = "모든 유저 조회")
    @GetMapping("/all")
    public List<Member> members() {
        return memberService.findMembers();
    }

    @Operation(description = "유저 정보 변경")
    @PutMapping()
    public Member updateMember(@RequestBody Member member) {
        return memberService.updateMember(member).get();
    }

}
