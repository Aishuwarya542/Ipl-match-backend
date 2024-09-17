package com.indium.ipl_match.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "official")
public class Official {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int officialId;

    @Column(name = "official_name", nullable = false)
    private String officialName;

    @Column(name = "role", nullable = false)
    private String role;

    @ManyToOne
    @JoinColumn(name = "match_number")  // Foreign key referencing match_info
    private MatchInfo matchInfo;  // Many officials can be assigned to one match
}
