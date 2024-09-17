package com.indium.ipl_match.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "meta")
public class Meta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long metaId;

    @Column(name = "data_version")
    private String dataVersion;

    @Column(name = "created")
    private LocalDate created;

    @Column(name = "revision")
    private int revision;
}
