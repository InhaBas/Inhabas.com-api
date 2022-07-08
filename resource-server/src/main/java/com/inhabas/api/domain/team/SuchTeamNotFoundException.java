package com.inhabas.api.domain.team;

public class SuchTeamNotFoundException extends RuntimeException {

    public SuchTeamNotFoundException() {
        super("해당 팀을 찾을 수 없습니다.");
    }
}
