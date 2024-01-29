package com.inhabas.api.domain.club.usecase;

import com.inhabas.api.domain.club.dto.ClubHistoryDto;
import com.inhabas.api.domain.club.dto.SaveClubHistoryDto;
import java.util.List;

public interface ClubHistoryService {

  Long writeClubHistory(Long memberId, SaveClubHistoryDto saveClubHistoryDto);

  ClubHistoryDto findClubHistory(Long clubHistoryId);

  List<ClubHistoryDto> getClubHistories();

  void updateClubHistory(Long memberId, Long clubHistoryId, SaveClubHistoryDto saveClubHistoryDto);

  void deleteClubHistory(Long clubHistoryId);
}
