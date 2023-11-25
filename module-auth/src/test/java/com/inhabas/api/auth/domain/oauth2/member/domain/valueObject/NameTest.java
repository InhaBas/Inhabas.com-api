package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class NameTest {

    @DisplayName("Name 타입에 유저 이름 저장")
    @Test
    public void UserName_is_OK() {
        //given
        String username = "홍길동";

        //when
        Name name = new Name(username);

        //then
        assertThat(name.getValue()).isEqualTo("홍길동");
    }

    @DisplayName("Name 타입에 너무 긴 유저 이름 저장 시도. 50자 이상")
    @Test
    public void UserName_is_too_long() {
        //given
        String username = "홍길동".repeat(17); // 51자

        //when
        assertThrows(
                IllegalArgumentException.class,
                ()-> new Name(username)
        );
    }

    @DisplayName("이름은 null 일 수 없다.")
    @Test
    public void Username_cannot_be_null() {
        assertThrows(IllegalArgumentException.class,
                ()-> new Name(null));
    }

    @DisplayName("이름은 빈 문자열일 수 없다.")
    @Test
    public void Username_cannot_be_blank() {
        assertThrows(IllegalArgumentException.class,
                ()-> new Name(""));
    }
}
