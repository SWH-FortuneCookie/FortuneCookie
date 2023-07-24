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
    private Integer count;


    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @OneToMany(mappedBy = "takingMedicine", cascade = CascadeType.ALL)
    private List<Mapping> mappingList = new ArrayList<>();


    @Builder
    public TakingMedicine(Integer count, Users users) {
        this.count = count;
        this.users = users;
    }

    public static TakingMedicine of(Users users) {
        return TakingMedicine.builder()
                .users(users)
                .build();
    }
}
