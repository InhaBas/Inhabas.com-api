package com.inhabas.api.domain.team.usecase;

import com.inhabas.api.domain.member.domain.entity.Member;
import com.inhabas.api.domain.team.domain.MemberTeam;
import com.inhabas.api.domain.team.domain.Team;
import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.member.repository.MemberRepository;
import com.inhabas.api.domain.team.repository.MemberTeamRepository;
import com.inhabas.api.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberTeamServiceImpl implements MemberTeamService {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final MemberTeamRepository memberTeamRepository;

    @Override
    @Transactional
    public void addMemberToTeam(MemberId memberId, Integer teamId) {
        Member memberProxy = memberRepository.getById(memberId);
        Team teamProxy = teamRepository.getById(teamId);

        memberTeamRepository.save(new MemberTeam(memberProxy, teamProxy));
    }

    @Override
    @Transactional
    public void deleteMemberFromTeam(MemberId memberId, Integer teamId) {

        memberTeamRepository.deleteByMemberIdAndTeamId(memberId, teamId);
    }
}
