package com.inhabas.api.domain.team.dto;

import com.inhabas.api.domain.team.domain.valueObject.TeamName;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class TeamSaveDto {

    private TeamName teamName;

    public TeamSaveDto(String teamName) {
        this.teamName = new TeamName(teamName);
    }

    public String getTeamName() {
        return teamName.getValue();
    }


}
