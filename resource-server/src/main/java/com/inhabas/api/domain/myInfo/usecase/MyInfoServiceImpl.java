package com.inhabas.api.domain.myInfo.usecase;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.authException.InvalidAuthorityException;
import com.inhabas.api.domain.myInfo.dto.MyBoardDto;
import com.inhabas.api.domain.myInfo.dto.MyBudgetSupportApplicationDto;
import com.inhabas.api.domain.myInfo.dto.MyCommentDto;
import com.inhabas.api.domain.myInfo.repository.MyInfoRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyInfoServiceImpl implements MyInfoService {

  private final MyInfoRepository myInfoRepository;

  // 현재 로그인한 유저의 모든 게시글을 불러온다.
  @Override
  @Transactional(readOnly = true)
  public List<MyBoardDto> getMyBoards(Long memberId) {

    List<MyBoardDto> myBoardDtoList = new ArrayList<>();

    myBoardDtoList.addAll(myInfoRepository.findAllBoardsByMemberId(memberId));
    if (SecurityContextHolder.getContext() == null) {
      throw new InvalidAuthorityException();
    }

    return myBoardDtoList;
  }

  // 현재 로그인한 유저의 모든 댓글을 불러온다.
  @Override
  @Transactional(readOnly = true)
  public List<MyCommentDto> getMyComments(Long memberId) {

    List<MyCommentDto> myCommentDtoList = new ArrayList<>();

    myCommentDtoList.addAll(myInfoRepository.findAllCommentsByMemberId(memberId));
    if (SecurityContextHolder.getContext() == null) {
      throw new InvalidAuthorityException();
    }

    return myCommentDtoList;
  }

  // 현재 로그인한 유저의 모든 예산 신청 내역을 불러온다.
  @Override
  @Transactional(readOnly = true)
  public List<MyBudgetSupportApplicationDto> getMyBudgetSupportApplications(Long memberId) {

    List<MyBudgetSupportApplicationDto> budgetSupportApplicationDtoList = new ArrayList<>();

    budgetSupportApplicationDtoList.addAll(
        myInfoRepository.findAllBudgetSupportApplicationsByMemberId(memberId));
    if (SecurityContextHolder.getContext() == null) {
      throw new InvalidAuthorityException();
    }

    return budgetSupportApplicationDtoList;
  }
}
