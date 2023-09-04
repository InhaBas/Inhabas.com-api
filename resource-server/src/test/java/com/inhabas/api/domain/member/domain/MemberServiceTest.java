package com.inhabas.api.domain.member.domain;

import static com.inhabas.api.domain.member.domain.valueObject.Role.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

import com.inhabas.api.domain.member.DuplicatedMemberFieldException;
import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.IbasInformation;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.domain.valueObject.SchoolInformation;
import com.inhabas.api.domain.member.repository.MemberRepository;
import com.inhabas.api.domain.member.dto.ContactDto;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    MemberServiceImpl memberService;

    @Mock
    MemberDuplicationChecker memberDuplicationChecker;

    @Mock
    MemberRepository memberRepository;


//    @DisplayName("교수 회원가입을 성공한다.")
//    @Test
//    public void 교수_회원가입() {
//        //given
//        ProfessorSignUpDto signUpForm = ProfessorSignUpDto.builder()
//                .name("유동현")
//                .major("컴퓨터공학과")
//                .phoneNumber("010-0000-1111")
//                .memberId(12345678)
//                .build();
//
//        Member expected = Member.builder()
//                .id(signUpForm.getMemberId())
//                .phoneNumber(signUpForm.getPhoneNumber())
//                .name(signUpForm.getName())
//                .picture("")
//                .schoolInformation(SchoolInformation.ofProfessor(signUpForm.getMajor()))
//                .ibasInformation(new IbasInformation(Role.ANONYMOUS, "", 0))
//                .build();
//        ReflectionTestUtils.setField(expected.getIbasInformation(), "joined", LocalDateTime.now());
//        given(memberRepository.save(any(Member.class))).willReturn(expected);
//
//        //when
//        Member newMember = memberService.saveSignUpForm(signUpForm);
//
//        //then
//        assertThat(newMember)
//                .usingRecursiveComparison()
//                .ignoringFields("joined")
//                .isEqualTo(expected);
//        assertThat(newMember.getIbasInformation().getJoined()).isNotNull();
//    }

    @DisplayName("같은 학번 저장 시 DuplicatedMemberFiledException 예외")
    @Test
    public void 같은_학번_저장_예외() {
        //given
        given(memberDuplicationChecker.isDuplicatedMember(any(Member.class))).willReturn(true);

        //when
        Member newMember = Member.builder()
                .id(new MemberId(12345678))
                .name("유동현")
                .phone("010-0000-0000")
                .email("my@gmail.com")
                .picture("")
                .ibasInformation(new IbasInformation(BASIC))
                .schoolInformation(SchoolInformation.ofUnderGraduate("전자공학과", 1, 1))
                .build();

        //then
        assertThrows(DuplicatedMemberFieldException.class,
                () -> memberService.save(newMember));
    }

    @DisplayName("같은 전화번호 저장 예외")
    @Test
    public void 같은_전화번호_저장_예외() {
        //given
        given(memberDuplicationChecker.isDuplicatedMember(any(Member.class))).willReturn(true);

        //when
        Member newMember = Member.builder()
                .id(new MemberId(12345678))
                .name("유동현")
                .phone("010-0000-0000")
                .email("my@gmail.com")
                .picture("")
                .ibasInformation(new IbasInformation(BASIC))
                .schoolInformation(SchoolInformation.ofUnderGraduate("전자공학과", 1, 1))
                .build();
        //then
        assertThrows(DuplicatedMemberFieldException.class,
                () -> memberService.save(newMember));
    }

    @DisplayName("회원의 권한을 변경한다.")
    @Test
    public void changeRoleTest() {
        //given
        MemberId memberId = new MemberId(12171652);
        Member targetMember = Member.builder()
                .id(memberId)
                .picture("")
                .name("유동현")
                .email("my@gmail.com")
                .phone("010-0000-0000")
                .schoolInformation(SchoolInformation.ofUnderGraduate("정보통신공학과", 1, 1))
                .ibasInformation(new IbasInformation(ANONYMOUS))
                .build();

        assert targetMember != null;
        Member result = Member.builder()
                .id(memberId)
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

    @Disabled
    @DisplayName("권한변경 시도 시에, 회원이 존재하지 않는 경우 MemberNotExistException 발생")
    @Test
    public void failToChangeRoleTest() {

        given(memberRepository.findById(any()))
                .willReturn(Optional.empty());

        //when
//        assertThrows(MemberNotFoundException.class,
//                () -> memberService.changeRole(new MemberId(12171652), Role.BASIC));
    }

    @DisplayName("회장 연락처 불러오기")
    @Test
    public void getChiefContact() {
        Member chief = Member.builder()
                .id(new MemberId(12171652))
                .picture("")
                .name("유동현")
                .email("my@gmail.com")
                .phone("010-0000-0000")
                .schoolInformation(SchoolInformation.ofUnderGraduate("정보통신공학과", 1, 1))
                .ibasInformation(new IbasInformation(CHIEF))
                .build();
        given(memberRepository.searchByRoleLimit(any(), anyInt())).willReturn(List.of(chief));

        //when
        ContactDto chiefContact = memberService.getChiefContact();

        //then
        assertThat(chiefContact.getEmail()).isEqualTo(chief.getEmail());
        assertThat(chiefContact.getPhoneNumber()).isEqualTo(chief.getPhone());
        assertThat(chiefContact.getName()).isEqualTo(chief.getName());
    }
}
