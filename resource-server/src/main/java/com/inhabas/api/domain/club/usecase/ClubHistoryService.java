package com.inhabas.api.domain.club.usecase;

import java.util.List;

import com.inhabas.api.domain.club.dto.ClubHistoryDto;
import com.inhabas.api.domain.club.dto.SaveClubHistoryDto;

public interface ClubHistoryService {

  Long writeClubHistory(Long memberId, SaveClubHistoryDto saveClubHistoryDto);

  ClubHistoryDto findClubHistory(Long clubHistoryId);

  List<ClubHistoryDto> getClubHistories();

  void updateClubHistory(Long memberId, Long clubHistoryId, SaveClubHistoryDto saveClubHistoryDto);

  void deleteClubHistory(Long clubHistoryId);
}
