package com.inhabas.api.domain.club.usecase;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.club.domain.entity.ClubHistory;
import com.inhabas.api.domain.club.dto.ClubHistoryDto;
import com.inhabas.api.domain.club.dto.SaveClubHistoryDto;
import com.inhabas.api.domain.club.repository.ClubHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubHistoryServiceImpl implements ClubHistoryService {

    private final ClubHistoryRepository clubHistoryRepository;

    private final MemberRepository memberRepository;


    @Override
    @Transactional
    public Long writeClubHistory(Long memberId, SaveClubHistoryDto saveClubHistoryDto) {

        Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        ClubHistory clubHistory = ClubHistory.builder()
                .member(writer)
                .title(saveClubHistoryDto.getTitle())
                .content(saveClubHistoryDto.getContent())
                .dateHistory(saveClubHistoryDto.getDateHistory())
                .build();

        ClubHistory result = clubHistoryRepository.save(clubHistory);

        return result.getId();

    }

    @Override
    @Transactional(readOnly = true)
    public ClubHistoryDto findClubHistory(Long clubHistoryId) {

        ClubHistory clubHistory = clubHistoryRepository.findById(clubHistoryId).orElseThrow(NotFoundException::new);

        return new ClubHistoryDto(clubHistory);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ClubHistoryDto> getClubHistories() {

        List<ClubHistory> clubHistoryList = clubHistoryRepository.findAll();

        return clubHistoryList.stream()
                .map(ClubHistoryDto::new)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public void updateClubHistory(Long memberId, Long clubHistoryId, SaveClubHistoryDto saveClubHistoryDto) {

        ClubHistory clubHistory = clubHistoryRepository.findById(clubHistoryId).orElseThrow(NotFoundException::new);
        Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

        clubHistory.updateClubHistory(writer, saveClubHistoryDto);

    }

    @Override
    @Transactional
    public void deleteClubHistories(Long clubHistoryId) {

        ClubHistory clubHistory = clubHistoryRepository.findById(clubHistoryId).orElseThrow(NotFoundException::new);
        clubHistoryRepository.delete(clubHistory);

    }
}
