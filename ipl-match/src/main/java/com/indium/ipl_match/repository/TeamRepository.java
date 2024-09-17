package com.indium.ipl_match.repository;

import com.indium.ipl_match.entity.Team;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TeamRepository extends CrudRepository<Team, Integer> {

    Optional<Team> findByTeamName(String teamName);

}
