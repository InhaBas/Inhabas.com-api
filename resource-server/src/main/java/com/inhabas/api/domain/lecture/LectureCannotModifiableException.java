package com.inhabas.api.domain.lecture;

import org.springframework.security.access.AccessDeniedException;

public class LectureCannotModifiableException extends AccessDeniedException {

    public LectureCannotModifiableException() {
        super("다른 담당자의 강의를 수정할 수 없습니다.");
    }

    public LectureCannotModifiableException(String msg) {
        super(msg);
    }
}
