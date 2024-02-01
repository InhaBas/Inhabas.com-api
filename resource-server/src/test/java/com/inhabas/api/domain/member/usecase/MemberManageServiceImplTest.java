package com.inhabas.api.domain.member.usecase;

import com.inhabas.api.auth.domain.oauth2.member.domain.entity.Member;
import com.inhabas.api.auth.domain.oauth2.member.dto.ApprovedMemberManagementDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.ContactDto;
import com.inhabas.api.auth.domain.oauth2.member.dto.NotApprovedMemberManagementDto;
import com.inhabas.api.auth.domain.oauth2.member.repository.MemberRepository;
import com.inhabas.api.auth.domain.oauth2.member.service.SMTPService;
import com.inhabas.api.auth.domain.oauth2.socialAccount.repository.MemberSocialAccountRepository;
import com.inhabas.api.domain.signUp.repository.AnswerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.MemberType.GRADUATED;
import static com.inhabas.api.auth.domain.oauth2.member.domain.valueObject.Role.BASIC;
import static com.inhabas.api.domain.member.domain.entity.MemberTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberManageServiceImplTest {

    @InjectMocks
    MemberManageServiceImpl memberManageService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    SMTPService amazonSMTPService;
    @Mock
    AnswerRepository answerRepository;
    @Mock
    MemberSocialAccountRepository memberSocialAccountRepository;


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
                        member.getSchoolInformation().getMemberType(),
                        member.getSchoolInformation().getGrade(),
                        member.getSchoolInformation().getMajor());

        //when
        List<NotApprovedMemberManagementDto> notApprovedMemberManagementDtos =
                memberManageService.getNotApprovedMembersBySearchAndRole("12171707");

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
                        member.getSchoolInformation().getMemberType(),
                        member.getRole(),
                        member.getSchoolInformation().getGeneration(),
                        member.getSchoolInformation().getMajor());

        //when
        List<ApprovedMemberManagementDto> approvedMemberManagementDtos =
                memberManageService.getApprovedMembersBySearchAndRole("12171707");

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
        memberManageService.updateUnapprovedMembers(any(), state);

        //then
        if (state.equals("pass"))
            then(memberRepository).should(times(1)).saveAll(any());
        else if (state.equals("fail"))
            then(memberRepository).should(times(1)).deleteAll(any());

    }

    @DisplayName("비활동 이상 회원들의 역할을 가능한 만큼만 수정한다.")
    @Test
    void updateApprovedMembersRoleTest() {
        //given
        Member member = deactivatedMember();
        given(memberRepository.findAllById(any())).willReturn(List.of(member));

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        given(securityContext.getAuthentication()).willReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_SECRETARY");
        Collection<GrantedAuthority> authorities = Collections.singletonList(authority);
        doReturn(authorities).when(authentication).getAuthorities();

        //when
        memberManageService.updateApprovedMembersRole(List.of(1L), BASIC);

        //then
        then(memberRepository).should(times(1)).saveAll(any());

    }

    @DisplayName("비활동 이상 회원들의 타입을 가능한 만큼만 수정한다.")
    @Test
    void updateApprovedMembersTypeTest() {
        //given
        Member member = deactivatedMember();
        given(memberRepository.findAllById(any())).willReturn(List.of(member));

        //when
        memberManageService.updateApprovedMembersType(List.of(1L), GRADUATED);

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
        ContactDto chiefContact = memberManageService.getChiefContact();

        //then
        assertThat(chiefContact.getEmail()).isEqualTo(member.getEmail());
        assertThat(chiefContact.getPhoneNumber()).isEqualTo(member.getPhone());
        assertThat(chiefContact.getName()).isEqualTo(member.getName());
    }

}