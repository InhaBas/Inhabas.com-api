package com.inhabas.api.domain.board.usecase;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.ANONYMOUS;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

@Service
@RequiredArgsConstructor
public class BoardSecurityChecker {

  private final BaseBoardRepository baseBoardRepository;
  private final MemberRepository memberRepository;
  private final MenuRepository menuRepository;
  private final CommentRepository commentRepository;
  private final DefaultRoleHierarchy roleHierarchy;

  public static final String READ_BOARD_LIST = "readBoardList";
  public static final String CREATE_BOARD = "createBoard";
  public static final String READ_BOARD = "readBoard";
  public static final String CREATE_COMMENT = "createComment";
  public static final String READ_COMMENT = "readComment";
  public static final String ROLE_PREFIX = "ROLE_";

  public boolean boardWriterOnly(Long boardId) {

    Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    BaseBoard baseBoard =
        baseBoardRepository.findById(boardId).orElseThrow(EntityNotFoundException::new);

    return baseBoard.isWrittenBy(member);
  }

  public boolean commentWriterOnly(Long commentId) {

    Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    Comment comment =
        commentRepository.findById(commentId).orElseThrow(EntityNotFoundException::new);

    return comment.isWrittenBy(memberId);
  }

  public boolean checkMenuAccess(Integer menuId, String action) {

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Collection<? extends GrantedAuthority> authorities;
    if (authentication == null) {
      authorities = Collections.singleton(new SimpleGrantedAuthority(ROLE_PREFIX + ANONYMOUS));
    } else {
      authorities =
          roleHierarchy
              .getHierarchy()
              .getReachableGrantedAuthorities(authentication.getAuthorities());
    }

    Menu menu = menuRepository.findById(menuId).orElseThrow(NotFoundException::new);
    List<Role> requiredRoles = new ArrayList<>();
    switch (action) {
      case READ_BOARD_LIST:
        requiredRoles = menu.getType().getReadBoardListRole();
        break;
      case CREATE_BOARD:
        requiredRoles = menu.getType().getCreateBoardRole();
        break;
      case READ_BOARD:
        requiredRoles = menu.getType().getReadBoardRole();
        break;
      case CREATE_COMMENT:
        requiredRoles = menu.getType().getCreateCommentRole();
        break;
      case READ_COMMENT:
        requiredRoles = menu.getType().getReadCommentRole();
        break;
    }

    Set<String> prefixedRoles =
        requiredRoles.stream().map(role -> ROLE_PREFIX + role).collect(Collectors.toSet());

    boolean hasAuthority =
        authorities.stream().map(GrantedAuthority::getAuthority).anyMatch(prefixedRoles::contains);

    if (!hasAuthority) {
      throw new InvalidAuthorityException();
    }

    return true;
  }
}
