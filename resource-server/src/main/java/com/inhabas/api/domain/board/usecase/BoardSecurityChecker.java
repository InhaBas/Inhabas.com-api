package com.inhabas.api.domain.board.usecase;

import com.inhabas.api.auth.domain.error.authException.InvalidAuthorityException;
import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.member.security.DefaultRoleHierarchy;
import com.inhabas.api.domain.board.domain.BaseBoard;
import com.inhabas.api.domain.board.repository.BaseBoardRepository;
import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.comment.repository.CommentRepository;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class BoardSecurityChecker {

    private final BaseBoardRepository baseBoardRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;
    private final CommentRepository commentRepository;
    private final DefaultRoleHierarchy roleHierarchy;


    public final static String CREATE_BOARD = "createBoard";
    public final static String READ_BOARD = "readBoard";
    public final static String CREATE_COMMENT = "createComment";
    public final static String READ_COMMENT = "readComment";
    public final static String ROLE_PREFIX = "ROLE_";


    public boolean boardWriterOnly(Long boardId) {

        Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        BaseBoard baseBoard = baseBoardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);

        return baseBoard.isWrittenBy(member);

    }

    public boolean commentWriterOnly(Long commentId) {

        Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Comment comment = commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);

        return comment.isWrittenBy(memberId);

    }


    public void checkMenuAccess(Integer menuId, String action) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = roleHierarchy.getHierarchy().getReachableGrantedAuthorities(authentication.getAuthorities());

        Menu menu = menuRepository.findById(menuId).orElseThrow(NotFoundException::new);
        Role required = null;
        switch (action) {
            case CREATE_BOARD:
                required = menu.getType().getCreateBoardRole();
                break;
            case READ_BOARD:
                required = menu.getType().getReadBoardRole();
                break;
            case CREATE_COMMENT:
                required = menu.getType().getCreateCommentRole();
                break;
            case READ_COMMENT:
                required = menu.getType().getReadCommentRole();
                break;
        }

        String finalRequired = ROLE_PREFIX + required;
        if (required != null && authorities.stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(finalRequired))) {
            throw new InvalidAuthorityException();
        }

    }

}
