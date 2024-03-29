package com.inhabas.api.domain.myInfo.repository;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.RequestStatus.PENDING;
import static com.inhabas.api.domain.member.domain.entity.MemberTest.basicMember1;
import static com.inhabas.api.domain.menu.domain.valueObject.MenuType.NOTICE;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.budget.domain.BudgetSupportApplication;
import com.inhabas.api.domain.budget.repository.BudgetApplicationRepository;
import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.comment.repository.CommentRepository;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.MenuGroup;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;
import com.inhabas.api.domain.myInfo.dto.MyBoardDto;
import com.inhabas.api.domain.myInfo.dto.MyBudgetSupportApplicationDto;
import com.inhabas.api.domain.myInfo.dto.MyCommentDto;
import com.inhabas.api.domain.normalBoard.domain.NormalBoard;
import com.inhabas.api.domain.normalBoard.domain.NormalBoardExampleTest;
import com.inhabas.api.domain.normalBoard.repository.NormalBoardRepository;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DefaultDataJpaTest
public class MyInfoRepositoryTest {

  @Autowired MyInfoRepository myInfoRepository;
  @Autowired NormalBoardRepository normalBoardRepository;
  @Autowired CommentRepository commentRepository;
  @Autowired BudgetApplicationRepository budgetApplicationRepository;
  @Autowired TestEntityManager em;

  private Member writer;

  @Transactional
  @BeforeEach
  public void setUp() {
    writer = em.persist(basicMember1());

    MenuGroup normalBoardMenuGroup = em.persist(new MenuGroup("게시판"));
    Menu noticeBoardMenu =
        em.persist(
            Menu.builder()
                .menuGroup(normalBoardMenuGroup)
                .priority(1)
                .type(NOTICE)
                .name("공지사항")
                .description("부원이 알아야 할 내용을 게시합니다.")
                .build());

    NormalBoard noticeBoard1 =
        NormalBoardExampleTest.getBoard1(noticeBoardMenu).writtenBy(writer, NormalBoard.class);
    NormalBoard noticeBoard2 =
        NormalBoardExampleTest.getBoard2(noticeBoardMenu).writtenBy(writer, NormalBoard.class);
    normalBoardRepository.save(noticeBoard1);
    normalBoardRepository.save(noticeBoard2);

    Comment comment1 =
        Comment.builder().content("내가 쓴 댓글").writer(writer).parentBoard(noticeBoard1).build();
    Comment reply1 =
        Comment.builder()
            .content("내가 쓴 대댓글")
            .writer(writer)
            .parentBoard(noticeBoard1)
            .build()
            .replyTo(comment1);
    commentRepository.save(comment1);
    commentRepository.save(reply1);

    MenuGroup budgetBoardMenuGroup = em.persist(new MenuGroup("회계"));
    Menu budgetMenu =
        em.persist(
            Menu.builder()
                .menuGroup(budgetBoardMenuGroup)
                .priority(1)
                .type(MenuType.BUDGET_SUPPORT)
                .name("예산지원신청")
                .description("예산 지원을 신청하는 게시판입니다.")
                .build());

    BudgetSupportApplication application =
        BudgetSupportApplication.builder()
            .applicant(writer)
            .account("123-123-123")
            .title("title")
            .dateUsed(LocalDateTime.now())
            .details("details")
            .menu(budgetMenu)
            .outcome(10000)
            .status(PENDING)
            .build()
            .writtenBy(writer, BudgetSupportApplication.class);
    budgetApplicationRepository.save(application);
  }

  @DisplayName("내가 작성한 글 목록을 내 정보에서 조회한다.")
  @Transactional(readOnly = true)
  @Test
  public void findAllBoardsByMemberId() {
    List<MyBoardDto> myBoards = myInfoRepository.findAllBoardsByMemberId(writer.getId());

    assertThat(myBoards).hasSize(2);
    assertThat(myBoards.get(0).getTitle()).isEqualTo("이건 공지1");
    assertThat(myBoards.get(1).getTitle()).isEqualTo("이건 공지2");
  }

  @DisplayName("내가 작성한 댓글 목록을 내 정보에서 조회한다.")
  @Transactional(readOnly = true)
  @Test
  public void findAllCommentsByMemberId() {
    List<MyCommentDto> myComments = myInfoRepository.findAllCommentsByMemberId(writer.getId());

    assertThat(myComments).hasSize(2);
    // 이 부분은 실제 내용으로 교체 필요
    assertThat(myComments.get(0).getContent()).isEqualTo("내가 쓴 댓글");
    assertThat(myComments.get(1).getContent()).isEqualTo("내가 쓴 대댓글");
  }

  @DisplayName("내가 작성한 예산지원신청 목록을 내 정보에서 조회한다.")
  @Transactional(readOnly = true)
  @Test
  public void findAllBudgetSupportApplicationsByMemberId() {
    List<MyBudgetSupportApplicationDto> myBudgetSupportApplications =
        myInfoRepository.findAllBudgetSupportApplicationsByMemberId(writer.getId());

    assertThat(myBudgetSupportApplications).hasSize(1);
    assertThat(myBudgetSupportApplications.get(0).getTitle()).isEqualTo("title");
  }
}
