package com.inhabas.api.service.board;

import com.inhabas.api.domain.board.BoardRepository;
import com.inhabas.api.dto.NormalBoardDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Slf4j
@Transactional
public class BoardServiceImpl implements BoardService {

    private BoardRepository boardRepository;

    @Override
    public void write(NormalBoardDto normalBoardDto) {
    }

    @Override
    public void modify(NormalBoardDto board) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void getBoard(NormalBoardDto board) {

    }

    @Override
    public List<NormalBoardDto> getBoardList(NormalBoardDto board) {
        return null;
    }
}
