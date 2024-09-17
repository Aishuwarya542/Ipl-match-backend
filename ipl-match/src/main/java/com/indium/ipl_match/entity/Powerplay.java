package com.indium.ipl_match.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "powerplay")
public class Powerplay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int powerplayId;

    @Column(name = "start_over")
    private float startOver;

    @Column(name = "end_over")
    private float endOver;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "inning_id")
    private Inning inning;

    public void setFromOver(float from) {
        this.startOver = from;
    }

    public void setToOver(float to) {
        this.endOver = to;
    }
}
