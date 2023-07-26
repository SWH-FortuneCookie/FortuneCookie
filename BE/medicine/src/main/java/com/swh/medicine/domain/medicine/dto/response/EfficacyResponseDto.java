package com.swh.medicine.domain.medicine.dto.response;

import com.swh.medicine.domain.medicine.domain.entity.Efficacy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EfficacyResponseDto {
    private String type;
}
