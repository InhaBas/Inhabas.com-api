package com.inhabas.api.domain.lecture.usecase;

import com.inhabas.api.domain.lecture.dto.*;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LectureService {

    void create(LectureRegisterForm form, MemberId memberId);

    void update(LectureUpdateForm form, MemberId memberId);

    void delete(Integer lectureId, MemberId memberId);

    LectureDetailDto get(Integer lectureId);

    Page<LectureListDto> getList(Pageable pageable);

    void approveOrDeny(Integer lectureId, StatusUpdateRequest request);
}
