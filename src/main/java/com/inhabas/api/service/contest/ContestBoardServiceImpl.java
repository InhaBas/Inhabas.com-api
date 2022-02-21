package com.inhabas.api.service.contest;

import com.inhabas.api.domain.contest.ContestBoard;
import com.inhabas.api.domain.contest.ContestBoardRepository;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.MemberRepository;
import com.inhabas.api.domain.menu.Menu;
import com.inhabas.api.domain.menu.MenuRepository;
import com.inhabas.api.dto.contest.DetailContestBoardDto;
import com.inhabas.api.dto.contest.ListContestBoardDto;
import com.inhabas.api.dto.contest.SaveContestBoardDto;
import com.inhabas.api.dto.contest.UpdateContestBoardDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ContestBoardServiceImpl implements ContestBoardService{

    private final ContestBoardRepository contestBoardRepository;
    private final MemberRepository memberRepository;
    private final MenuRepository menuRepository;

    @Override
    public Integer write(Integer menuId, SaveContestBoardDto dto) {
        Member writer = memberRepository.getById(dto.getLoginedUser());
        Menu menu = menuRepository.getById(menuId);
        ContestBoard contestBoard = ContestBoard.builder()
                .title(dto.getTitle())
                .contents(dto.getContents())
                .association(dto.getAssociation())
                .topic(dto.getTopic())
                .start(dto.getStart())
                .deadline(dto.getDeadline())
                .build()
                .writtenBy(writer)
                .inMenu(menu);
        return contestBoardRepository.save(contestBoard).getId();
    }

    @Override
    public Integer update(Integer menuId, UpdateContestBoardDto dto) {
        Member writer = memberRepository.getById(dto.getLoginedUser());
        Menu menu = menuRepository.getById(menuId);
        ContestBoard entity = ContestBoard.builder()
                .id(dto.getId())
                .title(dto.getTitle())
                .contents(dto.getContents())
                .association(dto.getAssociation())
                .topic(dto.getTopic())
                .start(dto.getStart())
                .deadline(dto.getDeadline())
                .build()
                .writtenBy(writer)
                .inMenu(menu);
        return contestBoardRepository.save(entity).getId();
    }


    @Override
    public void delete(Integer id) {
        contestBoardRepository.deleteById(id);
    }

    @Override
    public DetailContestBoardDto getBoard(Integer menuId, Integer id) {
        return  contestBoardRepository.findDtoById(menuId, id)
                .orElseThrow(()-> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
    }

    @Override
    public Page<ListContestBoardDto> getBoardList(Integer menuId, Pageable pageable) {
        return contestBoardRepository.findAllByMenuId(menuId, pageable);
    }
}
