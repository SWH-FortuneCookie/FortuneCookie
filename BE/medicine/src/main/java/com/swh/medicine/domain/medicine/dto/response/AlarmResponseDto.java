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
    private int minutes;

    @Builder
    public AlarmResponseDto(String subName, List<Integer> days, int hour, int minutes) {
        this.subName = subName;
        this.days = days;
        this.hour = hour;
        this.minutes = minutes;
    }

    public static AlarmResponseDto of(String subName, List<Integer> days, int hour, int minutes) {
        return AlarmResponseDto.builder()
                .subName(subName)
                .days(days)
                .hour(hour)
                .minutes(minutes)
                .build();
    }
}
