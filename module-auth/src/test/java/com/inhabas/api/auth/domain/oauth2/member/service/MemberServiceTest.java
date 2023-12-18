package com.inhabas.api.auth.domain.oauth2.member.service;


import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.IbasInformation;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.SchoolInformation;
import com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.StudentId;
import com.inhabas.api.auth.domain.oauth2.member.dto.ContactDto;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    MemberServiceImpl memberService;
    @Mock
    MemberRepository memberRepository;


    @DisplayName("회원의 권한을 변경한다.")
    @Test
    public void changeRoleTest() {
        //given
        StudentId studentId = new StudentId("12171652");
        Member targetMember = Member.builder()
                .studentId(studentId)
                .picture("")
                .name("유동현")
                .email("my@gmail.com")
                .phone("010-0000-0000")
                .schoolInformation(SchoolInformation.ofUnderGraduate("정보통신공학과", 1))
                .ibasInformation(new IbasInformation(ANONYMOUS))
                .build();

        assert targetMember != null;
        Member result = Member.builder()
                .studentId(studentId)
                .picture(targetMember.getPicture())
                .name(targetMember.getName())
                .email("my@gmail.com")
                .phone(targetMember.getPhone())
                .schoolInformation(targetMember.getSchoolInformation())
                .ibasInformation(new IbasInformation(NOT_APPROVED))
                .build();
        given(memberRepository.save(any(Member.class)))
                .willReturn(result); // NOT care about this return-value of save() in Service logic

        //when
        memberService.changeRole(targetMember, NOT_APPROVED);

        //then
        assertThat(targetMember.getIbasInformation().getRole())
                .isEqualTo(NOT_APPROVED);
    }

    @DisplayName("회장 연락처 불러오기")
    @Test
    public void getChiefContact() {
        Member chief = Member.builder()
                .studentId(new StudentId("12171652"))
                .picture("")
                .name("유동현")
                .email("my@gmail.com")
                .phone("010-0000-0000")
                .schoolInformation(SchoolInformation.ofUnderGraduate("정보통신공학과", 1))
                .ibasInformation(new IbasInformation(CHIEF))
                .build();
        given(memberRepository.findByIbasInformation_Role(any())).willReturn(chief);

        //when
        ContactDto chiefContact = memberService.getChiefContact();

        //then
        assertThat(chiefContact.getEmail()).isEqualTo(chief.getEmail());
        assertThat(chiefContact.getPhoneNumber()).isEqualTo(chief.getPhone());
        assertThat(chiefContact.getName()).isEqualTo(chief.getName());
    }
}
