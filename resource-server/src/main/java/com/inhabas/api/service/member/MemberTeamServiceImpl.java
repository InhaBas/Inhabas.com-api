package com.inhabas.api.service.member;

import com.inhabas.api.domain.member.*;
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
    public void addMemberToTeam(Integer memberId, Integer teamId) {
        Member memberProxy = memberRepository.getById(memberId);
        Team teamProxy = teamRepository.getById(teamId);

        memberTeamRepository.save(new MemberTeam(memberProxy, teamProxy));
    }

    @Override
    @Transactional
    public void deleteMemberFromTeam(Integer memberId, Integer teamId) {

        memberTeamRepository.deleteByMemberIdAndTeamId(memberId, teamId);
    }
}
