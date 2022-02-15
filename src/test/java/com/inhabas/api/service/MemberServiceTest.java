package com.inhabas.api.service;

import com.inhabas.api.domain.member.IbasInformation;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.MemberRepository;
import com.inhabas.api.domain.member.SchoolInformation;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.dto.signUp.DetailSignUpDto;
import com.inhabas.api.dto.signUp.StudentSignUpDto;
import com.inhabas.api.security.domain.AuthUser;
import com.inhabas.api.service.member.DuplicatedMemberFieldException;
import com.inhabas.api.service.member.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.inhabas.api.domain.MemberTest.MEMBER1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    MemberServiceImpl memberService;

    @Mock
    MemberRepository memberRepository;

    @DisplayName("회원가입을 성공한다.")
    @Test
    public void 회원가입() {
        //given
        StudentSignUpDto signUpForm = StudentSignUpDto.builder()
                .name("유동현")
                .grade(3)
                .semester(2)
                .major("컴퓨터공학과")
                .phoneNumber("010-0000-1111")
                .studentId(12345678)
                .isProfessor(false)
                .build();

        AuthUser currentSignUpUser = new AuthUser("google", "my@email.com");
        ReflectionTestUtils.setField(currentSignUpUser, "id", 1);

        Member expected = Member.builder()
                .id(signUpForm.getStudentId())
                .phone(signUpForm.getPhoneNumber())
                .name(signUpForm.getName())
                .picture("")
                .schoolInformation(new SchoolInformation(signUpForm.getMajor(), signUpForm.getGrade(), signUpForm.getSemester()))
                .ibasInformation(new IbasInformation(Role.NOT_APPROVED_MEMBER, "", 0))
                .build();
        ReflectionTestUtils.setField(expected.getIbasInformation(), "joined", LocalDateTime.now());
        given(memberRepository.save(any(Member.class))).willReturn(expected);

        //when
        Member newMember = memberService.signUp(currentSignUpUser, signUpForm);

        //then
        assertThat(newMember)
                .usingRecursiveComparison()
                .ignoringFields("joined")
                .isEqualTo(expected);
        System.out.println(newMember.getIbasInformation());
        assertThat(newMember.getIbasInformation().getJoined()).isNotNull();
    }

    @DisplayName("같은 학번 저장 시 DuplicatedMemberFiledException 예외")
    @Test
    public void 같은_학번_저장_예외() {
        //given
        Integer sameStudentId = MEMBER1.getId();
        given(memberRepository.findById(anyInt())).willReturn(Optional.of(MEMBER1));

        AuthUser currentSignUpUser = new AuthUser("google", "my@email.com");
        ReflectionTestUtils.setField(currentSignUpUser, "id", 1);

        //when
        StudentSignUpDto signUpForm = StudentSignUpDto.builder()
                .name("유동현")
                .grade(3)
                .semester(2)
                .major("컴퓨터공학과")
                .phoneNumber("010-0000-1111")
                .studentId(sameStudentId)
                .isProfessor(false)
                .build();

        //then
        assertThrows(DuplicatedMemberFieldException.class,
                () -> memberService.signUp(currentSignUpUser, signUpForm));
    }

    @DisplayName("같은 전화번호 저장 예외")
    @Test
    public void 같은_전화번호_저장_예외() {
        //given
        AuthUser currentSignUpUser = new AuthUser("google", "my@email.com");
        ReflectionTestUtils.setField(currentSignUpUser, "id", 1);

        given(memberRepository.save(any(Member.class)))
                .willThrow(DataIntegrityViolationException.class);

        //when
        StudentSignUpDto signUpForm = StudentSignUpDto.builder()
                .name("유동현")
                .grade(3)
                .semester(2)
                .major("컴퓨터공학과")
                .phoneNumber("010-0000-1111")
                .studentId(12345678)
                .isProfessor(false)
                .build();

        //then
        assertThrows(DuplicatedMemberFieldException.class,
                () -> memberService.signUp(currentSignUpUser, signUpForm));
    }

    @DisplayName("임시저장한_개인정보를_불러온다")
    @Test
    public void 임시저장한_개인정보를_불러온다() {
        //given
        Integer studentId = 12171652;
        String email = "google@gmail.com";
        Member savedMember = Member.builder()
                .id(studentId)
                .name("유동현")
                .phone("010-0000-0000")
                .picture("")
                .ibasInformation(new IbasInformation(Role.BASIC_MEMBER, "", 0))
                .schoolInformation(new SchoolInformation("전자공학과", 3, 1))
                .build();
        given(memberService.findById(anyInt())).willReturn(Optional.ofNullable(savedMember));

        //when
        DetailSignUpDto savedForm = memberService.loadSignUpForm(studentId, email);

        //then
        then(memberRepository).should(times(1)).findById(anyInt());
        assertThat(savedForm)
                .usingRecursiveComparison()
                .isEqualTo(DetailSignUpDto.builder()
                        .memberId(studentId)
                        .email(email)
                        .grade(3)
                        .semester(1)
                        .isProfessor(false)
                        .major("전자공학과")
                        .name("유동현")
                        .phoneNumber("010-0000-0000")
                        .build()
                );
    }

//    @DisplayName("개인정보 수정 성공한다.")
//    @Test
//    public void 회원_정보_수정() {
//        //given
//        Member save = memberService.signUp(MEMBER1)
//                .orElseThrow(EntityNotFoundException::new);
//        String originalPhoneNumber = save.getPhone();
//
//        //when - 전화번호 수정
//        Member param = new Member(
//                save.getId(), save.getName(), "010-2222-2222", save.getPicture(),
//                save.getSchoolInformation(), save.getIbasInformation());
//        Member updateMember = memberService.updateMember(param)
//                .orElseThrow(EntityNotFoundException::new);
//
//        //then
//        String updatedPhoneNumber = updateMember.getPhone();
//
//        assertThat(updatedPhoneNumber).isEqualTo("010-2222-2222");
//        assertThat(updatedPhoneNumber).isNotEqualTo(originalPhoneNumber);
//    }
}
