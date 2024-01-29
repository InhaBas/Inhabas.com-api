package com.inhabas.api.domain.comment.repository;

import static com.inhabas.api.domain.member.domain.entity.MemberTest.basicMember1;
import static com.inhabas.api.domain.member.domain.entity.MemberTest.basicMember2;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.domain.board.domain.AlbumBoard;
import com.inhabas.api.domain.board.domain.valueObject.AlbumExampleTest;
import com.inhabas.api.domain.comment.domain.Comment;
import com.inhabas.api.domain.comment.dto.CommentDetailDto;
import com.inhabas.api.domain.menu.domain.Menu;
import com.inhabas.api.domain.menu.domain.MenuGroup;
import com.inhabas.api.domain.menu.domain.valueObject.MenuType;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DefaultDataJpaTest
public class CommentRepositoryTest {

  @Autowired CommentRepository commentRepository;
  @Autowired TestEntityManager em;

  Member boardWriter, commentWriter;
  AlbumBoard albumBoard;
  Menu menu;
  MenuGroup menuGroup;

  @BeforeEach
  public void setUp() {

    MenuGroup newMenuGroup = new MenuGroup("IBAS");
    menuGroup = em.persist(newMenuGroup);
    Menu newMenu = new Menu(menuGroup, 2, MenuType.ALBUM, "동아리 활동", "설명");
    menu = em.persist(newMenu);

    boardWriter = em.persist(basicMember1());
    commentWriter = em.persist(basicMember2());
    albumBoard =
        em.persist(AlbumExampleTest.getAlbumBoard1(menu).writtenBy(boardWriter, AlbumBoard.class));
  }

  @DisplayName("작성한 댓글과 저장된 댓글이 같다.")
  @Test
  @Transactional
  public void Success_Save_Comment() {
    // given
    Comment newComment = new Comment("필력 좋다 쓴이야", commentWriter, albumBoard);

    // when
    Comment saveComment = commentRepository.save(newComment);
    em.clear();

    // then
    assertThat(saveComment.getContent()).isEqualTo("필력 좋다 쓴이야");
    assertThat(saveComment.getParentBoard().getId()).isEqualTo(albumBoard.getId());
  }

  @DisplayName("대댓글을 성공적으로 등록한다.")
  @Test
  public void Success_Save_Reply() {
    // given
    Comment comment = new Comment("필력 좋다 쓴이야", commentWriter, albumBoard);
    commentRepository.save(comment);
    em.clear();

    // when
    Comment reply = new Comment("익1 고마워", boardWriter, albumBoard).replyTo(comment);
    Comment savedReply = commentRepository.save(reply);

    // then
    assertThat(savedReply.getContent()).isEqualTo("익1 고마워");
    assertThat(savedReply.getParentBoard().getId()).isEqualTo(albumBoard.getId());
    assertThat(savedReply.getParentComment().getId()).isEqualTo(comment.getId());
  }

  @DisplayName("게시글의 모든 댓글을 계층 구조로 가져온다.")
  @Test
  public void Hierarchical_Comment_List() {
    // given
    // 댓글을 달았다.
    Comment comment1 = new Comment("1) 필력 좋다 쓴이야", commentWriter, albumBoard);
    commentRepository.save(comment1);

    // 대댓글을 달았다.
    Comment reply1 = new Comment("1-1) 고마워", boardWriter, albumBoard).replyTo(comment1);
    commentRepository.save(reply1);

    // 댓글을 달았다.
    Comment comment2 = new Comment("2) 쓴이야 분발하자", commentWriter, albumBoard);
    commentRepository.save(comment2);

    // 대댓글을 달았다.
    Comment reply2_1 = new Comment("2-1) 너 누구야?", boardWriter, albumBoard).replyTo(comment2);
    commentRepository.save(reply2_1);

    // 대대댓글을 달았다.
    Comment reply2_2 = new Comment("2-2) 나? 김첨지", commentWriter, albumBoard).replyTo(reply2_1);
    commentRepository.save(reply2_2);
    em.clear();

    // when
    List<CommentDetailDto> commentList =
        commentRepository.findAllByParentBoardIdOrderByCreated(albumBoard.getId());

    // then
    assertThat(commentList.size()).isEqualTo(2); // 루트 댓글은 2 개이다.

    // 첫번째 루트 댓글
    assertThat(commentList.get(0).getContent()).isEqualTo(comment1.getContent());
    // 첫번째 루트 댓글의 대댓글은 1개
    assertThat(commentList.get(0).getChildrenComment())
        .hasSize(1)
        .extracting(CommentDetailDto::getContent)
        .contains(reply1.getContent());

    // 두번째 루트 댓글
    assertThat(commentList.get(1).getContent()).isEqualTo(comment2.getContent());
    // 두번째 루트 댓글의 대댓글은 1개
    assertThat(commentList.get(1).getChildrenComment())
        .hasSize(1)
        .extracting(CommentDetailDto::getContent)
        .containsExactly(reply2_1.getContent());
  }

  @DisplayName("게시판이 삭제되면 해당 댓글이 모두 삭제된다.")
  @Test
  public void Remove_Board_Then_All_The_Comments_Of_The_Board_Are_Gone() {
    // given
    Comment comment = new Comment("root) 첫 댓글", commentWriter, albumBoard);
    commentRepository.save(comment);

    Comment reply_1 = new Comment("reply1) 대댓글1", boardWriter, albumBoard).replyTo(comment);
    commentRepository.save(reply_1);

    Comment reply_2 = new Comment("reply2) 대댓글2", commentWriter, albumBoard).replyTo(comment);
    commentRepository.save(reply_2);

    Comment reply_3 = new Comment("reply3) 대댓글3", commentWriter, albumBoard).replyTo(comment);
    commentRepository.save(reply_3);
    em.clear();

    // when
    albumBoard = em.find(AlbumBoard.class, albumBoard.getId());
    em.remove(albumBoard);

    // then
    List<Comment> all = commentRepository.findAll();
    assertThat(all).hasSize(0);
  }
}
