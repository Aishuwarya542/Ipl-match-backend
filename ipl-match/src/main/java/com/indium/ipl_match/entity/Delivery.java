package com.indium.ipl_match.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int deliveryId;

    @ManyToOne
    @JoinColumn(name = "inning_id")
    private Inning inning;

    @Column(name = "over_number")
    private int overNumber;

    @Column(name = "ball_number")
    private int ballNumber;

    @Column(name = "runs_batter")
    private int runsBatter;

    @Column(name = "runs_extras")
    private int runsExtras;

    @Column(name = "runs_total")
    private int runsTotal;


    @ManyToOne
    @JoinColumn(name = "batter_id")
    private Player batter;

    @ManyToOne
    @JoinColumn(name = "bowler_id")
    private Player bowler;

    @ManyToOne
    @JoinColumn(name = "non_striker_id")
    private Player nonStriker;

    public void setOvers(Overs overEntity) {
    }
}
