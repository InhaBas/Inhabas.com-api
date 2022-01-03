package com.inhabas.api.domain;

import com.inhabas.api.domain.member.*;
import com.inhabas.api.domain.member.type.wrapper.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MemberTest {
    public static final Member MEMBER1 = new Member(
            12171234, "유동현", "010-1111-1111", ""
            , new SchoolInformation(Major.건축공학과, 3, 1)
            , new IbasInformation(Role.normalMember, "hello", 0));

    public static final Member MEMBER2 = new Member(
            12114321, "김민겸", "010-2222-2222", ""
            , new SchoolInformation(Major.경영학과, 2, 2)
            , new IbasInformation(Role.normalMember, "hi", 0));


    @DisplayName("Semester 타입에 학기를 지정")
    @Test
    public void Semester_is_OK() {
        //given
        Semester firstSemester = new Semester(1);
        Semester secondSemester = new Semester(2);

        //then
        assertThat(firstSemester.getValue()).isEqualTo(1);
        assertThat(secondSemester.getValue()).isEqualTo(2);
    }

    @DisplayName("존재하지 않는 학기를 지정")
    @Test
    public void No_Such_Semester() {
        //when
        assertThrows(
                IllegalArgumentException.class,
                () -> {Semester thirdSemester = new Semester(3);}
        );
        assertThrows(
                IllegalArgumentException.class,
                () -> {Semester thirdSemester = new Semester(0);}
        );
    }

    @DisplayName("Phone 타입에 핸드폰 번호를 저장")
    @Test
    public void Phone_is_OK() {
        //given
        String number = "010-1111-1111";

        //when
        Phone phoneNumber = new Phone(number);

        //then
        assertThat(phoneNumber.getValue()).isEqualTo("010-1111-1111");
    }

    @DisplayName("잘못된 핸드폰 번호 저장 시도")
    @Test
    public void PhoneNumber_is_Wrong() {
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
                number -> assertThrows(IllegalArgumentException.class, ()-> new Phone(number))
        );
    }

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

    @DisplayName("자기소개란 작성")
    @Test
    public void Introduce_is_OK() {
        //given
        String introduceString = "아이엠 그라운드 자기소개 하기.";

        //when
        Introduce introduce = new Introduce(introduceString);

        //then
        assertThat(introduce.getValue()).isEqualTo("아이엠 그라운드 자기소개 하기.");
    }

    @DisplayName("자기소개가 너무 길다. 300자 이상.")
    @Test
    public void Introduce_is_too_long() {
        //given
        String introduceString = "지금이문장은10자임".repeat(30); // 300자

        //then
        assertThrows(
                IllegalArgumentException.class,
                ()-> new Introduce(introduceString)
        );
    }

}
