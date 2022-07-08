package com.inhabas.api.domain.team.usecase;

import static org.assertj.core.api.Assertions.assertThat;

import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.member.domain.valueObject.IbasInformation;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.domain.valueObject.Role;
import com.inhabas.api.domain.member.domain.valueObject.SchoolInformation;
import com.inhabas.api.domain.member.repository.MemberRepository;
import com.inhabas.api.domain.team.domain.MemberTeam;
import com.inhabas.api.domain.team.domain.Team;
import com.inhabas.api.domain.team.repository.MemberTeamRepository;
import com.inhabas.api.domain.team.repository.TeamRepository;
import com.inhabas.testAnnotataion.DefaultDataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

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
