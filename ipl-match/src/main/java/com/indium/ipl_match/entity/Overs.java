package com.indium.ipl_match.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "overs")
public class Overs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;  // Auto-incremented primary key

    @Column(name = "overs", nullable = false)
    private int overs;

    @ManyToOne
    @JoinColumn(name = "inning_id")  // Foreign key referencing inning table
    private Inning inning;

    public void setOverNumber(int over) {
        this.overs = over;
    }

    public int getOverNumber() {
        return this.overs;
    }
}
