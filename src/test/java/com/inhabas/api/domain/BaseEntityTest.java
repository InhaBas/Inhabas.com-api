package com.inhabas.api.domain;

import com.inhabas.api.config.JpaConfig;
import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.board.NormalBoardRepository;
import com.inhabas.api.domain.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
public class BaseEntityTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    NormalBoardRepository boardRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void createdTimeTest() {
        //given
        memberRepository.save(MEMBER1);
        NormalBoard board = new NormalBoard("title", "contents")
                .writtenBy(MEMBER1)
                .inCategoryOf(em.getReference(Category.class, 2));

        //when
        NormalBoard save = boardRepository.save(board);

        //then
        assertThat(save.getCreated()).isNotNull();
        assertThat(save.getCreated()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    public void updatedTimeTest() {
        //given
        Member member = memberRepository.save(MEMBER1);
        NormalBoard board = new NormalBoard("title", "contents")
                .writtenBy(member)
                .inCategoryOf(em.getReference(Category.class, 2));
        boardRepository.save(board);

        //when
        NormalBoard param = new NormalBoard(board.getId(), "new title", "new contents")
                .writtenBy(member)
                .inCategoryOf(em.getReference(Category.class, 2));
        NormalBoard updateBoard = boardRepository.save(param);
        em.flush();

        //then
        assertThat(updateBoard.getUpdated()).isNotNull();
        assertThat(updateBoard.getUpdated()).isInstanceOf(LocalDateTime.class);
    }

}
