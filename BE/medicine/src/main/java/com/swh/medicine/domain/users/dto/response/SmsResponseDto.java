package com.swh.medicine.domain.users.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class SmsResponseDto {
    private String name;
    private String phone;

    public SmsResponseDto(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public static SmsResponseDto of(String name, String phone) {
        return SmsResponseDto.builder()
                .name(name)
                .phone(phone)
                .build();
    }
}
