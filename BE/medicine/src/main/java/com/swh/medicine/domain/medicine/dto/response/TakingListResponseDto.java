package com.swh.medicine.domain.medicine.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
public class TakingListResponseDto {
    private String subName;
    private boolean isAlarm; // 알람을 설정한 약인 경우에는 true, 아닌 경우에는 false
    private String shapeUrl;
    private String amount;

    // 알람 설정 한 경우에는 알람 시간이랑 복용량을 보여줘야함
    private String message; // 프론트에서 받기 쉽게 그냥 안에서 스트링을 합칠 수 있으면 합쳐서 전송해보기

    public TakingListResponseDto(String subName, boolean isAlarm, String shapeUrl, String amount, String message) {
        this.subName = subName;
        this.isAlarm = isAlarm;
        this.shapeUrl = shapeUrl;
        this.amount = amount;
        this.message = message;
    }
}
