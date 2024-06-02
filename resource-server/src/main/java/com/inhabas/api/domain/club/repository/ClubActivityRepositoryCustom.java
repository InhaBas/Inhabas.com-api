package com.inhabas.api.domain.club.repository;

import java.util.List;

import com.inhabas.api.domain.club.dto.ClubActivityDto;

public interface ClubActivityRepositoryCustom {

  List<ClubActivityDto> findAllAndSearch(String search);
}
