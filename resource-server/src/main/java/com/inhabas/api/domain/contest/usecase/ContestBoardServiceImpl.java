package com.inhabas.api.domain.contest.usecase;

import com.inhabas.api.domain.board.BoardNotFoundException;
import com.inhabas.api.domain.contest.domain.ContestBoard;
import com.inhabas.api.domain.contest.repository.ContestBoardRepository;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
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
    public Integer write(MemberId memberId, SaveContestBoardDto dto) {

        ContestBoard contestBoard = ContestBoard.builder()
                .title(dto.getTitle())
                .contents(dto.getContents())
                .association(dto.getAssociation())
                .topic(dto.getTopic())
                .start(dto.getStart())
                .deadline(dto.getDeadline())
                .build()
                        .writtenBy(memberId);
        return contestBoardRepository.save(contestBoard).getId();
    }

    @Override
    public Integer update(MemberId memberId, UpdateContestBoardDto dto) {

        ContestBoard savedContestBoard = contestBoardRepository.findById(dto.getId())
                .orElseThrow(BoardNotFoundException::new);

        ContestBoard entity = ContestBoard.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .contents(dto.getContents())
                .association(dto.getAssociation())
                .topic(dto.getTopic())
                .start(dto.getStart())
                .deadline(dto.getDeadline())
                .build()
                .writtenBy(memberId)
                .inMenu(savedContestBoard.getMenuId());

        return contestBoardRepository.save(entity).getId();
    }


    @Override
    public void delete(Integer id) {
        contestBoardRepository.deleteById(id);
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
