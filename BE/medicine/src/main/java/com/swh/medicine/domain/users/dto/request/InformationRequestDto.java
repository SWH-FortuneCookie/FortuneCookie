package com.swh.medicine.domain.users.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InformationRequestDto {
    private String name;
    private String phone;
    private String medicineName;
}
