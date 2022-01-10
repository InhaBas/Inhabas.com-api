package com.inhabas.api.service.board;

import com.inhabas.api.domain.board.BoardRepository;
import com.inhabas.api.domain.board.Category;
import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.dto.NormalBoardDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class BoardServiceImpl implements BoardService {

    private BoardRepository boardRepository;

    @Autowired
    public BoardServiceImpl (BoardRepository boardRepository){
        this.boardRepository = boardRepository;
    }

    @Override
    public Integer write(NormalBoardDto normalBoardDto) {
        return boardRepository.save(normalBoardDto.toEntity()).getId();
    }

    @Override
    public Integer modify(NormalBoardDto normalBoardDto) {
        return boardRepository.save(normalBoardDto.toEntity()).getId();
    }

    @Override
    public void delete(Integer id) {
        boardRepository.deleteById(id);
    }

    @Override
    public Optional<NormalBoard> getBoard(Integer id) {
        return boardRepository.findById(id);
    }

    @Override
    public List<NormalBoard> getBoardList(Category category) {
        return boardRepository.findAllByCategory(category);
    }
}
