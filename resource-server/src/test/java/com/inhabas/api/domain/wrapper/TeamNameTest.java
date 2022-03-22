package com.inhabas.api.domain.wrapper;

import com.inhabas.api.domain.member.type.wrapper.TeamName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TeamNameTest {

    @DisplayName("TeamName 타입에 제목을 저장한다.")
    @Test
    public void TeamName_is_OK() {
        //given
        String  TeamNameString = "게시판 제목입니다.";

        //when
        TeamName title = new TeamName(TeamNameString);

        //then
        assertThat(title.getValue()).isEqualTo("게시판 제목입니다.");
    }

    @DisplayName("TeamName 타입에 너무 긴 제목을 저장한다. 100자 이상")
    @Test
    public void TeamName_is_too_long() {
        //given
        String TeamNameString = "지금이문장은10자임".repeat(2);

        //then
        assertThrows(IllegalArgumentException.class,
                () -> new TeamName(TeamNameString));
    }

    @DisplayName("TeamName은 null 일 수 없습니다.")
    @Test
    public void TeamName_cannot_be_Null() {
        assertThrows(IllegalArgumentException.class,
                () -> new TeamName(null));
    }

    @DisplayName("TeamName은 빈 문자열일 수 없습니다.")
    @Test
    public void TeamName_cannot_be_Blank() {
        assertThrows(IllegalArgumentException.class,
                () -> new TeamName("\t"));
    }
}
