package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

public enum RequestStatus {
    PENDING("승인대기"),
    APPROVED("승인완료"),
    REJECTED("승인거절"),
    COMPLETED("처리완료");

    private final String value;

    RequestStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
