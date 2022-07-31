package com.inhabas.api.domain.lecture.domain.valueObject;

public enum LectureStatus {
    WAITING("승인 대기", 1),
    DENIED("승인 거절", 2),
    PROGRESSING("진행 중", 3),
    TERMINATED("종료", 4),
    ;

    private final Integer value;

    LectureStatus(String describe, Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
