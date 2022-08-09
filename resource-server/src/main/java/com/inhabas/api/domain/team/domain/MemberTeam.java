package com.inhabas.api.domain.team.domain;

import com.inhabas.api.domain.member.domain.entity.Member;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity @Getter
@Table(name = "user_team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTeam {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", foreignKey = @ForeignKey(name = "fk_to_user"))
    private Member member;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="team_id", foreignKey = @ForeignKey(name = "fk_to_team"))
    private Team team;


    /* constructor */

    public MemberTeam(Member member, Team team) {
        setMember(member);
        setTeam(team);
    }


    /* relational methods */

    private void setMember(Member member) {
        this.member = member;
        member.addTeam(this);
    }

    private void setTeam(Team team) {
        this.team = team;
        team.addMember(this);

    }
}
