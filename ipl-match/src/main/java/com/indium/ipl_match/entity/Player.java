package com.indium.ipl_match.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long playerId;

    @Column(name = "player_name", unique = true)
    private String playerName;

    @Column(name = "register_id")
    private String registerId;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;  // Many players belong to one team

    @ManyToOne
    @JoinColumn(name = "match_number")
    private MatchInfo matchInfo;  // Many players are part of one match
}
