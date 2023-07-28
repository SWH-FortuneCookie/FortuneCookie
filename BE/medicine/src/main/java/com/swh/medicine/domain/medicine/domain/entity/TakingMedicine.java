package com.swh.medicine.domain.medicine.domain.entity;

import com.swh.medicine.domain.users.domain.Users;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TakingMedicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @OneToMany(mappedBy = "takingMedicine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailTime> detailTimeList = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    private Medicine medicine;

    @Builder
    public TakingMedicine(Users users, Medicine medicine) {
        this.users = users;
        this.medicine = medicine;
    }

    public static TakingMedicine of(Users users, Medicine medicine) {
        return TakingMedicine.builder()
                .users(users)
                .medicine(medicine)
                .build();
    }
}
