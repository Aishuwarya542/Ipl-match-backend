package com.indium.ipl_match.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "inning")
public class Inning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inningId;

    @ManyToOne
    @JoinColumn(name = "match_number")  // Foreign key referencing match_info
    private MatchInfo matchInfo;  // Many innings can belong to one match

    @ManyToOne
    @JoinColumn(name = "team_id")  // Foreign key referencing team
    private Team team;  // One team is associated with the inning

}
