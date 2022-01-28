package com.inhabas.api.service.board;

import com.inhabas.api.domain.board.NormalBoard;
import com.inhabas.api.domain.board.NormalBoardRepository;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.MemberRepository;
import com.inhabas.api.domain.menu.Menu;
import com.inhabas.api.domain.menu.MenuRepository;
import com.inhabas.api.dto.board.BoardDto;

import com.inhabas.api.dto.board.SaveBoardDto;
import com.inhabas.api.dto.board.UpdateBoardDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;

import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final NormalBoardRepository boardRepository;
    private final MenuRepository menuRepository;
    private final MemberRepository memberRepository;

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Integer write(SaveBoardDto saveBoardDto) {
        Menu menu = menuRepository.getById(saveBoardDto.getMenuId());
        Member writer = memberRepository.findById(saveBoardDto.getLoginedUser()).orElseThrow();
        NormalBoard normalBoard = new NormalBoard(saveBoardDto.getTitle(), saveBoardDto.getContents())
                .inMenu(menu)
                .writtenBy(writer);
        return boardRepository.save(normalBoard).getId();
    }

    @Override
    public Integer update(UpdateBoardDto updateBoardDto) {
        NormalBoard entity = boardRepository.findById(updateBoardDto.getId()).orElseThrow();
//        NormalBoard updateBoard = new NormalBoard(updateBoardDto.getId(), updateBoardDto.getTitle(), updateBoardDto.getContents());
        entity.setTitleContents(updateBoardDto.getTitle(), updateBoardDto.getContents());
        Member writer = memberRepository.findById(updateBoardDto.getLoginedUser()).orElseThrow();
        if(entity.isWriter(writer))
            return boardRepository.save(entity).getId();
        else{
            throw new RuntimeException("잘못된 사용자");
        }
    }

    @Override
    public void delete(Integer id) {
        boardRepository.deleteById(id);
    }

    @Override
    public Optional<BoardDto> getBoard(Integer id) {
        return boardRepository.findDtoById(id);
    }

    @Override
    public Page<BoardDto> getBoardList(Integer menuId, Pageable pageable) {
            return boardRepository.findAllByMenuId(menuId, pageable);
    }

}
