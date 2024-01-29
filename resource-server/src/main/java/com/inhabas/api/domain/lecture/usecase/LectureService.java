package com.inhabas.api.domain.lecture.usecase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.lecture.dto.*;

public interface LectureService {

  void create(LectureRegisterForm form, StudentId studentId);

  void update(LectureUpdateForm form, StudentId studentId);

  void delete(Integer lectureId, StudentId studentId);

  LectureDetailDto get(Integer lectureId);

  Page<LectureListDto> getList(Pageable pageable);

  void approveOrDeny(Integer lectureId, LectureStatusUpdateRequest request);
}
