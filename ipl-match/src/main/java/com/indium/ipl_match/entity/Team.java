package com.indium.ipl_match.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int teamId;

    @Column(name = "team_name", nullable = false)
    private String teamName;

    @ManyToOne
    @JoinColumn(name = "match_number")  // Foreign key referencing match_info
    private MatchInfo matchInfo;  // Many teams can participate in one match
}
