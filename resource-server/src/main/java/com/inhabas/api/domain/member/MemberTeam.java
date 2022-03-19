package com.inhabas.api.domain.member;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@Table(name = "user_team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberTeam {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", foreignKey = @ForeignKey(name = "fk_to_user"))
    private Member member;

    /**
     * 조인 엔티티를 참조하는 행위 자체가 team 을 얻기 위한 행동이기 때문에, <br>
     * MemberTeam 을 가져오는 순간 team 까지 같이 가져온다.
     */
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
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
