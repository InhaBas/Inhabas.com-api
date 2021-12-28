package com.inhabas.api.repository.board;

import com.inhabas.api.domain.board.Board;
import com.inhabas.api.domain.board.Category;
import com.inhabas.api.dto.BoardDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JpaBoardRepository implements BoardRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Board save(Board board) {
        em.persist(board);
        return em.find(Board.class, board.getId());
    }

    @Override
    public Board findById(Integer id) {
        return em.find(Board.class, id);
    }

    @Override
    public List<Board> findAll() {
        return em.createQuery("select b from Board b", Board.class)
                .getResultList();
    }

    @Override
    public List<Board> findAllByCategory(Category type) {
        return em.createQuery("select b from Board b where b.category=:type", Board.class)
                .setParameter("type", type)
                .getResultList();
    }

    @Override
    public void deleteById(Integer id) {
        em.remove(this.findById(id));
    }

    @Override
    public void update(Board board) {
        em.merge(board);
    }
}
