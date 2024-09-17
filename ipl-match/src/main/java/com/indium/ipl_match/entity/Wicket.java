package com.indium.ipl_match.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "wicket")
public class Wicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int wicketId;

    @Column(name = "player_out")
    private String playerOut;

    @Column(name = "kind_of_dismissal")
    private String kindOfDismissal;

    @ManyToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;
}
