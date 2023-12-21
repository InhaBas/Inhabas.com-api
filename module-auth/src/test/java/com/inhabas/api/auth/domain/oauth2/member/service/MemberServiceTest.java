package com.inhabas.api.auth.domain.oauth2.member.service;


import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.dto.*;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.member.repository.UpdateNameRequestRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.inhabas.api.auth.domain.oauth2.member.domain.entity.MemberTest.*;
import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.EXECUTIVES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @InjectMocks
    MemberServiceImpl memberService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    UpdateNameRequestRepository updateNameRequestRepository;


    @DisplayName("미승인 회원을 [역할과 {학번 or 이름}] 으로 조회한다.")
    @Test
    void getNotApprovedMembersBySearchAndRoleTest() {
        //given
        Member member = notapprovedMember();
        given(memberRepository.findAllByRoleAndStudentIdLike(any(), any())).willReturn(List.of(member));
        NotApprovedMemberManagementDto notApprovedMemberManagementDto =
                new NotApprovedMemberManagementDto(
                        member.getName(),
                        member.getId(),
                        member.getStudentId(),
                        member.getPhone(),
                        member.getEmail(),
                        member.getSchoolInformation().getGrade(),
                        member.getSchoolInformation().getMajor());

        //when
        List<NotApprovedMemberManagementDto> notApprovedMemberManagementDtos =
                memberService.getNotApprovedMembersBySearchAndRole("12171707");

        //then
        assertThat(notApprovedMemberManagementDtos.get(0).getMemberId()).isEqualTo(notApprovedMemberManagementDto.getMemberId());

    }

    @DisplayName("비활동이상 회원을 [역할과 {학번 or 이름}] 으로 조회한다.")
    @Test
    void getApprovedMembersBySearchAndRole() {
        //given
        Member member = deactivatedMember();
        given(memberRepository.findAllByRolesInAndStudentIdLike(any(), any())).willReturn(List.of(member));
        ApprovedMemberManagementDto approvedMemberManagementDto =
                new ApprovedMemberManagementDto(
                        member.getName(),
                        member.getId(),
                        member.getStudentId(),
                        member.getPhone(),
                        member.getRole(),
                        member.getSchoolInformation().getGeneration(),
                        member.getSchoolInformation().getMajor());

        //when
        List<ApprovedMemberManagementDto> approvedMemberManagementDtos =
                memberService.getApprovedMembersBySearchAndRole("12171707");

        //then
        assertThat(approvedMemberManagementDtos.get(0).getMemberId()).isEqualTo(approvedMemberManagementDto.getMemberId());

    }

    @DisplayName("미승인 회원들을 합격하거나 불합격한다.")
    @ParameterizedTest
    @ValueSource(strings = {"pass", "fail"})
    void updateUnapprovedMembersTest(String state) {
        //given
        Member member = notapprovedMember();
        given(memberRepository.findAllById(any())).willReturn(List.of(member));

        //when
        memberService.updateUnapprovedMembers(any(), state);

        //then
        if (state.equals("pass"))
            then(memberRepository).should(times(1)).saveAll(any());
        else if (state.equals("fail"))
            then(memberRepository).should(times(1)).deleteAll(any());

    }

    @DisplayName("비활동 이상 회원들의 역할을 가능한 만큼만 수정한다.")
    @Test
    void updateApprovedMembersTest() {
        //given
        Member member = deactivatedMember();
        given(memberRepository.findAllById(any())).willReturn(List.of(member));

        //when
        memberService.updateApprovedMembers(List.of(1L), EXECUTIVES);

        //then
        then(memberRepository).should(times(1)).saveAll(any());

    }

    @DisplayName("회장 연락처 조회한다.")
    @Test
    public void getChiefContactTest() {
        //given
        Member member = chiefMember();
        given(memberRepository.findByIbasInformation_Role(any())).willReturn(member);

        //when
        ContactDto chiefContact = memberService.getChiefContact();

        //then
        assertThat(chiefContact.getEmail()).isEqualTo(member.getEmail());
        assertThat(chiefContact.getPhoneNumber()).isEqualTo(member.getPhone());
        assertThat(chiefContact.getName()).isEqualTo(member.getName());
    }

    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyProfileTest() {
        //given
        Member member = basicMember();
        given(memberRepository.findById(any())).willReturn(Optional.of(member));

        //when
        MyProfileDto myProfileDto = memberService.getMyProfile(any());

        //then
        assertThat(myProfileDto.getName()).isEqualTo(member.getName());
        assertThat(myProfileDto.getStudentId()).isEqualTo(member.getStudentId());
        assertThat(myProfileDto.getMajor()).isEqualTo(member.getSchoolInformation().getMajor());
        assertThat(myProfileDto.getGrade()).isEqualTo(member.getSchoolInformation().getGrade());
        assertThat(myProfileDto.getEmail()).isEqualTo(member.getEmail());
        assertThat(myProfileDto.getPhoneNumber()).isEqualTo(member.getPhone());
        assertThat(myProfileDto.getRole()).isEqualTo(member.getRole());
        assertThat(myProfileDto.getType()).isEqualTo(member.getSchoolInformation().getMemberType());
        assertThat(myProfileDto.getIntroduce()).isEqualTo(member.getIbasInformation().getIntroduce());

    }

    @DisplayName("내 정보 [학년, 전공, 전화번호] 를 수정한다.")
    @Test
    void updateMyProfileDetailTest() {
        //given
        Member member = basicMember();
        given(memberRepository.findById(any())).willReturn(Optional.of(member));
        ProfileDetailDto profileDetailDto = new ProfileDetailDto("경영학과", null, null);

        //when
        memberService.updateMyProfileDetail(any(), profileDetailDto);

        //then
        assertThat(member.getSchoolInformation().getMajor()).isEqualTo(profileDetailDto.getMajor());

    }

    @DisplayName("내 정보 [자기소개] 를 수정한다.")
    @Test
    void updateMyProfileIntroTest() {
        //given
        Member member = basicMember();
        given(memberRepository.findById(any())).willReturn(Optional.of(member));
        ProfileIntroDto profileIntroDto = new ProfileIntroDto("HELLO", true);

        //when
        memberService.updateMyProfileIntro(any(), profileIntroDto);

        //then
        assertThat(member.getIbasInformation().getIntroduce()).isEqualTo(profileIntroDto.getIntroduce());
        assertThat(member.getIbasInformation().getIsHOF()).isEqualTo(profileIntroDto.getIsHOF());

    }

    @DisplayName("내 정보 [이름] 을 수정 요청을 한다.")
    @Test
    void requestMyProfileNameTest() {
        //given
        Member member = basicMember();
        given(memberRepository.findById(any())).willReturn(Optional.of(member));
        ProfileNameDto profileNameDto = new ProfileNameDto("유동현");

        //when
        memberService.requestMyProfileName(any(), profileNameDto);

        //then
        then(updateNameRequestRepository).should(times(1)).save(any());

    }

}
