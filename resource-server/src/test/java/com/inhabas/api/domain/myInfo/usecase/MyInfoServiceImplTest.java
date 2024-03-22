package com.inhabas.api.domain.myInfo.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.inhabas.api.domain.myInfo.dto.MyBoardsDto;
import com.inhabas.api.domain.myInfo.dto.MyBudgetSupportApplicationDto;
import com.inhabas.api.domain.myInfo.dto.MyCommentsDto;
import com.inhabas.api.domain.myInfo.repository.MyInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class MyInfoServiceImplTest {

  @Mock
  private MyInfoRepository myInfoRepository;

  @InjectMocks
  private MyInfoServiceImpl myInfoService;

  @BeforeEach
  void setUp() {
    SecurityContext securityContext = mock(SecurityContext.class);
    Authentication authentication = mock(Authentication.class);

    given(securityContext.getAuthentication()).willReturn(authentication);
    given(authentication.getPrincipal()).willReturn(1L);

    SecurityContextHolder.setContext(securityContext);
  }

  @Test
  @DisplayName("내가 쓴 게시글 목록을 조회한다.")
  void getMyBoards() {
    given(myInfoRepository.findAllBoardsByMemberId(1L)).willReturn(Collections.singletonList(new MyBoardsDto()));

    List<MyBoardsDto> result = myInfoService.getMyBoards();

    assertThat(result).isNotEmpty();
    verify(myInfoRepository).findAllBoardsByMemberId(1L);
  }

  @Test
  @DisplayName("내가 쓴 댓글 목록을 조회한다.")
  void getMyComments() {
    given(myInfoRepository.findAllCommentsByMemberId(1L)).willReturn(Collections.singletonList(new MyCommentsDto()));

    List<MyCommentsDto> result = myInfoService.getMyComments();

    assertThat(result).isNotEmpty();
    verify(myInfoRepository).findAllCommentsByMemberId(1L);
  }

  @Test
  @DisplayName("내가 쓴 예산지원 신청 목록을 조회한다.")
  void getMyBudgetSupportApplications() {
    given(myInfoRepository.findAllBudgetSupportApplicationsByMemberId(1L)).willReturn(Collections.singletonList(new MyBudgetSupportApplicationDto()));

    List<MyBudgetSupportApplicationDto> result = myInfoService.getMyBudgetSupportApplications();

    assertThat(result).isNotEmpty();
    verify(myInfoRepository).findAllBudgetSupportApplicationsByMemberId(1L);
  }
}
