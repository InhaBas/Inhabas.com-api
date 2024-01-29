package com.inhabas.api.domain.club.usecase;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.error.businessException.NotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.exception.MemberNotFoundException;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.domain.board.domain.valueObject.Content;
import com.inhabas.api.domain.board.domain.valueObject.Title;
import com.inhabas.api.domain.club.domain.ClubHistory;
import com.inhabas.api.domain.club.dto.ClubHistoryDto;
import com.inhabas.api.domain.club.dto.SaveClubHistoryDto;
import com.inhabas.api.domain.club.repository.ClubHistoryRepository;

@Service
@RequiredArgsConstructor
public class ClubHistoryServiceImpl implements ClubHistoryService {

  private final ClubHistoryRepository clubHistoryRepository;

  private final MemberRepository memberRepository;

  @Override
  @Transactional
  public Long writeClubHistory(Long memberId, SaveClubHistoryDto saveClubHistoryDto) {

    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
    ClubHistory clubHistory =
        ClubHistory.builder()
            .member(writer)
            .title(new Title(saveClubHistoryDto.getTitle()))
            .content(new Content(saveClubHistoryDto.getContent()))
            .dateHistory(saveClubHistoryDto.getDateHistory())
            .build();

    ClubHistory result = clubHistoryRepository.save(clubHistory);

    return result.getId();
  }

  @Override
  @Transactional(readOnly = true)
  public ClubHistoryDto findClubHistory(Long clubHistoryId) {

    ClubHistory clubHistory =
        clubHistoryRepository.findById(clubHistoryId).orElseThrow(NotFoundException::new);

    return new ClubHistoryDto(clubHistory);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ClubHistoryDto> getClubHistories() {

    Sort sort = Sort.by(Sort.Direction.DESC, "dateHistory");
    List<ClubHistory> clubHistoryList = clubHistoryRepository.findAll(sort);

    return clubHistoryList.stream().map(ClubHistoryDto::new).collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void updateClubHistory(
      Long memberId, Long clubHistoryId, SaveClubHistoryDto saveClubHistoryDto) {

    ClubHistory clubHistory =
        clubHistoryRepository.findById(clubHistoryId).orElseThrow(NotFoundException::new);
    Member writer = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);

    clubHistory.updateClubHistory(writer, saveClubHistoryDto);
  }

  @Override
  @Transactional
  public void deleteClubHistory(Long clubHistoryId) {

    ClubHistory clubHistory =
        clubHistoryRepository.findById(clubHistoryId).orElseThrow(NotFoundException::new);
    clubHistoryRepository.delete(clubHistory);
  }
}
