package com.swh.medicine.domain.users.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class InformationResponseDto {
    private String name;
    private String phone;

    public InformationResponseDto(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public static InformationResponseDto of(String name, String phone) {
        return InformationResponseDto.builder()
                .name(name)
                .phone(phone)
                .build();
    }
}
