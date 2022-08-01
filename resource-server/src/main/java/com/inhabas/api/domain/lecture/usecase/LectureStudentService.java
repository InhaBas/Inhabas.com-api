package com.inhabas.api.domain.lecture.usecase;

import com.inhabas.api.domain.lecture.domain.valueObject.StudentStatus;
import com.inhabas.api.domain.lecture.dto.StudentListDto;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface LectureStudentService {

    void enroll(Integer lectureId, MemberId memberId);

    /**
     * 학생 한명의 상태를 {@code PROGRESS} 또는 {@code BLOCKED} 상태로 변경한다. 탈주시킬 수 없다.
     * @param studentId 학번이 아닌 강의등록명단에서의 id
     */
    void changeStatusOfOneStudentByLecturer(Integer studentId, MemberId lecturerId, StudentStatus status);

    void changeStatusOfStudentsByLecturer(Map<Integer, StudentStatus> list, MemberId lecturerId);

    void exitBySelf(Integer lectureId, MemberId studentId);

    Page<StudentListDto> searchStudents(Integer lectureId, Pageable pageable);
}
