package com.inhabas.api.domain.wrapper;

import com.inhabas.api.domain.member.type.wrapper.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UsernameTest {

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
}
