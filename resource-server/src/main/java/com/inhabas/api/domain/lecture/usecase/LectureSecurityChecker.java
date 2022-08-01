package com.inhabas.api.domain.lecture.usecase;

import com.inhabas.api.domain.lecture.domain.Lecture;
import com.inhabas.api.domain.lecture.repository.LectureRepository;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class LectureSecurityChecker {

    private final LectureRepository lectureRepository;

    public boolean instructorOnly(Integer lectureId) {

        MemberId currentMemberId = (MemberId) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(EntityNotFoundException::new);

        return lecture.isHeldBy(currentMemberId);
    }
}
