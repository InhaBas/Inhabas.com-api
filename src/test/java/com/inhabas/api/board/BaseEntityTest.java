package com.inhabas.api.board;

import com.inhabas.api.domain.board.Board;
import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.member.IbasInformation;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.SchoolInformation;
import com.inhabas.api.repository.board.JpaBoardRepository;
import com.inhabas.api.repository.member.JpaMemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class BaseEntityTest {

    @Autowired
    JpaMemberRepository memberRepository;
    @Autowired
    JpaBoardRepository boardRepository;

    @Test
    public void createdTimeTest() {
        //given
        Member member = new Member(12171123, "유동현", "010-1111-1111", null, new SchoolInformation(), new IbasInformation());
        memberRepository.save(member);
        Board board = new Board("title", "contents", member, Category.free);

        //when
        Board save = boardRepository.save(board);

        //then
        assertThat(save.getCreated()).isNotNull();
        assertThat(save.getCreated()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    public void updatedTimeTest() {
        //given
        Member member = new Member(12171123, "유동현", "010-1111-1111", null, new SchoolInformation(), new IbasInformation());
        member = memberRepository.save(member);
        Board board = new Board("title", "contents", member, Category.free);
        boardRepository.save(board);

        //when
        Board param = new Board(board.getId(), "new title", "new contents", member, Category.free);
        Board updateBoard = boardRepository.update(param);

        //then
        assertThat(updateBoard.getUpdated()).isNotNull();
        assertThat(updateBoard.getUpdated()).isInstanceOf(LocalDateTime.class);
    }

}
