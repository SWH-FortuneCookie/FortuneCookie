package com.swh.medicine.domain.medicine.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AlarmResponseDto {
    private String subName;
    private List<Integer> days;
    private int hour;
    private int minute;

    @Builder
    public AlarmResponseDto(String subName, List<Integer> days, int hour, int minute) {
        this.subName = subName;
        this.days = days;
        this.hour = hour;
        this.minute = minute;
    }

    public static AlarmResponseDto of(String subName, List<Integer> days, int hour, int minute) {
        return AlarmResponseDto.builder()
                .subName(subName)
                .days(days)
                .hour(hour)
                .minute(minute)
                .build();
    }
}
