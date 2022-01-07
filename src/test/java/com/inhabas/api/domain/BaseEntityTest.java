package com.inhabas.api.domain;

import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.board.BoardRepository;
import com.inhabas.api.domain.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BaseEntityTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BoardRepository boardRepository;
    @PersistenceContext
    EntityManager em;

    @Test
    public void createdTimeTest() {
        //given
        memberRepository.save(MEMBER1);
        NormalBoard board = new NormalBoard("title", "contents", MEMBER1, Category.free);

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
        NormalBoard board = new NormalBoard("title", "contents", member, Category.free);
        boardRepository.save(board);

        //when
        NormalBoard param = new NormalBoard(board.getId(), "new title", "new contents", member, Category.free);
        NormalBoard updateBoard = boardRepository.save(param);
        em.flush();

        //then
        assertThat(updateBoard.getUpdated()).isNotNull();
        assertThat(updateBoard.getUpdated()).isInstanceOf(LocalDateTime.class);
    }

}
