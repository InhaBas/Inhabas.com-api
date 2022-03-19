package com.inhabas.api.controller;

import com.inhabas.api.dto.member.TeamDto;
import com.inhabas.api.dto.member.TeamSaveDto;
import com.inhabas.api.service.member.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Tag(name = "부서(team)", description = "id 에 맞는 부서 없으면, 400 BadRequest.")
@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/{id}")
    @Operation(summary = "해당하는 부서 id 에 맞는 이름을 반환한다.")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<TeamDto> getTeamInfo(@PathVariable Integer id) {
        TeamDto teamInfo = teamService.getTeamInfo(id);
        return ResponseEntity.ok(teamInfo);
    }

    @GetMapping
    @Operation(summary = "모든 부서 정보를 불러온다.")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<List<TeamDto>> getAllTeamInfo() {
        List<TeamDto> allTeamInfo = teamService.getAllTeamInfo();
        return ResponseEntity.ok(allTeamInfo);
    }

    @PutMapping
    @Operation(summary = "id 에 해당하는 부서 이름을 변경한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "20자 넘어가면 안됨.")
    })
    public ResponseEntity<?> updateTeamInfo(@Valid @RequestBody TeamDto teamDto) {
        teamService.update(teamDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "id 해당하는 부서를 삭제한다.")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<?> deleteTeamInfo(@PathVariable Integer id) {
        teamService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @Operation(summary = "부서를 새로 생성한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "20자 넘어가면 안됨.")
    })
    public ResponseEntity<?> createTeam(@RequestBody TeamSaveDto teamSaveDto) {
        teamService.create(teamSaveDto);
        return ResponseEntity.noContent().build();
    }

}
