package com.inhabas.api.domain.scholarship.usecase;

import java.util.List;

import com.inhabas.api.domain.scholarship.domain.ScholarshipBoardType;
import com.inhabas.api.domain.scholarship.dto.SaveScholarshipBoardDto;
import com.inhabas.api.domain.scholarship.dto.ScholarshipBoardDetailDto;
import com.inhabas.api.domain.scholarship.dto.ScholarshipBoardDto;

public interface ScholarshipBoardService {

  List<ScholarshipBoardDto> getPosts(ScholarshipBoardType boardType, String search);

  ScholarshipBoardDetailDto getPost(ScholarshipBoardType boardType, Long boardId, Long memberId);

  Long write(
      ScholarshipBoardType boardType,
      SaveScholarshipBoardDto saveScholarshipBoardDto,
      Long memberId);

  void update(
      Long boardId,
      ScholarshipBoardType boardType,
      SaveScholarshipBoardDto saveScholarshipBoardDto,
      Long memberId);

  void delete(ScholarshipBoardType boardType, Long boardId);
}
