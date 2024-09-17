package com.indium.ipl_match.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.core.serializer.Serializer;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "match_info")
public class MatchInfo implements Serializable {

    @Id
    private int matchNumber;  // match_number is the primary key

    @Column(name = "city")
    private String city;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "match_type")
    private String matchType;

    @Column(name = "venue")
    private String venue;

    @Column(name = "season")
    private String season;

    @Column(name = "gender")
    private String gender;

    @Column(name = "balls_per_over")
    private int ballsPerOver;

    @Column(name = "overs")
    private int overs;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "winner")
    private String winner;

    @Column(name = "outcome_by_runs")
    private int outcomeByRuns;

    @Column(name = "player_of_match")
    private String playerOfMatch;

    @ManyToOne
    @JoinColumn(name = "meta_id")  // Defines the foreign key in match_info pointing to meta_id
    private Meta meta;  // Many MatchInfo entries are linked to one Meta
}
