package com.inhabas.api.domain.contest.usecase;

import com.inhabas.api.domain.board.BoardCannotModifiableException;
import com.inhabas.api.domain.board.BoardNotFoundException;
import com.inhabas.api.domain.contest.domain.ContestBoard;
import com.inhabas.api.domain.contest.repository.ContestBoardRepository;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.menu.domain.valueObject.MenuId;
import com.inhabas.api.domain.contest.dto.DetailContestBoardDto;
import com.inhabas.api.domain.contest.dto.ListContestBoardDto;
import com.inhabas.api.domain.contest.dto.SaveContestBoardDto;
import com.inhabas.api.domain.contest.dto.UpdateContestBoardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ContestBoardServiceImpl implements ContestBoardService {

    private final ContestBoardRepository contestBoardRepository;

    @Override
    public Integer write(StudentId studentId, SaveContestBoardDto dto) {

        ContestBoard contestBoard = ContestBoard.builder()
                .title(dto.getTitle())
                .contents(dto.getContents())
                .association(dto.getAssociation())
                .topic(dto.getTopic())
                .start(dto.getStart())
                .deadline(dto.getDeadline())
                .build()
                        .writtenBy(studentId);
        return contestBoardRepository.save(contestBoard).getId();
    }

    @Override
    public Integer update(StudentId studentId, UpdateContestBoardDto dto) {

        ContestBoard contestBoard = contestBoardRepository.findById(dto.getId())
                .orElseThrow(BoardNotFoundException::new);

        contestBoard.modify(
                dto.getTitle(),
                dto.getContents(),
                dto.getAssociation(),
                dto.getTopic(),
                dto.getStart(),
                dto.getDeadline(),
                studentId);

        return contestBoardRepository.save(contestBoard).getId();
    }


    @Override
    public void delete(StudentId studentId, Integer boardId) {

        ContestBoard contestBoard = contestBoardRepository.findById(boardId)
                .orElseThrow(BoardNotFoundException::new);

        if (contestBoard.cannotModifiableBy(studentId)) {
            throw new BoardCannotModifiableException("삭제 권한이 없습니다.");
        }

        contestBoardRepository.deleteById(boardId);
    }

    @Override
    public DetailContestBoardDto getBoard(Integer id) {
        return  contestBoardRepository.findDtoById(id)
                .orElseThrow(BoardNotFoundException::new);
    }

    @Override
    public Page<ListContestBoardDto> getBoardList(MenuId menuId, Pageable pageable) {
        return contestBoardRepository.findAllByMenuId(menuId, pageable);
    }
}
