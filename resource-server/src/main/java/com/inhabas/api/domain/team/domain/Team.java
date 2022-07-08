package com.inhabas.api.domain.team.domain;

import com.inhabas.api.domain.team.domain.valueObject.TeamName;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private TeamName name;

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
    private List<MemberTeam> memberList = new ArrayList<>();

    public Team(String name) {
        this.name = new TeamName(name);
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public void addMember(MemberTeam member) {
        memberList.add(member);
    }
}

