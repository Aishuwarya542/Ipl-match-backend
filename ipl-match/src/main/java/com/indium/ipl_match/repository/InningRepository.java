package com.indium.ipl_match.repository;

import com.indium.ipl_match.entity.Inning;
import com.indium.ipl_match.entity.MatchInfo;
import org.springframework.data.repository.CrudRepository;

public interface InningRepository extends CrudRepository<Inning , Integer> {

}
