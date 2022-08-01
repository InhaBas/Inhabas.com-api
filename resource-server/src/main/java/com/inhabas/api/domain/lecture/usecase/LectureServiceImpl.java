package com.inhabas.api.domain.lecture.usecase;

import com.inhabas.api.domain.lecture.LectureCannotModifiableException;
import com.inhabas.api.domain.lecture.domain.Lecture;
import com.inhabas.api.domain.lecture.dto.*;
import com.inhabas.api.domain.lecture.repository.LectureRepository;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class LectureServiceImpl implements LectureService {

    private final LectureRepository repository;

    @Transactional
    @Override
    public void create(LectureRegisterForm form, MemberId memberId) {

        Lecture lecture = form.toEntity(memberId);
        repository.save(lecture);
    }

    @Transactional
    @Override
    public void update(LectureUpdateForm form, MemberId memberId) {

        Lecture lecture = repository.findById(form.getId())
                .orElseThrow(EntityNotFoundException::new);

        lecture.update(memberId, form.getTitle(), form.getApplyDeadLine(), form.getDaysOfWeeks(), form.getPlace(),
                form.getIntroduction(), form.getCurriculumDetails(), form.getParticipantsLimits(), form.getMethod());
    }

    @Transactional
    @Override
    public void delete(Integer lectureId, MemberId memberId) {

        Lecture lecture = repository.findById(lectureId)
                .orElseThrow(EntityNotFoundException::new);

        if (lecture.notModifiableBy(memberId))
            throw new LectureCannotModifiableException();

        repository.delete(lecture);
    }

    @Transactional(readOnly = true)
    @Override
    public LectureDetailDto get(Integer lectureId) {

        return repository.getDetails(lectureId)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<LectureListDto> getList(Pageable pageable) {

        return repository.getList(pageable);
    }

    @Override
    public void approveOrDeny(Integer lectureId, StatusUpdateRequest request) {

        Lecture lecture = repository.findById(lectureId)
                .orElseThrow(EntityNotFoundException::new);

        lecture.approveOrDeny(request.getStatus(), request.getRejectReason());
    }
}
