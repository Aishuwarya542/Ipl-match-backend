package com.indium.ipl_match.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "toss")
public class Toss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tossId;

    @Column(name = "toss_winner")
    private String tossWinner;

    @Column(name = "decision")
    private String decision;

    @ManyToOne
    @JoinColumn(name = "match_number")
    private MatchInfo matchInfo;
}
