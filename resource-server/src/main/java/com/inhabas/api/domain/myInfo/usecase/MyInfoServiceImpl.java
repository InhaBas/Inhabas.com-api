package com.inhabas.api.domain.myInfo.usecase;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.authException.InvalidAuthorityException;
import com.inhabas.api.domain.myInfo.dto.MyBoardsDto;
import com.inhabas.api.domain.myInfo.dto.MyBudgetSupportApplicationDto;
import com.inhabas.api.domain.myInfo.dto.MyCommentsDto;
import com.inhabas.api.domain.myInfo.repository.MyInfoRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyInfoServiceImpl implements MyInfoService {

  private final MyInfoRepository myInfoRepository;

  // 현재 로그인한 유저의 모든 게시글을 불러온다.
  @Override
  @Transactional(readOnly = true)
  public List<MyBoardsDto> getMyBoards() {

    List<MyBoardsDto> myBoardsDtoList = new ArrayList<>();

    if (SecurityContextHolder.getContext() == null) {
      throw new InvalidAuthorityException();
    }
    Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    myBoardsDtoList.addAll(myInfoRepository.findAllBoardsByMemberId(memberId));
    if (SecurityContextHolder.getContext() == null) {
      throw new InvalidAuthorityException();
    }

    return myBoardsDtoList;
  }

  // 현재 로그인한 유저의 모든 댓글을 불러온다.
  @Override
  @Transactional(readOnly = true)
  public List<MyCommentsDto> getMyComments() {

    List<MyCommentsDto> MyCommentsDtoList = new ArrayList<>();

    if (SecurityContextHolder.getContext() == null) {
      throw new InvalidAuthorityException();
    }
    Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    MyCommentsDtoList.addAll(myInfoRepository.findAllCommentsByMemberId(memberId));
    if (SecurityContextHolder.getContext() == null) {
      throw new InvalidAuthorityException();
    }

    return MyCommentsDtoList;
  }

  // 현재 로그인한 유저의 모든 예산 신청 내역을 불러온다.
  @Override
  @Transactional(readOnly = true)
  public List<MyBudgetSupportApplicationDto> getMyBudgetSupportApplications() {

    List<MyBudgetSupportApplicationDto> budgetSupportApplicationDtoList = new ArrayList<>();

    if (SecurityContextHolder.getContext() == null) {
      throw new InvalidAuthorityException();
    }
    Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    budgetSupportApplicationDtoList.addAll(
        myInfoRepository.findAllBudgetSupportApplicationsByMemberId(memberId));
    if (SecurityContextHolder.getContext() == null) {
      throw new InvalidAuthorityException();
    }

    return budgetSupportApplicationDtoList;
  }
}
