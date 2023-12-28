package com.inhabas.api.auth.domain.oauth2.member.domain.valueObject;

import com.inhabas.api.auth.domain.error.businessException.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PhoneTest {

    @DisplayName("Phone 타입에 핸드폰 번호를 저장")
    @Test
    public void Phone_is_OK() {
        //given
        String number = "010-1111-1111";

        //when
        Phone phone = new Phone(number);

        //then
        assertThat(phone.getValue()).isEqualTo("010-1111-1111");
    }

    @DisplayName("잘못된 핸드폰 번호 저장 시도")
    @Test
    public void Phone_is_Wrong() {
        //given
        String[] wrongNumbers = {
                "010-111-1111",
                "011-1111-1111",
                "02-503-1234",
                "010)111-1111",
                "01023452345"
        };

        //then
        Arrays.stream(wrongNumbers).forEach(
                number -> assertThrows(InvalidInputException.class, ()-> new Phone(number))
        );
    }

    @DisplayName("핸드폰 번호에 null 이 저장될 수 없다.")
    @Test
    public void Phone_cannot_be_Null() {
        assertThrows(InvalidInputException.class,
                () -> new Phone(null));
    }

    @DisplayName("핸드폰 번호에 빈 문자열이 저장될 수 없다.")
    @Test
    public void Phone_cannot_be_Blank() {
        assertThrows(InvalidInputException.class,
                () -> new Phone("  "));
    }

}
