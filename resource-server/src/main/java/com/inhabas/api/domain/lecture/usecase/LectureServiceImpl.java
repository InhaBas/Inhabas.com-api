package com.inhabas.api.domain.lecture.usecase;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.lecture.LectureCannotModifiableException;
import com.inhabas.api.domain.lecture.domain.Lecture;
import com.inhabas.api.domain.lecture.dto.*;
import com.inhabas.api.domain.lecture.repository.LectureRepository;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

  private final LectureRepository repository;

  @Transactional
  @Override
  public void create(LectureRegisterForm form, StudentId studentId) {

    Lecture lecture = form.toEntity(studentId);
    repository.save(lecture);
  }

  @Transactional
  @Override
  public void update(LectureUpdateForm form, StudentId studentId) {

    Lecture lecture = repository.findById(form.getId()).orElseThrow(EntityNotFoundException::new);

    lecture.update(
        studentId,
        form.getTitle(),
        form.getApplyDeadLine(),
        form.getDaysOfWeeks(),
        form.getPlace(),
        form.getIntroduction(),
        form.getCurriculumDetails(),
        form.getParticipantsLimits(),
        form.getMethod());
  }

  @Transactional
  @Override
  public void delete(Integer lectureId, StudentId studentId) {

    Lecture lecture = repository.findById(lectureId).orElseThrow(EntityNotFoundException::new);

    if (lecture.notModifiableBy(studentId)) throw new LectureCannotModifiableException();

    if (!lecture.canBeDeleted())
      throw new LectureCannotModifiableException("이미 진행된 강의는 삭제할 수 없습니다.");

    repository.delete(lecture);
  }

  @Transactional(readOnly = true)
  @Override
  public LectureDetailDto get(Integer lectureId) {

    return repository.getDetails(lectureId).orElseThrow(EntityNotFoundException::new);
  }

  @Transactional(readOnly = true)
  @Override
  public Page<LectureListDto> getList(Pageable pageable) {

    return repository.getList(pageable);
  }

  @Override
  public void approveOrDeny(Integer lectureId, LectureStatusUpdateRequest request) {

    Lecture lecture = repository.findById(lectureId).orElseThrow(EntityNotFoundException::new);

    lecture.approveOrDeny(request.getStatus(), request.getRejectReason());
  }
}
