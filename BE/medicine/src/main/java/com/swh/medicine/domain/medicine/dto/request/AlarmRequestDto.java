package com.swh.medicine.domain.medicine.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AlarmRequestDto {
    private String name;
    private int[] days;
    private int hour;
    private int minute;
}
