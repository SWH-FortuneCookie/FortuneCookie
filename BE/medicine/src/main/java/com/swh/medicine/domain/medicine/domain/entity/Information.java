package com.swh.medicine.domain.medicine.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Information {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ingredient;

    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

    @Builder
    public Information(String ingredient, Medicine medicine) {
        this.ingredient = ingredient;
        this.medicine = medicine;
    }
}
