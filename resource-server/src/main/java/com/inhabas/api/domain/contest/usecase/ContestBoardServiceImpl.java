package com.inhabas.api.domain.contest.usecase;

import javax.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.inhabas.api.domain.contest.repository.ContestBoardRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ContestBoardServiceImpl implements ContestBoardService {

  private final ContestBoardRepository contestBoardRepository;

  //    @Override
  //    public Integer write(StudentId studentId, SaveContestBoardDto dto) {
  //
  //        ContestBoard contestBoard = ContestBoard.builder()
  //                .title(dto.getTitle())
  //                .contents(dto.getContent())
  //                .association(dto.getAssociation())
  //                .topic(dto.getTopic())
  //                .start(dto.getStart())
  //                .deadline(dto.getDeadline())
  //                .build()
  //                        .writtenBy(studentId);
  //        return contestBoardRepository.save(contestBoard).getId();
  //    }
  //
  //    @Override
  //    public Integer update(StudentId studentId, UpdateContestBoardDto dto) {
  //
  //        ContestBoard contestBoard = contestBoardRepository.findById(dto.getId())
  //                .orElseThrow(BoardNotFoundException::new);
  //
  //        contestBoard.modify(
  //                dto.getTitle(),
  //                dto.getContent(),
  //                dto.getAssociation(),
  //                dto.getTopic(),
  //                dto.getStart(),
  //                dto.getDeadline(),
  //                studentId);
  //
  //        return contestBoardRepository.save(contestBoard).getId();
  //    }
  //
  //
  //    @Override
  //    public void delete(StudentId studentId, Integer boardId) {
  //
  //        ContestBoard contestBoard = contestBoardRepository.findById(boardId)
  //                .orElseThrow(BoardNotFoundException::new);
  //
  //        if (contestBoard.cannotModifiableBy(studentId)) {
  //            throw new OnlyWriterModifiableException("삭제 권한이 없습니다.");
  //        }
  //
  //        contestBoardRepository.deleteById(boardId);
  //    }
  //
  //    @Override
  //    public DetailContestBoardDto getBoard(Integer id) {
  //        return  contestBoardRepository.findDtoById(id)
  //                .orElseThrow(BoardNotFoundException::new);
  //    }
  //
  //    @Override
  //    public Page<ListContestBoardDto> getBoardList(MenuId menuId, Pageable pageable) {
  //        return contestBoardRepository.findAllByMenuId(menuId, pageable);
  //    }
}
