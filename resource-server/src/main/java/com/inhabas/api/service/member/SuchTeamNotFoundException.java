package com.inhabas.api.service.member;

public class SuchTeamNotFoundException extends RuntimeException {

    public SuchTeamNotFoundException() {
        super("해당 팀을 찾을 수 없습니다.");
    }
}
