package com.inhabas.api.domain.scholarship.repository;

import java.util.List;
import java.util.Optional;

import com.inhabas.api.domain.scholarship.domain.Scholarship;
import com.inhabas.api.domain.scholarship.domain.ScholarshipBoardType;
import com.inhabas.api.domain.scholarship.dto.ScholarshipBoardDto;

public interface ScholarshipBoardRepositoryCustom {

  List<ScholarshipBoardDto> findAllByTypeAndSearch(ScholarshipBoardType boardType, String search);

  Optional<Scholarship> findByTypeAndId(ScholarshipBoardType boardType, Long boardId);
}
