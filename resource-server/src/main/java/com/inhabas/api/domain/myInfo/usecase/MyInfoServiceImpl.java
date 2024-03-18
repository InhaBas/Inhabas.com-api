package com.inhabas.api.domain.myInfo.usecase;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.authException.InvalidAuthorityException;
import com.inhabas.api.domain.myInfo.dto.MyCommentsDto;
import com.inhabas.api.domain.myInfo.dto.MyPostsDto;
import com.inhabas.api.domain.myInfo.repository.MyInfoRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyInfoServiceImpl implements MyInfoService {

  private final MyInfoRepository myInfoRepository;

  // 현재 로그인한 유저의 모든 게시글을 불러온다.
  @Override
  @Transactional(readOnly = true)
  public List<MyPostsDto> getMyBoards() {

    List<MyPostsDto> posts = new ArrayList<>();

    if (SecurityContextHolder.getContext() == null) {
      throw new InvalidAuthorityException();
    }
    Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    posts.addAll(myInfoRepository.findAllBoardsByMemberId(memberId));
    if (SecurityContextHolder.getContext() == null) {
      throw new InvalidAuthorityException();
    }

    return posts;
  }

  @Override
  @Transactional(readOnly = true)
  public List<MyCommentsDto> getMyComments() {

    List<MyCommentsDto> comments = new ArrayList<>();

    if (SecurityContextHolder.getContext() == null) {
      throw new InvalidAuthorityException();
    }
    Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    comments.addAll(myInfoRepository.findAllCommentsByMemberId(memberId));
    if (SecurityContextHolder.getContext() == null) {
      throw new InvalidAuthorityException();
    }

    return comments;
  }
}
