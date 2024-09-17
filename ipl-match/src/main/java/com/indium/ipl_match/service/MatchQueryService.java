package com.indium.ipl_match.service;

import com.indium.ipl_match.entity.*;
import com.indium.ipl_match.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class MatchQueryService {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MatchInfoRepository matchInfoRepository;

    // to get the number of matches played by a player -- Query 1
    @Cacheable(value = "matchesByPlayer", key = "#playerName")
    public List<MatchInfo> getMatchesByPlayerName(String playerName) {
        return playerRepository.findMatchesByPlayerName(playerName);
    }

    // to get the total runs scored by a player -- Query 2
    @Cacheable(value = "cumulativeScore", key = "#playerName")
    public Integer getCumulativeScoreByPlayerName(String playerName) {
        Integer cumulativeScore = playerRepository.findCumulativeScoreByPlayerName(playerName);

        if (cumulativeScore != null) {
            System.out.println("Cumulative score for " + playerName + ": " + cumulativeScore);
        } else {
            System.out.println("Player not found or no score available for " + playerName);
            cumulativeScore = 0;
        }

        return cumulativeScore;
    }

    // to get the total score of the match on the given date --Query 3
    @Cacheable(value = "matchScoresByDate", key = "#matchDate")
    public List<Map<String, Object>> getMatchScoresByDate(LocalDate matchDate) {
        List<Object[]> results = matchInfoRepository.findMatchScoresByDate(matchDate);
        List<Map<String, Object>> matchScores = new ArrayList<>();
        if (!results.isEmpty()) {
            for (Object[] result : results) {
                Map<String, Object> matchScoreMap = new HashMap<>();
                matchScoreMap.put("matchNumber", result[0]);
                matchScoreMap.put("totalScore", result[1]);
                matchScores.add(matchScoreMap);
            }
            System.out.println("Match scores on " + matchDate + ": " + matchScores);
        } else {
            System.out.println("No matches found on the date: " + matchDate);
        }
        return matchScores;
    }

    // to display the list of the required number top batsmen -- Query 4
    @Cacheable(value = "TopBatsmen", key = "#numberOfPlayers")
    public List<Object[]> getTopBatsmenWithNameAndId(int numberOfPlayers) {
        return playerRepository.findTopBatsmenWithNameAndId(PageRequest.of(0, numberOfPlayers)).getContent();
    }
}