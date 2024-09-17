package com.indium.ipl_match.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "target")
public class Target {

    @Id
    @Column(name = "inning_id")
    private int inningId;

    @Column(name = "target_runs")
    private int targetRuns;

    @Column(name = "target_overs")
    private int targetOvers;

    @OneToOne
    @MapsId
    @JoinColumn(name = "inning_id")
    private Inning inning;
}
