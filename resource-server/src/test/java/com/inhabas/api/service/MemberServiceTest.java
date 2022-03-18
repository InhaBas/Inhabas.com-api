package com.inhabas.api.service;

import com.inhabas.api.domain.member.IbasInformation;
import com.inhabas.api.domain.member.Member;
import com.inhabas.api.domain.member.MemberRepository;
import com.inhabas.api.domain.member.SchoolInformation;
import com.inhabas.api.domain.member.type.wrapper.Phone;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.dto.signUp.DetailSignUpDto;
import com.inhabas.api.dto.signUp.ProfessorSignUpDto;
import com.inhabas.api.dto.signUp.StudentSignUpDto;
import com.inhabas.api.service.member.DuplicatedMemberFieldException;
import com.inhabas.api.service.member.MemberNotExistException;
import com.inhabas.api.service.member.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

    @DisplayName("학생 회원가입을 성공한다.")
    @Test
    public void 학생_회원가입() {
        //given
        StudentSignUpDto signUpForm = StudentSignUpDto.builder()
                .name("유동현")
                .semester(2)
                .major("컴퓨터공학과")
                .phoneNumber("010-0000-1111")
                .memberId(12345678)
                .build();

        Member expected = Member.builder()
                .id(signUpForm.getMemberId())
                .phone(signUpForm.getPhoneNumber())
                .name(signUpForm.getName())
                .picture("")
                .schoolInformation(SchoolInformation.ofStudent(signUpForm.getMajor(), signUpForm.getSemester()))
                .ibasInformation(new IbasInformation(Role.NOT_APPROVED_MEMBER, "", 0))
                .build();
        ReflectionTestUtils.setField(expected.getIbasInformation(), "joined", LocalDateTime.now());
        given(memberRepository.save(any(Member.class))).willReturn(expected);

        //when
        Member newMember = memberService.saveSignUpForm(signUpForm);

        //then
        assertThat(newMember)
                .usingRecursiveComparison()
                .ignoringFields("joined")
                .isEqualTo(expected);
        assertThat(newMember.getIbasInformation().getJoined()).isNotNull();
    }

    @DisplayName("교수 회원가입을 성공한다.")
    @Test
    public void 교수_회원가입() {
        //given
        ProfessorSignUpDto signUpForm = ProfessorSignUpDto.builder()
                .name("유동현")
                .major("컴퓨터공학과")
                .phoneNumber("010-0000-1111")
                .memberId(12345678)
                .build();

        Member expected = Member.builder()
                .id(signUpForm.getMemberId())
                .phone(signUpForm.getPhoneNumber())
                .name(signUpForm.getName())
                .picture("")
                .schoolInformation(SchoolInformation.ofProfessor(signUpForm.getMajor()))
                .ibasInformation(new IbasInformation(Role.ANONYMOUS, "", 0))
                .build();
        ReflectionTestUtils.setField(expected.getIbasInformation(), "joined", LocalDateTime.now());
        given(memberRepository.save(any(Member.class))).willReturn(expected);

        //when
        Member newMember = memberService.saveSignUpForm(signUpForm);

        //then
        assertThat(newMember)
                .usingRecursiveComparison()
                .ignoringFields("joined")
                .isEqualTo(expected);
        assertThat(newMember.getIbasInformation().getJoined()).isNotNull();
    }

    @DisplayName("같은 학번 저장 시 DuplicatedMemberFiledException 예외")
    @Test
    public void 같은_학번_저장_예외() {
        //given
        Integer sameStudentId = MEMBER1.getId();
        given(memberRepository.existsById(anyInt())).willReturn(true);

        //when
        StudentSignUpDto signUpForm = StudentSignUpDto.builder()
                .name("유동현")
                .semester(2)
                .major("컴퓨터공학과")
                .phoneNumber("010-0000-1111")
                .memberId(sameStudentId)
                .build();

        //then
        assertThrows(DuplicatedMemberFieldException.class,
                () -> memberService.saveSignUpForm(signUpForm));
    }

    @DisplayName("같은 전화번호 저장 예외")
    @Test
    public void 같은_전화번호_저장_예외() {
        //given
        given(memberRepository.existsByPhone(any(Phone.class))).willReturn(true);

        //when
        StudentSignUpDto signUpForm = StudentSignUpDto.builder()
                .name("유동현")
                .semester(2)
                .major("컴퓨터공학과")
                .phoneNumber("010-0000-1111")
                .memberId(12345678)
                .build();

        //then
        assertThrows(DuplicatedMemberFieldException.class,
                () -> memberService.saveSignUpForm(signUpForm));
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
                .schoolInformation(SchoolInformation.ofStudent("전자공학과", 1))
                .build();
        given(memberRepository.findById(anyInt())).willReturn(Optional.ofNullable(savedMember));

        //when
        DetailSignUpDto savedForm = memberService.loadSignUpForm(studentId, email);

        //then
        then(memberRepository).should(times(1)).findById(anyInt());
        assertThat(savedForm)
                .usingRecursiveComparison()
                .isEqualTo(DetailSignUpDto.builder()
                        .memberId(studentId)
                        .email(email)
                        .semester(1)
                        .major("전자공학과")
                        .name("유동현")
                        .phoneNumber("010-0000-0000")
                        .build()
                );
    }

    @DisplayName("회원의 권한을 변경한다.")
    @Test
    public void changeRoleTest() {
        //given
        Integer memberId = 12171652;
        Member targetMember = Member.builder()
                .id(memberId)
                .picture("")
                .name("유동현")
                .phone("010-0000-0000")
                .schoolInformation(SchoolInformation.ofStudent("정보통신공학과", 1))
                .ibasInformation(new IbasInformation(Role.ANONYMOUS, "", 0))
                .build();
        given(memberRepository.findById(anyInt()))
                .willReturn(Optional.ofNullable(targetMember));

        assert targetMember != null;
        Member result = Member.builder()
                .id(targetMember.getId())
                .picture(targetMember.getPicture())
                .name(targetMember.getName())
                .phone(targetMember.getPhone())
                .schoolInformation(targetMember.getSchoolInformation())
                .ibasInformation(new IbasInformation(Role.NOT_APPROVED_MEMBER, "", 0))
                .build();
        given(memberRepository.save(any(Member.class)))
                .willReturn(result); // NOT care about this return-value of save() in Service logic

        //when
        memberService.changeRole(memberId, Role.NOT_APPROVED_MEMBER);

        //then
        assertThat(targetMember.getIbasInformation().getRole())
                .isEqualTo(Role.NOT_APPROVED_MEMBER);
    }

    @DisplayName("권한변경 시도 시에, 회원이 존재하지 않는 경우 MemberNotExistException 발생")
    @Test
    public void failToChangeRoleTest() {

        given(memberRepository.findById(anyInt()))
                .willReturn(Optional.empty());

        //when
        assertThrows(MemberNotExistException.class,
                () -> memberService.changeRole(12171652, Role.BASIC_MEMBER));
    }

    @DisplayName("중복되는 학번이 존재한다.")
    @Test
    public void 중복되는_Id가_있다() {

        //given
        given(memberRepository.existsById(anyInt())).willReturn(true);

        //when
        boolean result = memberService.isDuplicatedId(12171652);

        //then
        then(memberRepository).should(times(1)).existsById(anyInt());
        assertTrue(result);
    }

    @DisplayName("중복되는 학번이 없다")
    @Test
    public void 중복되는_Id가_없다() {

        //given
        given(memberRepository.existsById(anyInt())).willReturn(false);

        //when
        boolean result = memberService.isDuplicatedId(12171652);

        //then
        then(memberRepository).should(times(1)).existsById(anyInt());
        assertFalse(result);
    }

    @DisplayName("중복되는 핸드폰 번호가 있다.")
    @Test
    public void 중복되는_핸드폰_번호가_있다() {

        //given
        given(memberRepository.existsByPhone(any(Phone.class))).willReturn(true);

        //when
        boolean result = memberService.isDuplicatedPhoneNumber(new Phone("010-0000-0000"));

        //then
        then(memberRepository).should(times(1)).existsByPhone(any(Phone.class));
        assertTrue(result);
    }

    @DisplayName("중복되는 핸드폰 번호가 없다.")
    @Test
    public void 중복되는_핸드폰_번호가_없다() {

        //given
        given(memberRepository.existsByPhone(any(Phone.class))).willReturn(false);

        //when
        boolean result = memberService.isDuplicatedPhoneNumber(new Phone("010-0000-0000"));

        //then
        then(memberRepository).should(times(1)).existsByPhone(any(Phone.class));
        assertFalse(result);
    }
}
