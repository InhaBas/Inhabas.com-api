package com.inhabas.api.domain;

import com.inhabas.api.domain.member.*;
import com.inhabas.api.domain.member.type.IbasInformation;
import com.inhabas.api.domain.member.type.SchoolInformation;
import com.inhabas.api.domain.member.type.wrapper.Role;
import com.inhabas.testConfig.DefaultDataJpaTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DefaultDataJpaTest
public class MemberTeamTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberTeamRepository memberTeamRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired MemberRepository memberRepository;

    private Team IT;
    private Member member;

    @BeforeEach
    public void setUp() {
        IT = entityManager.persist(new Team("IT 부서"));

        member = entityManager.persist(
                new Member(12171652, "유동현", "010-0000-0000", "",
                        SchoolInformation.ofUnderGraduate("건축공학과", 3),
                        new IbasInformation(Role.BASIC_MEMBER)));

        memberTeamRepository.save(new MemberTeam(member, IT));

        entityManager.flush();
        entityManager.clear();
    }

    @DisplayName("팀을 삭제하면, 해당 팀에 속한 멤버들이 팀에서 방출된다.")
    @Test
    public void expelledByDeletingTeamsTest() {

        //when
        teamRepository.deleteById(IT.getId());
        entityManager.flush();
        entityManager.clear();

        //then
        assertThat(entityManager.find(Team.class, IT.getId())).isNull();
        assertTrue(memberTeamRepository.findAll().isEmpty());

    }

    @DisplayName("회원을 삭제하면, 해당 팀에 소속된 회원 목록에서 사라진다.")
    @Test
    public void vanishedByDeletingMemberProfileTest() {

        //when
        memberRepository.deleteById(member.getId());
        entityManager.flush();
        entityManager.clear();

        //then
        assertThat(entityManager.find(Member.class, member.getId())).isNull();
        assertTrue(memberTeamRepository.findAll().isEmpty());

    }

}
