package com.inhabas.api.repository.board;

import com.inhabas.api.domain.board.Board;
import com.inhabas.api.domain.board.Category;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@Transactional // BoardService 에 달아줘야함. 현재는 임시로 여기에.
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
    public Optional<Board> findById(Integer id) {
        return Optional.ofNullable(em.find(Board.class, id));
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
        em.remove(this.findById(id)
                .orElseThrow(EntityNotFoundException::new));
    }

    @Override
    public Board update(Board board) {
        if (exist(board)) {
            board = em.merge(board);
            em.flush(); // persist context 에 반영된 수정내용을 db 에 update query 보내게 함.
            return board;
        }
        else
            throw new EntityNotFoundException();
    }

    private boolean exist(Board board) {
        return this.findById(board.getId()).isPresent();
    }
}
