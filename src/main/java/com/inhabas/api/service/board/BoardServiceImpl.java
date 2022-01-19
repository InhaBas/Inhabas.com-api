package com.inhabas.api.service.board;

import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.board.NormalBoardRepository;
import com.inhabas.api.dto.board.BoardDto;

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
import java.util.Optional;


@Service
@Slf4j
@Transactional
public class BoardServiceImpl implements BoardService {

    private NormalBoardRepository boardRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    public BoardServiceImpl (NormalBoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    @Override
    public Integer write(SaveBoardDto saveBoardDto) {
        return boardRepository.save(saveBoardDto.toEntity()).getId();
    }

    @Override
    public Integer update(UpdateBoardDto updateBoardDto) {
        NormalBoard entity = updateBoardDto.toEntity();
        if(DoesExistBoard(entity)){
            return boardRepository.save(entity).getId();
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
    public Optional<BoardDto> getBoard(Integer categoryId, Integer id) {
        return boardRepository.findDtoById(id);
    }

    @Override
    public Page<BoardDto> getBoardList(Integer menuId, Pageable pageable) {
            return boardRepository.findAllByMenuId(menuId, pageable);
    }
}
