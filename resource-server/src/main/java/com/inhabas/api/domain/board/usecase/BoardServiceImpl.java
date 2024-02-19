package com.inhabas.api.domain.board.usecase;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.board.domain.NormalBoard;
import com.inhabas.api.domain.board.dto.BoardDto;
import com.inhabas.api.domain.board.dto.SaveBoardDto;
import com.inhabas.api.domain.board.dto.UpdateBoardDto;
import com.inhabas.api.domain.board.exception.OnlyWriterModifiableException;
import com.inhabas.api.domain.board.repository.NormalBoardRepository;
import com.inhabas.api.domain.menu.repository.MenuRepository;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

  private final NormalBoardRepository boardRepository;
  private final MenuRepository menuRepository;
  private final MemberRepository memberRepository;

  @Override
  public Long write(Long memberId, SaveBoardDto saveBoardDto) {

    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

    NormalBoard normalBoard =
        new NormalBoard(saveBoardDto.getTitle(), saveBoardDto.getContents())
            .writtenBy(writer, NormalBoard.class);

    return boardRepository.save(normalBoard).getId();
  }

  @Override
  public Long update(Long memberId, UpdateBoardDto updateBoardDto) {

    NormalBoard savedBoard =
        boardRepository.findById(updateBoardDto.getId()).orElseThrow(NotFoundException::new);
    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

    savedBoard.modify(updateBoardDto.getTitle(), updateBoardDto.getContents(), writer);

    return boardRepository.save(savedBoard).getId();
  }

  @Override
  public void delete(Long memberId, Long boardId) {

    NormalBoard board = boardRepository.findById(boardId).orElseThrow(NotFoundException::new);
    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

    if (board.cannotModifiableBy(writer)) {
      throw new OnlyWriterModifiableException();
    }

    boardRepository.deleteById(boardId);
  }

  @Override
  public BoardDto getBoard(Long id) {

    return boardRepository.findDtoById(id).orElseThrow(NotFoundException::new);
  }

  @Override
  public Page<BoardDto> getBoardList(Integer menuId, Pageable pageable) {

    return boardRepository.findAllByMenuId(menuId, pageable);
  }
}
