package com.swh.medicine.domain.users.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String device;
    private String guardianPhone;

    @Builder
    public Users(String device, String guardianPhone) {
        this.device = device;
        this.guardianPhone = guardianPhone;
    }

    public static Users of(String device) {
        return Users.builder()
                .device(device)
                .build();
    }

    public void addGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone;
    }
}
