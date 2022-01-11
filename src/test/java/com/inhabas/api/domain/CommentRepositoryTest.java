package com.inhabas.api.domain;

import com.inhabas.api.config.JpaConfig;
import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.comment.CommentRepository;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.MemberRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static com.inhabas.api.domain.NormalBoardTest.FREE_BOARD;


@DataJpaTest
@Import(JpaConfig.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentRepositoryTest {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    TestEntityManager em;

    Member writer;
    NormalBoard normalBoard;

/*    @BeforeEach
    public void setUp() {

    }*/

    @DisplayName("댓글을 성공적으로 저장한다.")
    @Test
    public void save_comment() {

    }

}
