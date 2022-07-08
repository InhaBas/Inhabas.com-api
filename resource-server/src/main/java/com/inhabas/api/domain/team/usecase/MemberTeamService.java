package com.inhabas.api.domain.team.usecase;

import com.inhabas.api.domain.member.domain.valueObject.MemberId;

public interface MemberTeamService {

    void addMemberToTeam(MemberId memberId, Integer TeamId);

    void deleteMemberFromTeam(MemberId memberId, Integer TeamId);
}
