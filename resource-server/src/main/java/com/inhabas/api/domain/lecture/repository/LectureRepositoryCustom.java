package com.inhabas.api.domain.lecture.repository;

import com.inhabas.api.domain.lecture.dto.LectureDetailDto;
import com.inhabas.api.domain.lecture.dto.LectureListDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LectureRepositoryCustom {

  Optional<LectureDetailDto> getDetails(Integer id);

  Page<LectureListDto> getList(Pageable pageable);
}
