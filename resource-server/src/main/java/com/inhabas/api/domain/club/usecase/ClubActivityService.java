package com.inhabas.api.domain.club.usecase;

import com.inhabas.api.domain.club.dto.ClubActivityDetailDto;
import com.inhabas.api.domain.club.dto.ClubActivityDto;
import com.inhabas.api.domain.club.dto.SaveClubActivityDto;
import java.util.List;

public interface ClubActivityService {

  List<ClubActivityDto> getClubActivities();

  Long writeClubActivity(Long memberId, SaveClubActivityDto saveClubActivityDto);

  ClubActivityDetailDto getClubActivity(Long boardId);

  void updateClubActivity(Long boardId, SaveClubActivityDto saveClubActivityDto);

  void deleteClubActivity(Long boardId);
}
