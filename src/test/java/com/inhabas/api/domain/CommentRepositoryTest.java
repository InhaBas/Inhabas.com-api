package com.inhabas.api.domain;

import com.inhabas.api.config.JpaConfig;
import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.comment.Comment;
import com.inhabas.api.domain.comment.CommentRepository;
import com.inhabas.api.domain.member.Member;

import com.inhabas.api.dto.CommentDetailDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import java.util.List;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static com.inhabas.api.domain.MemberTest.MEMBER2;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@Import(JpaConfig.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
        normalBoard = em.persist(
                NormalBoardTest.getFreeBoard()
                        .writtenBy(writer)
                        .inCategoryOf(em.find(Category.class, 2))
        );
    }

    @DisplayName("작성한 댓글과 저장된 댓글이 같다.")
    @Test
    public void Success_Save_Comment() {
        //when
        Comment newComment = new Comment("필력 좋다 쓴이야").writtenBy(commentWriter);
        normalBoard.addComment(newComment);  // 연관관계 설정.
        Comment saveComment = commentRepository.save(newComment);

        em.clear();

        //then
        assertThat(saveComment.getContents()).isEqualTo("필력 좋다 쓴이야");
        assertThat(saveComment.getParentBoard().getId()).isEqualTo(normalBoard.getId());
    }

    @DisplayName("대댓글을 성공적으로 등록한다.")
    @Test
    public void Success_Save_Reply() {
        //given
        Comment comment = new Comment("필력 좋다 쓴이야").writtenBy(commentWriter);
        normalBoard.addComment(comment);  // 연관관계 설정.
        Comment saveComment = commentRepository.save(comment);

        em.clear();

        //when
        Comment reply = new Comment("익1 고마워").writtenBy(writer);
        saveComment.addReply(reply);  // 대댓글을 달았다.
        Comment savedReply = commentRepository.save(reply);

        //then
        assertThat(savedReply.getContents()).isEqualTo("익1 고마워");
        assertThat(savedReply.getParentBoard().getId()).isEqualTo(normalBoard.getId());
        assertThat(savedReply.getParentComment().getId()).isEqualTo(saveComment.getId());
    }

    @DisplayName("게시글의 모든 댓글을 계층 구조로 가져온다.")
    @Test
    public void Hierarchical_Comment_List() {
        //given
        Comment comment1 = new Comment("1) 필력 좋다 쓴이야").writtenBy(commentWriter);
        normalBoard.addComment(comment1); // 댓글을 달았다.
        commentRepository.save(comment1);

        Comment reply1 = new Comment("1-1) 고마워").writtenBy(writer);
        comment1.addReply(reply1); // 대댓글을 달았다.
        commentRepository.save(reply1);

        Comment comment2 = new Comment("2) 쓴이야 분발하자").writtenBy(commentWriter);
        normalBoard.addComment(comment2); // 댓글을 달았다.
        commentRepository.save(comment2);

        Comment reply2_1 = new Comment("2-1) 너 누구야?").writtenBy(writer);
        comment2.addReply(reply2_1); // 대댓글을 달았다.
        commentRepository.save(reply2_1);

        Comment reply2_2 = new Comment("2-2) 나? 김첨지").writtenBy(commentWriter);
        comment2.addReply(reply2_2); // 대댓글을 달았다
        commentRepository.save(reply2_2);

        em.clear();


        //when
        List<CommentDetailDto> commentList = commentRepository.findAllByParentBoardIdOrderByCreated(normalBoard.getId());


        //then
        assertThat(commentList.size()).isEqualTo(2); // 루트 댓글은 2 개이다.

        // 첫번째 루트 댓글
        assertThat(commentList.get(0).getContents()).isEqualTo("1) 필력 좋다 쓴이야");
        // 첫번째 루트 댓글의 대댓글은 1개
        assertThat(commentList.get(0).getChildren())
                .hasSize(1)
                .extracting(CommentDetailDto::getContents)
                .contains("1-1) 고마워");

        // 두번째 루트 댓글
        assertThat(commentList.get(1).getContents()).isEqualTo("2) 쓴이야 분발하자");
        // 두번째 루트 댓글의 대댓글은 2개
        assertThat(commentList.get(1).getChildren())
                .hasSize(2)
                .extracting(CommentDetailDto::getContents)
                .containsExactly("2-1) 너 누구야?", "2-2) 나? 김첨지");
    }

    @DisplayName("루트 댓글을 삭제하면 대댓글도 같이 삭제된다.")
    @Test
    public void Remove_Root_Comment() {
        //given
        Comment comment = new Comment("root) 첫 댓글").writtenBy(commentWriter);
        normalBoard.addComment(comment); // 댓글을 달았다.
        commentRepository.save(comment);

        Comment reply_1 = new Comment("reply1) 대댓글1").writtenBy(writer);
        comment.addReply(reply_1); // 대댓글을 달았다.
        commentRepository.save(reply_1);

        Comment reply_2 = new Comment("reply2) 대댓글2").writtenBy(commentWriter);
        comment.addReply(reply_2); // 대댓글을 달았다
        commentRepository.save(reply_2);

        em.clear();


        //when
        commentRepository.delete(comment);

        //then
        List<Comment> all = commentRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }

    @DisplayName("대댓글을 삭제한다.")
    @Test
    public void Remove_Reply() {
        //given
        Comment comment = new Comment("root) 첫 댓글").writtenBy(commentWriter);
        normalBoard.addComment(comment); // 댓글을 달았다.
        comment = commentRepository.save(comment);

        Comment reply_1 = new Comment("reply1) 대댓글1").writtenBy(writer);
        comment.addReply(reply_1); // 대댓글을 달았다.
        commentRepository.save(reply_1);

        Comment reply_2 = new Comment("reply2) 대댓글2").writtenBy(commentWriter);
        comment.addReply(reply_2); // 대댓글을 달았다
        reply_2 = commentRepository.save(reply_2);

        Comment reply_3 = new Comment("reply3) 대댓글3").writtenBy(commentWriter);
        comment.addReply(reply_3); // 대댓글을 달았다
        commentRepository.save(reply_3);

        em.clear();


        //when
        commentRepository.deleteById(reply_2.getId());


        //then
        List<Comment> all = commentRepository.findAll();

        assertThat(all)
                .hasSize(3)
                .extracting(Comment::getContents)
                .doesNotContain(reply_2.getContents())
                .contains(
                        comment.getContents(),
                        reply_1.getContents(),
                        reply_3.getContents()
                );

    }

    @DisplayName("게시판이 삭제되면 해당 댓글이 모두 삭제된다.")
    @Test
    public void Remove_Board_Then_All_The_Comments_Of_The_Board_Are_Gone() {
        //given
        Comment comment = new Comment("root) 첫 댓글").writtenBy(commentWriter);
        normalBoard.addComment(comment); // 댓글을 달았다.
        commentRepository.save(comment);

        Comment reply_1 = new Comment("reply1) 대댓글1").writtenBy(writer);
        comment.addReply(reply_1); // 대댓글을 달았다.
        commentRepository.save(reply_1);

        Comment reply_2 = new Comment("reply2) 대댓글2").writtenBy(commentWriter);
        comment.addReply(reply_2); // 대댓글을 달았다
        commentRepository.save(reply_2);

        Comment reply_3 = new Comment("reply3) 대댓글3").writtenBy(commentWriter);
        comment.addReply(reply_3); // 대댓글을 달았다
        commentRepository.save(reply_3);

        em.clear();


        //when
        normalBoard = em.find(NormalBoard.class, normalBoard.getId());
        em.remove(normalBoard);

        //then
        List<Comment> all = commentRepository.findAll();
        assertThat(all.size()).isEqualTo(0);
    }

}
