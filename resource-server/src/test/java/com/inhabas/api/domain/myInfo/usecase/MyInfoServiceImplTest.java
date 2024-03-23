package com.inhabas.api.domain.myInfo.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.entity.MemberTest;
import com.inhabas.api.domain.myInfo.dto.MyBoardDto;
import com.inhabas.api.domain.myInfo.dto.MyBudgetSupportApplicationDto;
import com.inhabas.api.domain.myInfo.dto.MyCommentDto;
import com.inhabas.api.domain.myInfo.repository.MyInfoRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class MyInfoServiceImplTest {

  @Mock private MyInfoRepository myInfoRepository;

  @InjectMocks private MyInfoServiceImpl myInfoService;

  @DisplayName("내가 쓴 게시글 목록을 조회한다.")
  @Transactional(readOnly = true)
  @Test
  void getMyBoards() {
    Member writer = MemberTest.basicMember1();
    given(myInfoRepository.findAllBoardsByMemberId(writer.getId()))
        .willReturn(Collections.singletonList(new MyBoardDto()));

    List<MyBoardDto> result = myInfoService.getMyBoards(writer.getId());

    assertThat(result).isNotEmpty();
    verify(myInfoRepository).findAllBoardsByMemberId(writer.getId());
  }

  @DisplayName("내가 쓴 댓글 목록을 조회한다.")
  @Transactional(readOnly = true)
  @Test
  void getMyComments() {
    Member writer = MemberTest.basicMember1();
    given(myInfoRepository.findAllCommentsByMemberId(writer.getId()))
        .willReturn(Collections.singletonList(new MyCommentDto()));

    List<MyCommentDto> result = myInfoService.getMyComments(writer.getId());

    assertThat(result).isNotEmpty();
    verify(myInfoRepository).findAllCommentsByMemberId(writer.getId());
  }

  @DisplayName("내가 쓴 예산지원 신청 목록을 조회한다.")
  @Transactional(readOnly = true)
  @Test
  void getMyBudgetSupportApplications() {
    Member writer = MemberTest.basicMember1();
    given(myInfoRepository.findAllBudgetSupportApplicationsByMemberId(writer.getId()))
        .willReturn(Collections.singletonList(new MyBudgetSupportApplicationDto()));

    List<MyBudgetSupportApplicationDto> result =
        myInfoService.getMyBudgetSupportApplications(writer.getId());

    assertThat(result).isNotEmpty();
    verify(myInfoRepository).findAllBudgetSupportApplicationsByMemberId(writer.getId());
  }
}
