package com.swh.medicine.domain.medicine.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Efficacy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

    @Builder
    public Efficacy(String type, Medicine medicine) {
        this.type = type;
        this.medicine = medicine;
    }
}
