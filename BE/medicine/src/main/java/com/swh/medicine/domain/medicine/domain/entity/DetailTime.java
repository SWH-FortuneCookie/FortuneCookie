package com.swh.medicine.domain.medicine.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DetailTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int hour; // 1-24
    private int minute; // 0-59
    private int day; // 1-7

    @ManyToOne(fetch = FetchType.LAZY)
    private TakingMedicine takingMedicine;

    @Builder
    public DetailTime(int hour, int minute, int day, TakingMedicine takingMedicine) {
        this.hour = hour;
        this.minute = minute;
        this.day = day;
        this.takingMedicine = takingMedicine;
    }
}
