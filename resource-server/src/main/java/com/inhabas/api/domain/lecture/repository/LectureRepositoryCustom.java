package com.inhabas.api.domain.lecture.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inhabas.api.domain.lecture.dto.LectureDetailDto;
import com.inhabas.api.domain.lecture.dto.LectureListDto;

public interface LectureRepositoryCustom {

  Optional<LectureDetailDto> getDetails(Integer id);

  Page<LectureListDto> getList(Pageable pageable);
}
