package com.inhabas.api.service.board;

import com.inhabas.api.domain.board.NormalBoardRepository;
import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.dto.board.SaveBoardDto;
import com.inhabas.api.dto.board.UpdateBoardDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@Transactional
public class BoardServiceImpl implements BoardService {

    private final NormalBoardRepository boardRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    public BoardServiceImpl (NormalBoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    @Override
    public NormalBoard write(SaveBoardDto saveBoardDto) {
        Category category = em.getReference(Category.class, saveBoardDto.getCategoryId());
        return boardRepository.save(saveBoardDto.toEntity());
    }

    @Override
    public NormalBoard update(UpdateBoardDto updateBoardDto) {
        NormalBoard entity = updateBoardDto.toEntity();
        if(DoesExistBoard(entity)){
            return boardRepository.save(entity);
        } else {
            return null; // 확인 필요
        }
    }

    private boolean DoesExistBoard(NormalBoard normalBoard){
        return boardRepository.findById(normalBoard.getId()).isPresent();
    }

    @Override
    public void delete(Integer id) {
        boardRepository.deleteById(id);
    }

    @Override
    public Optional<NormalBoard> getBoard(Integer categoryId, Integer id) {

        return boardRepository.findById(id);
    }

    @Override
    public Page<NormalBoard> getBoardList(Pageable pageable, Integer categoryId) {
        if (categoryId == null)
            return boardRepository.findAll(pageable);
        else
            return boardRepository.findAllByCategoryId(categoryId, pageable);
    }
}
