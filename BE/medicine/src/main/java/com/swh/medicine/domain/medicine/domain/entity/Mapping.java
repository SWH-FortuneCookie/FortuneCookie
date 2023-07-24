package com.swh.medicine.domain.medicine.domain.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Mapping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TakingMedicine takingMedicine;

    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

    @Builder
    public Mapping(TakingMedicine takingMedicine, Medicine medicine) {
        this.takingMedicine = takingMedicine;
        this.medicine = medicine;
    }
}
