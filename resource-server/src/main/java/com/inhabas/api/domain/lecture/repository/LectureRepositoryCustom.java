package com.inhabas.api.domain.lecture.repository;

import com.inhabas.api.domain.lecture.dto.LectureDetailDto;
import com.inhabas.api.domain.lecture.dto.LectureListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LectureRepositoryCustom {

    Optional<LectureDetailDto> getDetails(Integer id);

    Page<LectureListDto> getList(Pageable pageable);
}
