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
    private String fcmToken;

    @Builder
    public Users(String device, String guardianPhone, String fcmToken) {
        this.device = device;
        this.guardianPhone = guardianPhone;
        this.fcmToken = fcmToken;
    }

    public static Users of(String device, String fcmToken) {
        return Users.builder()
                .device(device)
                .fcmToken(fcmToken)
                .build();
    }

    public void addGuardianPhone(String guardianPhone) {
        this.guardianPhone = guardianPhone;
    }
}
