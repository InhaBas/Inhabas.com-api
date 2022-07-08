package com.inhabas.api.domain.team.repository;

import com.inhabas.api.domain.member.domain.valueObject.MemberId;
import com.inhabas.api.domain.team.domain.MemberTeam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberTeamRepository extends JpaRepository<MemberTeam, Integer> {

    void deleteByMemberIdAndTeamId(MemberId memberId, Integer teamId);
}
