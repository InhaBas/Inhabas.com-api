package com.inhabas.api.domain;

import com.inhabas.api.config.JpaConfig;
import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.comment.Comment;
import com.inhabas.api.domain.comment.CommentRepository;
import com.inhabas.api.domain.member.Member;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static com.inhabas.api.domain.MemberTest.MEMBER2;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    TestEntityManager em;

    Member writer, commentWriter;
    NormalBoard normalBoard;

    @BeforeEach
    public void setUp() {
        writer = em.persist(MEMBER1);
        commentWriter = em.persist(MEMBER2);
        normalBoard = em.persist(NormalBoardTest.getFreeBoard().writtenBy(writer));
    }

    @AfterEach
    public void clearPersistenceContext() {
        em.flush();
        em.clear();
    }

    @DisplayName("작성한 댓글과 저장된 댓글이 같다.")
    @Test
    public void Success_Save_Comment() {
        //when
        Comment newComment = new Comment("필력 좋다 쓴이야").writtenBy(commentWriter);
        normalBoard.addComment(newComment);  // 연관관계 설정.
        Comment saveComment = commentRepository.save(newComment);

        //then
        assertThat(saveComment.equals(newComment)).isTrue();
        assertThat(saveComment).isIn(normalBoard.getComments());
    }

    @DisplayName("대댓글을 성공적으로 등록한다.")
    @Test
    public void Success_Save_Reply() {
        //given
        Comment comment = new Comment("필력 좋다 쓴이야").writtenBy(commentWriter);
        normalBoard.addComment(comment);  // 연관관계 설정.
        Comment saveComment = commentRepository.save(comment);

        em.flush();
        em.clear();

        //when
        Comment reply = new Comment("익1 고마워").writtenBy(writer);
        comment.addReply(reply);  // 대댓글을 달았다.
        Comment savedReply = commentRepository.save(comment).getChildren().get(0);

        //then
        assertThat(savedReply.getContents()).isEqualTo("익1 고마워");
        assertThat(savedReply.getParentBoard().getId()).isEqualTo(normalBoard.getId());
        assertThat(normalBoard.getComments().stream() // 게시판에 달린 댓글 리스트 하위에 작성한 대댓글이 달려있는가?
                .anyMatch(_comment -> _comment.getChildren().contains(reply))).isTrue();
    }




}
