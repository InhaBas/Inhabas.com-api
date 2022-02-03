package com.inhabas.api.service.contest;

import com.inhabas.api.domain.contest.ContestBoard;
import com.inhabas.api.domain.contest.ContestBoardRepository;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.MemberRepository;
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

    @Override
    public Integer write(SaveContestBoardDto dto) {
        Member writer = memberRepository.getById(dto.getLoginedUser());
        ContestBoard contestBoard = new ContestBoard(dto.getTitle(), dto.getContents(), dto.getAssociation( ), dto.getTopic(), dto.getStart(), dto.getDeadline());

        contestBoard.writtenBy(writer);
        return contestBoardRepository.save(contestBoard).getId();
    }

    @Override
    public Integer update(UpdateContestBoardDto dto) {
        ContestBoard entity = contestBoardRepository.findById(dto.getId()).orElseThrow(()-> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        entity.setTitle(dto.getTitle());
        entity.setContents(dto.getContents());
        entity.setAssociation(dto.getAssociation());
        entity.setTopic(dto.getTopic());
        entity.setStart(dto.getStart());
        entity.setDeadline(dto.getDeadline());
        return contestBoardRepository.save(entity).getId();
    }


    @Override
    public void delete(Integer id) {
        contestBoardRepository.deleteById(id);
    }

    @Override
    public DetailContestBoardDto getBoard(Integer id) {
        return  contestBoardRepository.findDtoById(id).orElseThrow(()-> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
    }

    @Override
    public Page<ListContestBoardDto> getBoardList(Integer menuId, Pageable pageable) {
        return contestBoardRepository.findAllByMenuId(menuId, pageable);
    }
}
