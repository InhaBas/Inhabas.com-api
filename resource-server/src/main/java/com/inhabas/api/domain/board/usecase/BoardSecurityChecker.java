package com.inhabas.api.domain.board.usecase;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.board.domain.BaseBoard;
import com.inhabas.api.domain.board.repository.BaseBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class BoardSecurityChecker {

    private final BaseBoardRepository baseBoardRepository;

    private final MemberRepository memberRepository;

    public boolean writerOnly(Long boardId) {

        Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        BaseBoard baseBoard = baseBoardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);

        return baseBoard.isWriter(member);

    }

}
