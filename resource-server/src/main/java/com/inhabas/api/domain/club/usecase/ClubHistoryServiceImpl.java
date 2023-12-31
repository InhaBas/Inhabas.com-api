package com.inhabas.api.domain.club.usecase;

import com.inhabas.api.domain.club.dto.ClubHistoryDto;
import com.inhabas.api.domain.club.dto.SaveClubHistoryDto;
import com.inhabas.api.domain.club.repository.ClubHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubHistoryServiceImpl implements ClubHistoryService {

    private final ClubHistoryRepository clubHistoryRepository;


    @Override
    public Long writeClubHistory(SaveClubHistoryDto saveClubHistoryDto) {
        return null;
    }

    @Override
    public ClubHistoryDto findClubHistory(Long clubHistoryId) {
        return null;
    }

    @Override
    public List<ClubHistoryDto> getClubHistories() {
        return null;
    }

    @Override
    public void updateClubHistory(Long clubHistoryId, SaveClubHistoryDto saveClubHistoryDto) {

    }

    @Override
    public void deleteClubHistories(Long clubHistoryId) {

    }
}
