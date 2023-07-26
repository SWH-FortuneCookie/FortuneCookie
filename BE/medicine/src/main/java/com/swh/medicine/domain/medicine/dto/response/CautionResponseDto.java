package com.swh.medicine.domain.medicine.dto.response;

import com.swh.medicine.domain.medicine.domain.entity.Medicine;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CautionResponseDto {
    private String caution;
    private String subName;

    @Builder
    public CautionResponseDto(String caution, String subName) {
        this.caution = caution;
        this.subName = subName;
    }

    public static CautionResponseDto of(Medicine medicine) {
        return CautionResponseDto.builder()
                .caution(medicine.getCaution())
                .subName(medicine.getSubName())
                .build();
    }
}
