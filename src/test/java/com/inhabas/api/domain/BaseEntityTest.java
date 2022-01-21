package com.inhabas.api.domain;

import com.inhabas.api.config.JpaConfig;
import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.NormalBoardRepository;
import com.inhabas.api.domain.board.NormalBoardRepositoryImpl;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.MemberRepository;
import com.inhabas.api.dto.board.BoardDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfig.class})
public class BaseEntityTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void createdTimeTest() {
        //given
        em.persist(MEMBER1);
        NormalBoard board = new NormalBoard("title", "contents")
                .writtenBy(MEMBER1)
                .inCategoryOf(em.getReference(Category.class, 2));

        //when
        em.persist(board);

        //then
        assertThat(board.getCreated()).isNotNull();
        assertThat(board.getCreated()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    public void updatedTimeTest() {
        //given
        em.persist(MEMBER1);
        NormalBoard board = new NormalBoard("title", "contents")
                .writtenBy(MEMBER1)
                .inCategoryOf(em.getReference(Category.class, 2));
        em.persist(board);

        //when
        NormalBoard param = new NormalBoard(board.getId(), "new title", "new contents")
                .writtenBy(MEMBER1)
                .inCategoryOf(em.getReference(Category.class, 2));
        em.merge(param);
        em.flush();em.clear();

        //then
        NormalBoard find = em.find(NormalBoard.class, board.getId());
        assertThat(find.getUpdated()).isNotNull();
        assertThat(find.getUpdated()).isInstanceOf(LocalDateTime.class);
    }

}
