package com.inhabas.api.domain.club.usecase;

import java.util.List;

import com.inhabas.api.domain.club.dto.ClubActivityDetailDto;
import com.inhabas.api.domain.club.dto.ClubActivityDto;
import com.inhabas.api.domain.club.dto.SaveClubActivityDto;

public interface ClubActivityService {

  List<ClubActivityDto> getClubActivities(String search);

  Long writeClubActivity(Long memberId, SaveClubActivityDto saveClubActivityDto);

  ClubActivityDetailDto getClubActivity(Long boardId);

  void updateClubActivity(Long boardId, SaveClubActivityDto saveClubActivityDto, Long memberId);

  void deleteClubActivity(Long boardId);
}
