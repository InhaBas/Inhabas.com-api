package com.inhabas.api.domain.team.repository;

import com.inhabas.api.domain.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Integer> {
}
