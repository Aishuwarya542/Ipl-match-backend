package com.indium.ipl_match.repository;

import com.indium.ipl_match.entity.*;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface PlayerRepository extends PagingAndSortingRepository<Player , Integer> {

    Optional<Player> findByPlayerName(String playerName);

    @Query("SELECT DISTINCT mi FROM MatchInfo mi " +
            "JOIN Player p ON mi.matchNumber = p.matchInfo.matchNumber " +
            "WHERE p.playerName = :playerName")
    List<MatchInfo> findMatchesByPlayerName(@Param("playerName") String playerName);

    @Query("SELECT SUM(d.runsBatter) FROM Delivery d " +
            "JOIN d.batter p " +
            "WHERE p.playerName = :playerName " +
            "GROUP BY p.playerName")
    Integer findCumulativeScoreByPlayerName(@Param("playerName") String playerName);

    @Query("SELECT p.playerId, p.playerName FROM Player p JOIN Delivery d ON p.playerId = d.batter.playerId " +
            "GROUP BY p.playerId, p.playerName " +
            "ORDER BY SUM(d.runsBatter) ASC")
    Page<Object[]> findTopBatsmenWithNameAndId(Pageable pageable);

    void save(Player player);
}
