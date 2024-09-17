package com.indium.ipl_match.repository;

import com.indium.ipl_match.entity.MatchInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MatchInfoRepository extends CrudRepository<MatchInfo, Integer> {

    @Query("SELECT mi.matchNumber, SUM(d.runsTotal) AS totalScore " +
            "FROM MatchInfo mi " +
            "JOIN Inning i ON mi.matchNumber = i.matchInfo.matchNumber " +
            "JOIN Delivery d ON i.inningId = d.inning.inningId " +
            "WHERE mi.date = :matchDate " +
            "GROUP BY mi.matchNumber")
    List<Object[]> findMatchScoresByDate(@Param("matchDate") LocalDate matchDate);

}
