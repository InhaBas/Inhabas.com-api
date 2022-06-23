package com.inhabas.api.service;

import com.inhabas.api.domain.member.*;
import com.inhabas.api.domain.member.type.IbasInformation;
import com.inhabas.api.domain.member.type.SchoolInformation;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.api.service.member.MemberTeamService;
import com.inhabas.api.service.member.MemberTeamServiceImpl;
import com.inhabas.testConfig.DefaultDataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultDataJpaTest
@Import({MemberTeamServiceImpl.class})
public class MemberTeamServiceTest {

    @Autowired private MemberTeamRepository memberTeamRepository;
    @Autowired private MemberTeamService memberTeamService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private TeamRepository teamRepository;
    @Autowired private TestEntityManager entityManager;


    private Member member;
    private Team team;

    @BeforeEach
    public void setUp() {
        member = memberRepository.save(new Member(
                new MemberId(12171652), "유동현", "010-1111-1111", "my@gmail.com", ""
                , SchoolInformation.ofUnderGraduate("건축공학과", 3)
                , new IbasInformation(Role.BASIC_MEMBER)));
        team = teamRepository.save(new Team("IT 부서"));

        entityManager.flush();
        entityManager.clear();
    }


    @DisplayName("회원을 팀에 추가한다.")
    @Test
    public void addMemberToTeamTest() {

        //when
        memberTeamService.addMemberToTeam(member.getId(), team.getId());

        //then
        MemberTeam memberTeam = memberTeamRepository.findAll().get(0);
        assertThat(memberTeam.getMember().getId()).isEqualTo(member.getId());
        assertThat(memberTeam.getTeam().getId()).isEqualTo(team.getId());
    }

    @DisplayName("회원을 팀에서 제외한다.")
    @Test
    public void deleteMemberFromTeamTest() {
        //given
        memberTeamService.addMemberToTeam(member.getId(), team.getId());

        //when
        memberTeamService.deleteMemberFromTeam(member.getId(), team.getId());

        //then
        assertThat(memberTeamRepository.count()).isEqualTo(0);
        assertThat(teamRepository.count()).isGreaterThan(0L);
        assertThat(memberRepository.count()).isGreaterThan(0L);
    }
}
