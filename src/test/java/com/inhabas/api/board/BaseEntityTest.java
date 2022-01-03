package com.inhabas.api.board;

import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.member.IbasInformation;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.SchoolInformation;
import com.inhabas.api.repository.board.BoardRepository;
import com.inhabas.api.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
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
        Member member = new Member(12171123, "유동현", "010-1111-1111", null, new SchoolInformation(), new IbasInformation());
        memberRepository.save(member);
        NormalBoard board = new NormalBoard("title", "contents", member, Category.free);

        //when
        NormalBoard save = boardRepository.save(board);

        //then
        assertThat(save.getCreated()).isNotNull();
        assertThat(save.getCreated()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    public void updatedTimeTest() {
        //given
        Member member = new Member(12171123, "유동현", "010-1111-1111", null, new SchoolInformation(), new IbasInformation());
        member = memberRepository.save(member);
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
