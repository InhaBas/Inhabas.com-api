package com.inhabas.api.domain.lecture.usecase;

import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.domain.lecture.domain.Lecture;
import com.inhabas.api.domain.lecture.repository.LectureRepository;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LectureSecurityChecker {

  private final LectureRepository lectureRepository;

  public boolean instructorOnly(Integer lectureId) {

    StudentId currentStudentId =
        (StudentId) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    Lecture lecture =
        lectureRepository.findById(lectureId).orElseThrow(EntityNotFoundException::new);

    return lecture.isHeldBy(currentStudentId);
  }
}
